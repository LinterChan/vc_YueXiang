package com.linter.vc_yuexiang.http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.os.AsyncTask;

public class HttpRequestHelper {
	String url = null;
	Map<String, String> map = null;
	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	
	private DoResultListener doResultListener = null;

	public HttpRequestHelper(String url, Map<String, String> map) {
		this.url = url;
		this.map = map;
		initData();
	}

	private void initData() {
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) it.next();
			nameValuePair.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
	}

	class HttpRequestTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... param) {
			String result = new HttpUtil().Connect(url, nameValuePair);
			return result.trim();
		}

		@Override
		protected void onPostExecute(String result) {
			doResultListener.doResult(result);
		}
	}
	
	public interface DoResultListener{
		public void doResult(String result);
	}
	public void setDoResultListener(DoResultListener doResultListener){
		this.doResultListener = doResultListener;
	}
	public void execute(){
		HttpRequestTask httpRequestTask = new HttpRequestTask();
		httpRequestTask.execute();
	}
}
