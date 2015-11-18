package com.linter.vc_yuexiang.register;

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

/**
 * 注册Activity
 * 
 * @author LinterChen linterchen@vanchu.net
 * @date 2015-11-16
 */
public class RegisterActivity extends Activity {
	private EditText usernameEditText;
	private EditText passwordEditText;
	private Button registerButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		initView();
		setupRegisterButton();
	}

	public void initView() {
		usernameEditText = (EditText) findViewById(R.id.register_username_edittext);
		passwordEditText = (EditText) findViewById(R.id.register_password_edittext);
		registerButton = (Button) findViewById(R.id.register_register_button);
	}

	public void setupRegisterButton() {
		registerButton.setOnClickListener(new RegisterButtonListener());
	}

	class RegisterButtonListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			Map<String, String> map = getRequestData();
			if (map != null) {
				String url = HttpUtil.URL_IP + "/RegisterServlet";
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
				case ResultConst.REG_USER_EXIST:
					Toast.makeText(RegisterActivity.this, "该用户已存在",
							Toast.LENGTH_SHORT).show();
					break;
				case ResultConst.REG_SUCCESS:
					// 跳转到HomeActivity

					finish();
					break;
				case ResultConst.REG_FAIL:
					Toast.makeText(RegisterActivity.this, "注册失败,请稍后重试",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
		helper.execute();
	}

	public boolean isCorrectOfInput(String username, String password) {
		if (username.equals("")) {
			Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (password.equals("")) {
			Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

}
