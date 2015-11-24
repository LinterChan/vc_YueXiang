package com.linter.vc_yuexiang.home;

import com.linter.vc_yuexiang.common.DoResultListener;
import com.linter.vc_yuexiang.http.ImageLoader;

public class HomeBackgroundLoader {
	public static void loadImage(String url, DoResultListener listener) {
		ImageLoader loader = new ImageLoader(url);
		loader.setDoResultListener(listener);
		loader.execute();
	}
}
