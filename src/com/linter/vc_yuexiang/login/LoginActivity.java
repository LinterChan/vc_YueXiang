package com.linter.vc_yuexiang.login;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vc_yuexiang.R;
import com.linter.vc_yuexiang.common.ResultConst;
import com.linter.vc_yuexiang.http.HttpRequestHelper;
import com.linter.vc_yuexiang.http.HttpRequestHelper.DoResultListener;
import com.linter.vc_yuexiang.http.HttpUtil;

public class LoginActivity extends Activity {
	private EditText usernameEditText;
	private EditText passwordEditText;
	private Button loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initView();
		setupLoginButton();
	}

	public void initView() {
		usernameEditText = (EditText) findViewById(R.id.login_username_edittext);
		passwordEditText = (EditText) findViewById(R.id.login_password_edittext);
		loginButton = (Button) findViewById(R.id.login_login_button);
	}

	public void setupLoginButton() {
		loginButton.setOnClickListener(new LoginButtonListener());
	}

	class LoginButtonListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			Map<String, String> map = getRequestData();
			if (map != null) {
				String url = HttpUtil.URL_IP + "/LoginServlet";
				requestToServer(url, map);
			}
		}
	}

	public Map<String, String> getRequestData() {
		String username = usernameEditText.getText().toString().trim();
		String password = passwordEditText.getText().toString().trim();
		if (isCorrectOfInput(username, password)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("username", username);
			map.put("password", password);
			return map;
		}
		return null;
	}

	public void requestToServer(String url, Map<String, String> map) {
		HttpRequestHelper helper = new HttpRequestHelper(url, map);
		helper.setDoResultListener(new DoResultListener() {
			@Override
			public void doResult(String result) {
				switch (Integer.parseInt(result)) {
				case ResultConst.LOGIN_USER_NOT_EXIST:
					Toast.makeText(LoginActivity.this, "用户不存在",
							Toast.LENGTH_SHORT).show();
					break;
				case ResultConst.LOGIN_PASSWORD_WORRY:
					Toast.makeText(LoginActivity.this, "密码错误",
							Toast.LENGTH_SHORT).show();
					break;
				case ResultConst.LOGIN_SUCCESS:
					// 跳转到HomeActivity主页

					finish();
					break;
				case ResultConst.LOGIN_FAIL:
					Toast.makeText(LoginActivity.this, "登录失败,请稍后重试",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
		helper.execute();
	}

	public boolean isCorrectOfInput(String username, String password) {
		if (username.equals("")) {
			Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (password.equals("")) {
			Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}
}
