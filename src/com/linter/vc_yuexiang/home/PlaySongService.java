package com.linter.vc_yuexiang.home;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;

public class PlaySongService extends Service {
	private MediaPlayer player;
	private String songUrl = "";
	private final IBinder binder = new LocalBinder();
	private OnStopSongListener listener;

	@Override
	public void onCreate() {
		super.onCreate();
		initPlayer();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	private void initPlayer() {
		player = new MediaPlayer();
	}

	public void setupPlayer(String songUrl) {
		if (player.isPlaying()) {
			player.stop();
		}
		if (!this.songUrl.equals(songUrl)) {
			// 若不是同一首歌/还没设置url
			if (listener != null) {
				listener.onStopSong();
			}
			try {
				player.setDataSource(songUrl);
				player.setOnCompletionListener(new CompletionListener());
				player.prepare();
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.songUrl = songUrl;
		}
	}

	public void playSong() {
		player.start();
	}

	public void stopSong(String songUrl) {
		if (player.isPlaying() && this.songUrl.equals(songUrl)) {
			player.pause();
		}
	}

	@Override
	public void onDestroy() {
		player.release();
		super.onDestroy();
	}

	public class LocalBinder extends Binder {
		public PlaySongService getService() {
			return PlaySongService.this;
		}
	}

	private class CompletionListener implements OnCompletionListener {
		@Override
		public void onCompletion(MediaPlayer arg0) {
			player.stop();
			// 回调方法：更新界面
			if (listener != null) {
				listener.onStopSong();
			}
		}
	}

	public interface OnStopSongListener {
		void onStopSong();
	}

	public void setOnStopSongListener(OnStopSongListener listener) {
		if (this.listener != listener) {
			this.listener = listener;
		}
	}
}
