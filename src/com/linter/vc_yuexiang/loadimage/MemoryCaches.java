package com.linter.vc_yuexiang.loadimage;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class MemoryCaches {
	private LruCache<String, Bitmap> caches = null;

	public MemoryCaches() {
		initMemoryCaches();
	}

	private void initMemoryCaches() {
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		int cacheSize = maxMemory / 10;
		caches = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight() / 1024;
				// return value.getByteCount()/1024;
			}
		};
	}

	public Bitmap getBitmapFromMemoryCaches(String url) {
		return caches.get(url);
	}

	public void addBitmapToMemoryCaches(String url, Bitmap bitmap) {
		if (caches.get(url) == null) {
			caches.put(url, bitmap);
		}
	}
}
