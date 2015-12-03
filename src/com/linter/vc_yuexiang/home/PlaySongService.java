package com.linter.vc_yuexiang.home;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;

public class PlaySongService extends Service {
	private MediaPlayer player;
	private String songUrl = "";
	private OnStopSongListener listener;

	@Override
	public void onCreate() {
		super.onCreate();
		initPlayer();
		int pid = Process.myPid();
		System.out.println("service pid:" + pid);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return new AidlServiceImpl();
	}

	private void initPlayer() {
		player = new MediaPlayer();
	}

	private class AidlServiceImpl extends AidlService.Stub {
		@Override
		public void setupPlayer(String pSongUrl) throws RemoteException {
			if (player.isPlaying()) {
				player.stop();
			}
			if (!songUrl.equals(pSongUrl)) {
				// 若不是同一首歌/还没设置url
				if (listener != null) {
					listener.onStopSong();
				}
				try {
					player.setDataSource(pSongUrl);
					player.setOnCompletionListener(new CompletionListener());
					player.prepare();
				} catch (Exception e) {
					e.printStackTrace();
				}
				songUrl = pSongUrl;
			}
		}

		@Override
		public void playSong() throws RemoteException {
			player.start();
		}

		@Override
		public void stopSong(String pSongUrl) throws RemoteException {
			if (player.isPlaying() && songUrl.equals(pSongUrl)) {
				player.pause();
			}
		}

		@Override
		public void setOnStopSongListener(OnStopSongListener pListener)
				throws RemoteException {
			if (listener != pListener) {
				listener = pListener;
			}
		}

	}

	@Override
	public void onDestroy() {
		player.release();
		super.onDestroy();
	}

	private class CompletionListener implements OnCompletionListener {
		@Override
		public void onCompletion(MediaPlayer arg0) {
			player.stop();
			// 回调方法：更新界面
			if (listener != null) {
				try {
					listener.onStopSong();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
