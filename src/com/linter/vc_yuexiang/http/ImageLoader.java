package com.linter.vc_yuexiang.http;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;

import com.linter.vc_yuexiang.http.HttpRequestHelper.HandleResultListener;

public class ImageLoader {
	private Set<LoadImageTask> tasks;
	private LruCache<String, Bitmap> memoryCaches;

	public ImageLoader() {
		tasks = new HashSet<LoadImageTask>();
		initMemoryCaches();
	}

	private void initMemoryCaches() {
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		int cacheSize = maxMemory / 10;
		memoryCaches = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight() / 1024;
				// return value.getByteCount()/1024;
			}
		};
	}

	public Bitmap getBitmapFromMemoryCaches(String url) {
		return memoryCaches.get(url);
	}

	private void addBitmapToMemoryCaches(String url, Bitmap bitmap) {
		if (memoryCaches.get(url) == null) {
			memoryCaches.put(url, bitmap);
		}
	}

	public void loadImage(String url, int reqWidth, int reqHeight,
			HandleResultListener listener) {
		Bitmap bitmap = getBitmapFromMemoryCaches(url);
		if (bitmap == null) {
			LoadImageTask loadImageTask = new LoadImageTask(url, listener);
			tasks.add(loadImageTask);
			loadImageTask.execute(reqWidth, reqHeight);
		} else {
			if (listener != null) {
				listener.doResult(bitmap);
			}
		}
	}

	private static Bitmap getBitmapFromUrlWithCompressed(String str_url,
			int reqWidth, int reqHeight) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		URL url = null;
		try {
			url = new URL(str_url);
			HttpURLConnection conn = null;
			conn = (HttpURLConnection) url.openConnection();
			InputStream is = null;
			is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			BitmapFactory.decodeStream(bis, null, options);

			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);
			options.inJustDecodeBounds = false;

			conn = (HttpURLConnection) url.openConnection();
			is = conn.getInputStream();
			bis = new BufferedInputStream(is);
			bitmap = BitmapFactory.decodeStream(bis, null, options);
			
			conn.disconnect();
			is.close();
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
//		System.out.println("options:"+width+"*"+height);
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRate = Math.round((float) height
					/ (float) reqHeight);
			final int widthRate = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRate < widthRate ? heightRate : widthRate;
		}
		System.out.println("inSampleSize:"+inSampleSize);
		return inSampleSize;
	}

	public void cancelAllTasks() {
		if (tasks != null) {
			for (LoadImageTask task : tasks) {
				task.cancel(false);
			}
		}
	}

	private class LoadImageTask extends AsyncTask<Integer, Void, Bitmap> {
		private String url;
		private HandleResultListener listener;

		public LoadImageTask(String url, HandleResultListener listener) {
			this.url = url;
			this.listener = listener;
		}

		@Override
		protected Bitmap doInBackground(Integer... params) {
			Bitmap bitmap = getBitmapFromUrlWithCompressed(url, params[0],
					params[1]);
			if (bitmap != null) {
				addBitmapToMemoryCaches(url, bitmap);
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			if (listener != null) {
				listener.doResult(bitmap);
			}
			tasks.remove(this);
		}
	}
}
