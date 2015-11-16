package com.example.vc_yuexiang.register;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vc_yuexiang.R;
import com.example.vc_yuexiang.http.HttpUtil;
import com.example.vc_yuexiang.util.TypeUtil;

public class RegisterActivity extends Activity {
	private EditText et_username;
	private EditText et_pswd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		initView();
	}

	public void initView() {
		et_username = (EditText) findViewById(R.id.register_et_username);
		et_pswd = (EditText) findViewById(R.id.register_et_pswd);
	}

	/**
	 * ע�ᰴť�ĵ���¼�
	 * 
	 * @param view
	 */
	public void onClick_registerButton(View view) {
		String username = et_username.getText().toString().trim();
		String pswd = et_pswd.getText().toString().trim();
		if (isCorrectOfInput(username, pswd)) {
			RegisterTask registerTask = new RegisterTask();
			registerTask.execute(username, pswd);
		}
	}

	/**
	 * �ж�EditText�����ʽ�Ƿ���ȷ
	 * 
	 * @return boolean
	 */
	public boolean isCorrectOfInput(String username, String pswd) {
		if (username.equals("")) {
			Toast.makeText(RegisterActivity.this, "�û�������Ϊ��", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (pswd.equals("")) {
			Toast.makeText(RegisterActivity.this, "���벻��Ϊ��", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	/**
	 * ִ�е�¼�������첽������
	 * 
	 * @author vanchu
	 */
	class RegisterTask extends AsyncTask<String, Void, Integer> {
		@Override
		protected Integer doInBackground(String... param) {
			// �����¼������url
			String url = HttpUtil.URL_IP + "/RegisterServlet";
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
			nameValuePair.add(new BasicNameValuePair("username", param[0]));
			nameValuePair.add(new BasicNameValuePair("pswd", param[1]));
			String result = new HttpUtil().Connect(url, nameValuePair);
			return Integer.parseInt(result.trim());
		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			case TypeUtil.REG_USEREXIST:
				// ���û��Ѵ���
				Toast.makeText(RegisterActivity.this, "���û��Ѵ���",
						Toast.LENGTH_SHORT).show();
				break;
			case TypeUtil.REG_SUCCESS:
				// ע��ɹ�
				// ��ת��LoginActivity
				break;
			case TypeUtil.REG_FAIL:
				// ע��ʧ��
				Toast.makeText(RegisterActivity.this, "ע��ʧ��,���Ժ�����",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}

}
