package com.linter.vc_yuexiang.radio;

import com.linter.vc_yuexiang.http.ImageLoader;
import com.linter.vc_yuexiang.http.HttpRequestHelper.HandleResultListener;

public class RadioModel {
	public static void loadImage(String url, HandleResultListener listener) {
		ImageLoader loader = new ImageLoader(url);
		loader.setHandleResultListener(listener);
		loader.execute();
	}
}
