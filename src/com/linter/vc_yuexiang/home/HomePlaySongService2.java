package com.linter.vc_yuexiang.home;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;

public class HomePlaySongService2 extends Service {
	private MediaPlayer player;
	private String songUrl = "";
	// private boolean isSongUrlSeted = false;
	private final IBinder binder = new LocalBinder();
	private UpdateUIAfterStopSongListener listener;

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
				listener.updateUI();
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
		public HomePlaySongService2 getService() {
			return HomePlaySongService2.this;
		}
	}

	private class CompletionListener implements OnCompletionListener {
		@Override
		public void onCompletion(MediaPlayer arg0) {
			// 停止已播完的音乐
			player.stop();
			// 回调方法：更新界面
			if (listener != null) {
				listener.updateUI();
			}
		}
	}

	public interface UpdateUIAfterStopSongListener {
		void updateUI();
	}

	public void setUpdateUIAfterStopSongListener(
			UpdateUIAfterStopSongListener listener) {
		if (this.listener != listener) {
			this.listener = listener;
		}
	}
}
