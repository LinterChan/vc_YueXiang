package com.linter.vc_yuexiang.http;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.example.vc_yuexiang.R;
import com.linter.vc_yuexiang.http.HttpRequestHelper.HandleResultListener;

public class ImageLoader {
	private Set<LoadImageTask> tasks;
	private LruCache<String, Bitmap> memoryCaches;

	public ImageLoader() {
		tasks = new HashSet<LoadImageTask>();
		initMemoryCaches();
	}

	private void initMemoryCaches() {
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 10;
		memoryCaches = new LruCache<String, Bitmap>(cacheSize) {
			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}
		};
	}

	public void showImage(String url, ImageView imageView) {
		Bitmap bitmap = getBitmapFromMemoryCaches(url);
		if (bitmap == null) {
			imageView.setImageResource(R.drawable.ic_launcher);
		} else {
			imageView.setImageBitmap(bitmap);
		}
	}

	private Bitmap getBitmapFromMemoryCaches(String url) {
		return memoryCaches.get(url);
	}

	private void addBitmapToMemoryCaches(String url, Bitmap bitmap) {
		if (memoryCaches.get(url) == null) {
			memoryCaches.put(url, bitmap);
		}
	}

	public void loadImage(String url, HandleResultListener listener) {
		Bitmap bitmap = getBitmapFromMemoryCaches(url);
		if (bitmap == null) {
			LoadImageTask loadImageTask = new LoadImageTask(url, listener);
			tasks.add(loadImageTask);
			loadImageTask.execute();
		} else {
			if (listener != null) {
				listener.doResult(bitmap);
			}
		}
	}

	private static Bitmap getBitmapFromUrl(String str_url) {
		Bitmap bitmap = null;
		URL url = null;
		try {
			url = new URL(str_url);
			HttpURLConnection conn = null;
			conn = (HttpURLConnection) url.openConnection();
			InputStream is = null;
			is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bitmap = BitmapFactory.decodeStream(bis);
			is.close();
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public void cancelAllTasks() {
		if (tasks != null) {
			for (LoadImageTask task : tasks) {
				task.cancel(false);
			}
		}
	}

	private class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {
		private String url;
		private HandleResultListener listener;

		public LoadImageTask(String url, HandleResultListener listener) {
			this.url = url;
			this.listener = listener;
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			Bitmap bitmap = getBitmapFromUrl(url);
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
