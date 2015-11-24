package com.linter.vc_yuexiang.home;

import java.util.HashMap;
import java.util.Map;

public class RequestLoveDataGetter {
	public static Map<String, String> getRequestData(String sid, String username) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("sid", sid);
		map.put("username", username);
		return map;
	}
}
