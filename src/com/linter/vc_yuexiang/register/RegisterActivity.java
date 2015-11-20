package com.linter.vc_yuexiang.register;

import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vc_yuexiang.R;
import com.linter.vc_yuexiang.common.BaseActivity;
import com.linter.vc_yuexiang.common.CorrectnessListener;
import com.linter.vc_yuexiang.common.DoResultListener;
import com.linter.vc_yuexiang.common.ResultConst;
import com.linter.vc_yuexiang.http.HttpClientUtil;

/**
 * 注册Activity
 * 
 * @author LinterChen linterchen@vanchu.net
 * @date 2015-11-16
 */
public class RegisterActivity extends BaseActivity {
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

	@Override
	protected void onDestroy() {
		isDestroyed = true;
		super.onDestroy();
	}

	private void initView() {
		usernameEditText = (EditText) findViewById(R.id.register_username_edittext);
		passwordEditText = (EditText) findViewById(R.id.register_password_edittext);
		registerButton = (Button) findViewById(R.id.register_register_button);
	}

	private void setupRegisterButton() {
		registerButton.setOnClickListener(new RegisterButtonListener());
	}

	private class RegisterButtonListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			Map<String, String> map = RequestRegisterDataGetter.getRequestData(
					usernameEditText, passwordEditText,
					new RegisterCorrectnessListener());
			if (map != null) {
				String url = HttpClientUtil.URL_IP + "/RegisterServlet";
				RegisterRequester.requestToServer(url, map,
						new DoRegisterResultListener());
			}
		}
	}

	class DoRegisterResultListener implements DoResultListener {
		@Override
		public void doResult(String result) {
			if (!isFinishing() && !isDestroyed) {
				switch (Integer.parseInt(result)) {
				case ResultConst.REG_EMAIL_NOT_EXIST:
					Toast.makeText(RegisterActivity.this, "该邮箱不存在",
							Toast.LENGTH_SHORT).show();
					break;
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
		}
	}

	class RegisterCorrectnessListener implements CorrectnessListener {
		@Override
		public void doResultAfterJudgeInput(int resultCode) {
			switch (resultCode) {
			case ResultConst.REG_EMAIL_NOT_CORRECT:
				Toast.makeText(RegisterActivity.this, "邮箱格式错误",
						Toast.LENGTH_SHORT).show();
				break;
			case ResultConst.REG_USER_NULL:
				Toast.makeText(RegisterActivity.this, "用户名不能为空",
						Toast.LENGTH_SHORT).show();
				break;
			case ResultConst.REG_PASSWORD_NULL:
				Toast.makeText(RegisterActivity.this, "密码不能为空",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}

}
