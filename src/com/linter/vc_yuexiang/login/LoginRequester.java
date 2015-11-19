package com.linter.vc_yuexiang.login;

import java.util.Map;

import android.app.Activity;
import android.widget.Toast;

import com.linter.vc_yuexiang.common.ResultConst;
import com.linter.vc_yuexiang.http.HttpRequestHelper;
import com.linter.vc_yuexiang.http.HttpRequestHelper.DoResultListener;

public class LoginRequester {

	public static void requestToServer(final Activity activity, String url,
			Map<String, String> map) {
		HttpRequestHelper helper = new HttpRequestHelper(url, map);
		helper.setDoResultListener(new DoResultListener() {
			@Override
			public void doResult(String result) {
				switch (Integer.parseInt(result)) {
				case ResultConst.LOGIN_USER_NOT_EXIST:
					Toast.makeText(activity, "用户不存在", Toast.LENGTH_SHORT)
							.show();
					break;
				case ResultConst.LOGIN_PASSWORD_WORRY:
					Toast.makeText(activity, "密码错误", Toast.LENGTH_SHORT).show();
					break;
				case ResultConst.LOGIN_SUCCESS:
					// 跳转到HomeActivity主页

					activity.finish();
					break;
				case ResultConst.LOGIN_FAIL:
					Toast.makeText(activity, "登录失败,请稍后重试", Toast.LENGTH_SHORT)
							.show();
					break;
				}
			}
		});
		helper.execute();
	}
}
