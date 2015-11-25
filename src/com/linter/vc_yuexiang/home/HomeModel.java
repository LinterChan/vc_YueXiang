package com.linter.vc_yuexiang.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.linter.vc_yuexiang.common.SongInfo;
import com.linter.vc_yuexiang.http.HttpClientUtil;
import com.linter.vc_yuexiang.http.HttpRequestHelper;
import com.linter.vc_yuexiang.http.ImageLoader;
import com.linter.vc_yuexiang.http.HttpRequestHelper.HandleResultListener;

public class HomeModel {
	public static void getSongData(HandleResultListener listener) {
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

	public static List<SongInfo> getSongData(String result) {
		List<SongInfo> list = new ArrayList<SongInfo>();
		try {
			JSONObject resultJson = new JSONObject(result);
			JSONArray jsonArray = resultJson.getJSONArray("songData");
			for (int i = 0; i < jsonArray.length(); i++) {
				SongInfo songInfo = new SongInfo();
				JSONObject song = jsonArray.getJSONObject(i);
				songInfo.setSid(song.getString("sid"));
				songInfo.setVol(song.getString("vol"));
				songInfo.setTitle(song.getString("title"));
				songInfo.setContent(song.getString("content"));
				songInfo.setSongName(song.getString("songname"));
				songInfo.setSinger(song.getString("singer"));
				songInfo.setSongUrl(song.getString("songurl"));
				songInfo.setImageUrl(song.getString("imageurl"));
				list.add(songInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
