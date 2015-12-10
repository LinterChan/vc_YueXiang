package com.linter.vc_yuexiang.loadimage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import libcore.io.DiskLruCache;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class DiskCaches {
	private int cacheSize = 5 * 1024 * 1024;
	private DiskLruCache caches = null;
	private Context context = null;

	public DiskCaches(Context context) {
		this.context = context;
		initDiskCaches();
	}

	private void initDiskCaches() {
		File cacheDir = getDiskCacheDir(context, "bitmap");
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		try {
			caches = DiskLruCache.open(cacheDir, getAppVersion(context), 1,
					cacheSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	private int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}

	public void addBitmapToDiskCaches(String url, Bitmap bitmap) {
		if (getBitmapFromDiskCaches(hashKeyForUrl(url)) == null) {
			try {
				DiskLruCache.Editor editor = caches.edit(hashKeyForUrl(url));
				if (editor != null) {
					OutputStream outputStream = editor.newOutputStream(0);
					boolean isOutputSuccess = bitmap.compress(
							Bitmap.CompressFormat.JPEG, 100, outputStream);
					outputStream.flush();
					outputStream.close();
					if (isOutputSuccess) {
						editor.commit();
					} else {
						editor.abort();
					}
				}
				// 将内存的操作记录同步到日志文件(journal文件)
				caches.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Bitmap getBitmapFromDiskCaches(String url) {
		Bitmap bitmap = null;
		try {
			DiskLruCache.Snapshot snapshot = caches.get(hashKeyForUrl(url));
			if (snapshot != null) {
				InputStream inputStream = snapshot.getInputStream(0);
				bitmap = BitmapFactory.decodeStream(inputStream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	private String hashKeyForUrl(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	private String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
}
