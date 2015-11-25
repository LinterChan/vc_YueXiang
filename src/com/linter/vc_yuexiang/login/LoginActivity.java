package com.linter.vc_yuexiang.login;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vc_yuexiang.R;
import com.linter.vc_yuexiang.common.BaseActivity;
import com.linter.vc_yuexiang.common.ResultConst;
import com.linter.vc_yuexiang.http.HttpRequestHelper.HandleResultListener;
import com.linter.vc_yuexiang.network.NetworkConnDetector;
import com.linter.vc_yuexiang.register.RegisterModel.CorrectnessListener;

/**
 * 
 * @author LinterChen linterchen@vanchu.net
 * @date 2015-11-20
 */
public class LoginActivity extends BaseActivity {
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

	private void initView() {
		usernameEditText = (EditText) findViewById(R.id.login_username_edittext);
		passwordEditText = (EditText) findViewById(R.id.login_password_edittext);
		loginButton = (Button) findViewById(R.id.login_login_button);
	}

	private void setupLoginButton() {
		loginButton.setOnClickListener(new LoginButtonListener());
	}

	private class LoginButtonListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			if (NetworkConnDetector.isNetworkConnected(getApplicationContext())) {
				LoginModel.requestToServer(usernameEditText.getText()
						.toString().trim(), passwordEditText.getText()
						.toString().trim(), new LoginResultListener(),
						new LoginCorrectnessListener());
			} else {
				Toast.makeText(LoginActivity.this, "网络未连接", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	class LoginResultListener implements HandleResultListener {
		@Override
		public void doResult(Object result) {
			if (!isFinishing() && !isFinished()) {
				switch (Integer.parseInt((String) result)) {
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
		}
	}

	class LoginCorrectnessListener implements CorrectnessListener {
		@Override
		public void doResultAfterJudgeInput(int resultCode) {
			switch (resultCode) {
			case ResultConst.LOGIN_EMAIL_NOT_CORRECT:
				Toast.makeText(LoginActivity.this, "邮箱格式错误", Toast.LENGTH_SHORT)
						.show();
				break;
			case ResultConst.LOGIN_USER_NULL:
				Toast.makeText(LoginActivity.this, "用户名不能为空",
						Toast.LENGTH_SHORT).show();
				break;
			case ResultConst.LOGIN_PASSWORD_NULL:
				Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	}
}
