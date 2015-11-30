package com.linter.vc_yuexiang.common;

public class SongInfo {
	private String sid;
	private String vol;
	private String writer;
	private String title;
	private String content;
	private String songName;
	private String singer;
	private String songUrl;
	private String imageUrl;

	public SongInfo(String sid, String vol, String writer, String title,
			String content, String songName, String singer, String songUrl,
			String imageUrl) {
		super();
		this.sid = sid;
		this.vol = vol;
		this.writer = writer;
		this.title = title;
		this.content = content;
		this.songName = songName;
		this.singer = singer;
		this.songUrl = songUrl;
		this.imageUrl = imageUrl;
	}

	public String getSid() {
		return sid;
	}

	public String getVol() {
		return vol;
	}

	public String getWriter() {
		return writer;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public String getSongName() {
		return songName;
	}

	public String getSinger() {
		return singer;
	}

	public String getSongUrl() {
		return songUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

}
