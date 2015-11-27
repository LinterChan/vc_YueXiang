package com.linter.vc_yuexiang.home;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.linter.vc_yuexiang.common.SongInfo;
import com.linter.vc_yuexiang.http.HttpClientUtil;
import com.linter.vc_yuexiang.http.HttpRequestHelper;
import com.linter.vc_yuexiang.http.ImageLoader;
import com.linter.vc_yuexiang.http.HttpRequestHelper.HandleResultListener;

public class HomeModel {
	public static void getSongDataFromServer(HandleResultListener listener) {
		String url = HttpClientUtil.URL_IP + "/GetSongDataServlet";
		HttpRequestHelper helper = new HttpRequestHelper(url, null);
		helper.setDoResultListener(listener);
		helper.execute();
	}

	public static void loadImage(String url, HandleResultListener listener) {
		ImageLoader loader = new ImageLoader(url);
		loader.setDoResultListener(listener);
		loader.execute();
	}

	public static void loveSong(String sid, String username,
			HandleResultListener listener) {
		Map<String, String> map = getRequestData(sid, username);
		String url = HttpClientUtil.URL_IP + "/LoveSongServlet";
		HttpRequestHelper helper = new HttpRequestHelper(url, map);
		helper.setDoResultListener(listener);
		helper.execute();
	}

	public static void cancelLoveSong(String sid, String username,
			HandleResultListener listener) {
		Map<String, String> map = getRequestData(sid, username);
		String url = HttpClientUtil.URL_IP + "/CancelLoveSongServlet";
		HttpRequestHelper helper = new HttpRequestHelper(url, map);
		helper.setDoResultListener(listener);
		helper.execute();
	}

	private static Map<String, String> getRequestData(String sid,
			String username) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("sid", sid);
		map.put("username", username);
		return map;
	}

	public static SongInfo getSongData(String result) {
		SongInfo songInfo = null;
		try {
			JSONObject json = new JSONObject(result);
			songInfo = new SongInfo();
			songInfo.setSid(json.getString("sid"));
			songInfo.setVol(json.getString("vol"));
			songInfo.setWriter(json.getString("writer"));
			songInfo.setTitle(json.getString("title"));
			songInfo.setContent(json.getString("content"));
			songInfo.setSongName(json.getString("songname"));
			songInfo.setSinger(json.getString("singer"));
			songInfo.setSongUrl(json.getString("songurl"));
			songInfo.setImageUrl(json.getString("imageurl"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return songInfo;
	}
}
