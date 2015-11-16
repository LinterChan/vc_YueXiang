package com.example.vc_yuexiang.register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vc_yuexiang.R;
import com.example.vc_yuexiang.http.HttpRequestHelper;
import com.example.vc_yuexiang.http.HttpRequestHelper.DoResultListener;
import com.example.vc_yuexiang.http.HttpUtil;
import com.example.vc_yuexiang.util.TypeUtil;

/**
 * 注册Activity
 * 
 * @author LinterChen linterchen@vanchu.net
 * @date 2015-11-16
 */
public class RegisterActivity extends Activity {
	private EditText usernameEditText;
	private EditText passwordEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		initView();
	}

	public void initView() {
		usernameEditText = (EditText) findViewById(R.id.register_username_edittext);
		passwordEditText = (EditText) findViewById(R.id.register_password_edittext);
		usernameEditText.clearFocus();
	}

	public void onClick_registerButton(View view) {
		String username = usernameEditText.getText().toString().trim();
		String password = passwordEditText.getText().toString().trim();
		if (isCorrectOfInput(username, password)) {
			String url = HttpUtil.URL_IP + "/RegisterServlet";
			Map<String, String> map = new HashMap<String, String>();
			map.put("username", username);
			map.put("password", password);
			HttpRequestHelper helper = new HttpRequestHelper(url, map);
			helper.setDoResultListener(new DoResultListener() {
				@Override
				public void doResult(String result) {
					switch (Integer.parseInt(result)) {
					case TypeUtil.REG_USER_EXIST:
						// 该用户已存在
						Toast.makeText(RegisterActivity.this, "该用户已存在",
								Toast.LENGTH_SHORT).show();
						break;
					case TypeUtil.REG_SUCCESS:
						// 注册成功
						// 跳转到LoginActivity
						break;
					case TypeUtil.REG_FAIL:
						// 注册失败
						Toast.makeText(RegisterActivity.this, "注册失败,请稍后重试",
								Toast.LENGTH_SHORT).show();
						break;
					}
				}
			});
//			RegisterTask registerTask = new RegisterTask();
//			registerTask.execute(username, password);
		}
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

	/**
	 * 执行注册的异步任务类
	 * 
	 * @author LinterChen linterchen@vanchu.net
	 * @date 2015-11-16
	 */
//	class RegisterTask extends AsyncTask<String, Void, Integer> {
//		@Override
//		protected Integer doInBackground(String... param) {
//			// 执行注册操作的url
//			String url = HttpUtil.URL_IP + "/RegisterServlet";
//			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
//			nameValuePair.add(new BasicNameValuePair("username", param[0]));
//			nameValuePair.add(new BasicNameValuePair("password", param[1]));
//			String result = new HttpUtil().Connect(url, nameValuePair);
//			return Integer.parseInt(result.trim());
//		}
//
//		@Override
//		protected void onPostExecute(Integer result) {
//			switch (result) {
//			case TypeUtil.REG_USER_EXIST:
//				// 该用户已存在
//				Toast.makeText(RegisterActivity.this, "该用户已存在",
//						Toast.LENGTH_SHORT).show();
//				break;
//			case TypeUtil.REG_SUCCESS:
//				// 注册成功
//				// 跳转到LoginActivity
//				break;
//			case TypeUtil.REG_FAIL:
//				// 注册失败
//				Toast.makeText(RegisterActivity.this, "注册失败,请稍后重试",
//						Toast.LENGTH_SHORT).show();
//				break;
//			}
//		}
//	}

}
