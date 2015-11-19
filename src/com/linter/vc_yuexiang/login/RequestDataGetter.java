package com.linter.vc_yuexiang.login;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 获取网络请求的参数
 * 
 * @author LinterChen linterchen@vanchu.net
 * @date 2015-11-19
 */
public class RequestDataGetter {

	public static Map<String, String> getRequestData(Context context,
			EditText usernameEditText, EditText passwordEditText) {
		String username = usernameEditText.getText().toString().trim();
		String password = passwordEditText.getText().toString().trim();
		if (isCorrectOfInput(context, username, password)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("username", username);
			map.put("password", password);
			return map;
		}
		return null;
	}

	public static boolean isCorrectOfInput(Context context, String username,
			String password) {
		if (username.equals("")) {
			Toast.makeText(context, "用户名不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (password.equals("")) {
			Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
}
