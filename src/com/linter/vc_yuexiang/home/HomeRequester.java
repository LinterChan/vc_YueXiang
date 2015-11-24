package com.linter.vc_yuexiang.home;

import java.util.Map;

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

	public static void loveSong(Map<String, String> map,
			DoResultListener listener) {
		String url = HttpClientUtil.URL_IP + "/LoveSongServlet";
		HttpRequestHelper helper = new HttpRequestHelper(url, map);
		helper.setDoResultListener(listener);
		helper.execute();
	}
	
	public static void cancelLoveSong(Map<String, String> map,
			DoResultListener listener) {
		String url = HttpClientUtil.URL_IP + "/CancelLoveSongServlet";
		HttpRequestHelper helper = new HttpRequestHelper(url, map);
		helper.setDoResultListener(listener);
		helper.execute();
	}
}
