package com.linter.vc_yuexiang.http;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.linter.vc_yuexiang.http.HttpRequestHelper.HandleResultListener;

public class ImageLoader {
	private String url = null;
	private HandleResultListener listener = null;

	public ImageLoader(String url) {
		this.url = url;
	}

	public void setDoResultListener(HandleResultListener listener) {
		this.listener = listener;
	}

	private class GetImageTask extends AsyncTask<Void, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(Void... arg0) {
			Bitmap bitmap = getBitmapFromURL(url);
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (listener != null) {
				listener.doResult(bitmap);
			}
		}
	}

	private Bitmap getBitmapFromURL(String str_url) {
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

	public void execute() {
		GetImageTask task = new GetImageTask();
		task.execute();
	}
}
