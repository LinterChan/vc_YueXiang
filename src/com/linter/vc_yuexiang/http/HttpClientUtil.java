package com.linter.vc_yuexiang.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * 网络请求的工具类
 * 
 * @author LinterChen linterchen@vanchu.net
 * @date 2015-11-16
 */
public class HttpClientUtil {
	public static final String URL_IP = "http://192.168.1.2:8080/YueXiang";
	private List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	private InputStream inputStream;
	private BufferedReader reader;
	private StringBuilder result = new StringBuilder();

	public String connect(String url, Map<String, String> map) {
		initData(map);
		HttpPost httpPost = new HttpPost(url);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity httpEntity = response.getEntity();
			inputStream = httpEntity.getContent();
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			while ((line = reader.readLine()) != null) {
				result.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}

	private void initData(Map<String, String> map) {
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) it.next();
			nameValuePair.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
	}
}