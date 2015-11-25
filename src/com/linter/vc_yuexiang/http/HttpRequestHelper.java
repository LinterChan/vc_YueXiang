package com.linter.vc_yuexiang.http;

import java.util.Map;

import android.os.AsyncTask;

public class HttpRequestHelper {
	private String url = null;
	private Map<String, String> map = null;
	private HandleResultListener doResultListener = null;

	public HttpRequestHelper(String url, Map<String, String> map) {
		this.url = url;
		this.map = map;
	}

	private class HttpRequestTask extends AsyncTask<String, Void, String> {
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
	
	public void setDoResultListener(HandleResultListener doResultListener){
		this.doResultListener = doResultListener;
	}

	public void execute() {
		HttpRequestTask httpRequestTask = new HttpRequestTask();
		httpRequestTask.execute();
	}
	
	public interface HandleResultListener{
		void doResult(Object result);
	}

	
}
