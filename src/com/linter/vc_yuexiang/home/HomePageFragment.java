package com.linter.vc_yuexiang.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vc_yuexiang.R;
import com.linter.vc_yuexiang.common.SharedPreferenceUtil;
import com.linter.vc_yuexiang.common.SongInfo;
import com.linter.vc_yuexiang.http.HttpRequestHelper.HandleResultListener;
import com.linter.vc_yuexiang.network.NetworkConnDetector;

public class HomePageFragment extends Fragment {
	private int i;
	private HomeActivity activity;
	StopPlaySongReceiver receiver;

	private SongInfo songInfo;
	private ImageView backgroundImageView;
	private TextView vol_writerTextView;
	private TextView titleTextView;
	private TextView contentTextView;
	private SeekBar songSeekBar;
	private ImageView playSongButton;
	private TextView songNameTextView;
	private ImageView shareSongButton;
	private ImageView loveSongButton;

	private MediaPlayer mPlayer;
	private boolean isPlayingSong = false;
	private Handler seekBarHandler;
	private int mProcess = 0;// 消息队列中的进程号
	private int songProgress = 0;// 播放进度

	private boolean isLoved = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_homepage, container,
				false);
		setParentActivity((HomeActivity) getActivity());
		initView(view);

		registerReceiver();
		getSongData();
//		setupSongList(); 测试用
		setupPlaySongButton();
		setupLoveSongButton();
		setupShareSongButton();
		setupSongSeekBar();

		return view;
	}
	
	@Override
	public void onStop() {
		stopPlaySong();
		super.onStop();
	}
	private void registerReceiver() {
		receiver = new StopPlaySongReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.STOPPLAYSONG_BROADCAST");
		activity.registerReceiver(receiver, filter);
	}

	private void sendBroadcast() {
		Intent intent = new Intent(
				"android.intent.action.STOPPLAYSONG_BROADCAST");
		intent.putExtra("i", i);
		activity.sendBroadcast(intent);
	}

	@Override
	public void onDestroy() {
		activity.unregisterReceiver(receiver);
		mPlayer.release();
		super.onDestroy();
	}

	public void setPosition(int i) {
		this.i = i;
	}

	private void setParentActivity(HomeActivity activity) {
		this.activity = activity;
	}

	private void initView(View view) {
		backgroundImageView = (ImageView) view
				.findViewById(R.id.homepage_background_imageview);
		vol_writerTextView = (TextView) view
				.findViewById(R.id.homepage_vol_writer_textview);
		titleTextView = (TextView) view
				.findViewById(R.id.homepage_title_textview);
		contentTextView = (TextView) view
				.findViewById(R.id.homepage_content_textview);
		songSeekBar = (SeekBar) view.findViewById(R.id.homepage_song_seekbar);
		playSongButton = (ImageView) view
				.findViewById(R.id.homepage_playsong_button);
		songNameTextView = (TextView) view
				.findViewById(R.id.homepage_songname_textview);
		shareSongButton = (ImageView) view
				.findViewById(R.id.homepage_share_imageview);
		loveSongButton = (ImageView) view
				.findViewById(R.id.homepage_love_imageview);
		mPlayer = new MediaPlayer();
	}

	private void getSongData() {
		if (NetworkConnDetector.isNetworkConnected(activity)) {
			HomeModel.getSongDataFromServer(new GetSongDataResultListener());
		} else {
			// 加载本地数据 Or 显示“重新加载”,若重新加载则隐藏所有控件

			Toast.makeText(getActivity(), "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}

	private class GetSongDataResultListener implements HandleResultListener {
		@Override
		public void doResult(Object result) {
			if (!getActivity().isFinishing() && !activity.isFinished()) {
				songInfo = HomeModel.getSongData((String) result);
				setupSongList();
			}
		}
	}

	private void setupSongList() {
		vol_writerTextView.setText(songInfo.getVol() + " "
				+ songInfo.getWriter());
		titleTextView.setText(songInfo.getTitle());
		contentTextView.setText(songInfo.getContent());
		songNameTextView.setText(songInfo.getSongName());
		try {
			mPlayer.setDataSource(songInfo.getSongUrl());
			mPlayer.setOnCompletionListener(new SongCompletionListener());
			mPlayer.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
		HomeModel
				.loadImage(songInfo.getImageUrl(), new SetBackgroundListener());

//		测试用
//		try {
//			mPlayer = MediaPlayer.create(activity, R.raw.f);
//			mPlayer.setOnCompletionListener(new SongCompletionListener());
//			mPlayer.prepare();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		songSeekBar.setMax(mPlayer.getDuration() / 1000);
		seekBarHandler = new SeekBarHandler();
	}

	private void setupSongSeekBar() {
		songSeekBar.setEnabled(false);
	}

	private class SongCompletionListener implements OnCompletionListener {
		@Override
		public void onCompletion(MediaPlayer arg0) {
			playSongButton.setImageResource(R.drawable.stop_button_not_press);
			seekBarHandler.removeMessages(mProcess);
			mPlayer.stop();
			isPlayingSong = false;
			songSeekBar.setProgress(0);
		}
	}

	private class SetBackgroundListener implements HandleResultListener {
		@Override
		public void doResult(Object result) {
			if (!getActivity().isFinishing() && !activity.isFinished()) {
				Bitmap bitmap = (Bitmap) result;
				backgroundImageView.setImageBitmap(bitmap);
			}
		}
	}

	private void setupPlaySongButton() {
		playSongButton.setOnTouchListener(new PlaySongButtonListener());
	}

	private void setupLoveSongButton() {
		loveSongButton.setOnClickListener(new LoveSongButtonListener());
	}

	private void setupShareSongButton() {
		shareSongButton.setOnClickListener(new ShareSongButtonListener());
	}

	private class PlaySongButtonListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (isPlayingSong) {
					playSongButton
							.setImageResource(R.drawable.start_button_press);
				} else {
					if (NetworkConnDetector.isNetworkConnected(activity)) {
						playSongButton
								.setImageResource(R.drawable.stop_button_press);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				if (isPlayingSong) {
					playSongButton
							.setImageResource(R.drawable.stop_button_not_press);
					isPlayingSong = false;
					mPlayer.pause();
					seekBarHandler.removeMessages(mProcess);
				} else {
					if (NetworkConnDetector.isNetworkConnected(activity)) {
						sendBroadcast();
						playSongButton
								.setImageResource(R.drawable.start_button_not_press);
						isPlayingSong = true;
						mPlayer.start();
						seekBarHandler.sendEmptyMessage(mProcess);
					} else {
						Toast.makeText(activity, "网络未连接", Toast.LENGTH_SHORT)
								.show();
					}
				}
				break;
			}
			return false;
		}
	}

	private class LoveSongButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (NetworkConnDetector.isNetworkConnected(activity)) {
				if (SharedPreferenceUtil.getLoginFlag(activity)) {
					if (isLoved) {
						HomeModel.cancelLoveSong(songInfo.getSid(),
								SharedPreferenceUtil.getLoginInfo(activity),
								new CancelLoveSongListener());
					} else {
						HomeModel.loveSong(songInfo.getSid(),
								SharedPreferenceUtil.getLoginInfo(activity),
								new LoveSongListener());
					}
				} else {
					Toast.makeText(activity, "请先登录", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(activity, "网络未连接", Toast.LENGTH_SHORT).show();
			}

		}
	}

	private class ShareSongButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Toast.makeText(activity, "该功能尚未开发", Toast.LENGTH_SHORT).show();
		}
	}

	private class LoveSongListener implements HandleResultListener {
		@Override
		public void doResult(Object result) {
			if (!activity.isFinishing() && !activity.isFinished()) {
				if (Boolean.parseBoolean((String) result)) {
					loveSongButton.setImageResource(R.drawable.home_icon_love2);
					isLoved = true;
					Toast.makeText(activity, "收藏成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(activity, "收藏失败", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private class CancelLoveSongListener implements HandleResultListener {
		@Override
		public void doResult(Object result) {
			if (!activity.isFinishing() && !activity.isFinished()) {
				if (Boolean.parseBoolean((String) result)) {
					loveSongButton.setImageResource(R.drawable.home_icon_love1);
					isLoved = false;
					Toast.makeText(activity, "取消收藏成功", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(activity, "取消收藏失败", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}

	public void stopPlaySong() {
		System.out.println("stopPlaySong");
		playSongButton.setImageResource(R.drawable.stop_button_not_press);
		isPlayingSong = false;
		mPlayer.pause();
		seekBarHandler.removeMessages(mProcess);
	}

	private class SeekBarHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == mProcess && songProgress < songSeekBar.getMax()) {
				songProgress += 1;
				songSeekBar.incrementProgressBy(1);
				seekBarHandler.sendEmptyMessageDelayed(mProcess, 1000);
			}
		}
	}

	private class StopPlaySongReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Integer t = intent.getIntExtra("i", 0);
			if (i != t && mPlayer.isPlaying())
				stopPlaySong();
		}
	}
}
