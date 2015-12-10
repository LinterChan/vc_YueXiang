package com.linter.vc_yuexiang.loadimage;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.linter.vc_yuexiang.http.HttpRequestHelper.HandleResultListener;

public class ImageLoader {
	private Set<LoadImageTask> tasks;
	private MemoryCaches memoryCaches;
	private DiskCaches diskCaches;

	public ImageLoader(Context context) {
		tasks = new HashSet<LoadImageTask>();
		memoryCaches = new MemoryCaches();
		diskCaches = new DiskCaches(context);
	}

	public void loadImage(String url, int reqWidth, int reqHeight,
			HandleResultListener listener) {
		Bitmap bitmap = getBitmapFromCaches(url);
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

	public Bitmap getBitmapFromCaches(String url) {
		Bitmap bitmap = memoryCaches.getBitmapFromMemoryCaches(url);
		if (bitmap == null) {
			bitmap = diskCaches.getBitmapFromDiskCaches(url);
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

	private class LoadImageTask extends AsyncTask<Integer, Void, Bitmap> {
		private String url;
		private HandleResultListener listener;

		public LoadImageTask(String url, HandleResultListener listener) {
			this.url = url;
			this.listener = listener;
		}

		@Override
		protected Bitmap doInBackground(Integer... params) {
			Bitmap bitmap = BitmapGetter.getBitmapFromUrlWithCompressed(url,
					params[0], params[1]);
			if (bitmap != null) {
				memoryCaches.addBitmapToMemoryCaches(url, bitmap);
				diskCaches.addBitmapToDiskCaches(url, bitmap);
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
