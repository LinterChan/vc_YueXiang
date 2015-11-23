package com.linter.vc_yuexiang.home;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.linter.vc_yuexiang.common.SongInfo;

public class JsonTool {
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
