package com.linter.vc_yuexiang.home;

import com.linter.vc_yuexiang.common.DoResultListener;
import com.linter.vc_yuexiang.http.HttpClientUtil;
import com.linter.vc_yuexiang.http.HttpRequestHelper;

public class HomeRequester {
	public static void getSongData(DoResultListener listener) {
		String url = HttpClientUtil.URL_IP + "/GetSongDataServlet";
		HttpRequestHelper helper = new HttpRequestHelper(url, null);
		helper.setDoResultListener(listener);
		helper.execute();
	}
}
