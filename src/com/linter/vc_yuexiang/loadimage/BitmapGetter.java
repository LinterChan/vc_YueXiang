package com.linter.vc_yuexiang.loadimage;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapGetter {
	public static Bitmap getBitmapFromUrlWithCompressed(String str_url,
			int reqWidth, int reqHeight) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		URL url = null;
		try {
			url = new URL(str_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
		// System.out.println("options:"+width+"*"+height);
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRate = Math.round((float) height
					/ (float) reqHeight);
			final int widthRate = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRate < widthRate ? heightRate : widthRate;
		}
		// System.out.println("inSampleSize:"+inSampleSize);
		return inSampleSize;
	}
}
