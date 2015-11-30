package com.linter.vc_yuexiang.home;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;

public class HomePlaySongService extends Service {
	private MediaPlayer player;
	private String songUrl;
	private int currentPosition;
	private String status;
	private boolean isSongUrlSeted = false;

	@Override
	public void onCreate() {
		super.onCreate();
		initPlayer();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		songUrl = intent.getStringExtra("songurl");
		int position = intent.getIntExtra("position", 0);
		status = intent.getStringExtra("operation");
		if (currentPosition == position) {// 同一个fragment
			if (status.equals("start")) {
				if (!isSongUrlSeted) {
					try {
						player.setDataSource(songUrl);
						player.setOnCompletionListener(new CompletionListener());
						player.prepare();
					} catch (Exception e) {
						e.printStackTrace();
					}
					isSongUrlSeted = true;
				}
				player.start();
			} else {
				player.pause();
			}
		} else {// 不同fragment
			if (status.equals("start")) {
				player.stop();
				try {
					player.setDataSource(songUrl);
					player.setOnCompletionListener(new CompletionListener());
				} catch (Exception e) {
					e.printStackTrace();
				}
				//停止之前正在播放的音乐
				sendCast();
				player.start();
				currentPosition = position;
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	private void initPlayer() {
		player = new MediaPlayer();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		player.release();
		super.onDestroy();
	}

	private void sendCast(){
		Intent receiverIntent = new Intent(
				"android.intent.action.STOPPLAYSONG_BROADCAST");
		receiverIntent.putExtra("i", currentPosition);
		sendBroadcast(receiverIntent);
	}

	private class CompletionListener implements OnCompletionListener{
		@Override
		public void onCompletion(MediaPlayer arg0) {
			//停止以播完的音乐
			sendCast();
		}
	}
}
