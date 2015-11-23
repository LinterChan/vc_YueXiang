package com.linter.vc_yuexiang.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 网络请求工具类
 * 
 * @author LinterChen linterchen@vanchu.net
 * @date 2015-11-19
 */
public class HttpUtil {
	public static final String URL_IP = "http://192.168.1.2:8080/YueXiang";
	private String encoding = "UTF-8";
	private int connectTime = 5 * 1000;
	private int readTime = 5 * 1000;
	private StringBuilder requestData = new StringBuilder();
	private HttpURLConnection conn = null;
	private PrintWriter writer = null;
	private BufferedReader reader = null;
	private StringBuilder result = null;

	public String connect(String str_url, Map<String, String> map) {
		initData(map);
		try {
			URL url = new URL(str_url);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setChunkedStreamingMode(0);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(connectTime);
			conn.setReadTimeout(readTime);
			conn.setRequestProperty("Content-type",
					"application/x-java-serialized-object");
			conn.setRequestProperty("Charset", encoding);

			writer = new PrintWriter(conn.getOutputStream());
			writer.write(requestData.toString());
			writer.flush();
			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				reader = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					result.append(line + "\n");
				}
			} else {
				// 连接失败,网络异常
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.disconnect();
				}
				if (writer != null) {
					writer.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}

	private void initData(Map<String, String> map) {
		if (map == null) {
			return;
		}
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			requestData.append(entry.getKey() + "=");
			requestData.append(entry.getValue() + "&");
		}
		requestData.deleteCharAt(requestData.length() - 1);
	}
}
