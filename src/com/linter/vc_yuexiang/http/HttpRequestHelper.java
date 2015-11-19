package com.linter.vc_yuexiang.http;

import java.util.Map;
import android.os.AsyncTask;

public class HttpRequestHelper {
	String url = null;
	Map<String, String> map = null;

	private DoResultListener doResultListener = null;

	public HttpRequestHelper(String url, Map<String, String> map) {
		this.url = url;
		this.map = map;
	}

	class HttpRequestTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... param) {
			String result = new HttpUtil().connect(url, map);
			return result.trim();
		}

		@Override
		protected void onPostExecute(String result) {
			if (doResultListener != null)
				doResultListener.doResult(result);
		}
	}

	public interface DoResultListener {
		public void doResult(String result);
	}

	public void setDoResultListener(DoResultListener doResultListener) {
		this.doResultListener = doResultListener;
	}

	public void execute() {
		HttpRequestTask httpRequestTask = new HttpRequestTask();
		httpRequestTask.execute();
	}
}
