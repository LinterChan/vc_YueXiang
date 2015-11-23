package com.linter.vc_yuexiang.register;

import java.util.Map;

import com.linter.vc_yuexiang.common.DoResultListener;
import com.linter.vc_yuexiang.http.HttpClientUtil;
import com.linter.vc_yuexiang.http.HttpRequestHelper;

public class RegisterRequester {

	public static void requestToServer(Map<String, String> map,
			DoResultListener listener) {
		String url = HttpClientUtil.URL_IP + "/RegisterServlet";
		HttpRequestHelper helper = new HttpRequestHelper(url, map);
		helper.setDoResultListener(listener);
		helper.execute();
	}
	
}
