package com.linter.vc_yuexiang.login;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.linter.vc_yuexiang.common.ResultConst;
import com.linter.vc_yuexiang.http.HttpClientUtil;
import com.linter.vc_yuexiang.http.HttpRequestHelper;
import com.linter.vc_yuexiang.http.HttpRequestHelper.HandleResultListener;
import com.linter.vc_yuexiang.register.RegisterModel.CorrectnessListener;

public class LoginModel {

	public static void requestToServer(String username, String password,
			HandleResultListener hListener, CorrectnessListener cListener) {
		Map<String, String> map = getRequestData(username, password, cListener);
		if (map != null) {
			String url = HttpClientUtil.URL_IP + "/LoginServlet";
			HttpRequestHelper helper = new HttpRequestHelper(url, map);
			helper.setDoResultListener(hListener);
			helper.execute();
		}
	}

	private static Map<String, String> getRequestData(String username,
			String password, CorrectnessListener listener) {
		if (isCorrectOfInput(username, password, listener)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("username", username);
			map.put("password", password);
			return map;
		}
		return null;
	}

	private static boolean isCorrectOfInput(String username, String password,
			CorrectnessListener listener) {
		boolean isCorrect = false;
		int resultCode = ResultConst.LOGIN_USER_NULL;
		if (username.equals("")) {
			resultCode = ResultConst.LOGIN_USER_NULL;
			isCorrect = false;
		}
		if (password.equals("")) {
			resultCode = ResultConst.LOGIN_PASSWORD_NULL;
			isCorrect = false;
		}
		if (!isEmail(username)) {
			resultCode = ResultConst.LOGIN_EMAIL_NOT_CORRECT;
			isCorrect = false;
		}
		isCorrect = true;
		listener.doResultAfterJudgeInput(resultCode);
		return isCorrect;
	}

	private static boolean isEmail(String username) {
		String regex = "\\w+@\\w+\\.\\w+";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(username);
		return matcher.matches();
	}

}
