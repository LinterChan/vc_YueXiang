package com.linter.vc_yuexiang.login;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.linter.vc_yuexiang.common.CorrectnessListener;
import com.linter.vc_yuexiang.common.ResultConst;
import android.widget.EditText;

/**
 * 获取网络请求的参数
 * 
 * @author LinterChen linterchen@vanchu.net
 * @date 2015-11-19
 */
public class RequestLoginDataGetter {

	public static Map<String, String> getRequestData(EditText usernameEditText,
			EditText passwordEditText, CorrectnessListener listener) {
		String username = usernameEditText.getText().toString().trim();
		String password = passwordEditText.getText().toString().trim();
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
