package com.linter.vc_yuexiang.login;

import java.util.Map;

import com.linter.vc_yuexiang.common.DoResultListener;
import com.linter.vc_yuexiang.http.HttpRequestHelper;

public class LoginRequester {

	public static void requestToServer(String url, Map<String, String> map,
			DoResultListener listener) {
		HttpRequestHelper helper = new HttpRequestHelper(url, map);
		helper.setDoResultListener(listener);
		helper.execute();
	}

}
