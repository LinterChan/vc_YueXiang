package com.linter.vc_yuexiang.home;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vc_yuexiang.R;
import com.linter.vc_yuexiang.common.DoResultListener;
import com.linter.vc_yuexiang.common.SongInfo;
import com.linter.vc_yuexiang.network.NetworkConnDetector;

public class HomeActivity extends Activity {
	private static final int PAGE_NUM = 3;
	private ViewPager mViewPager;
	private View[] view;
	private List<View> mViewPagerData;
	private PagerAdapter mViewPagerAdapter;

	private TextView[] volTextView = new TextView[PAGE_NUM];
	private TextView[] titleTextView = new TextView[PAGE_NUM];
	private TextView[] contentTextView = new TextView[PAGE_NUM];
	private SeekBar[] songSeekBar = new SeekBar[PAGE_NUM];
	private ImageButton[] playSongButton = new ImageButton[PAGE_NUM];
	private TextView[] songNameTextView = new TextView[PAGE_NUM];

	private MediaPlayer[] mediaPlayer = new MediaPlayer[PAGE_NUM];

	private boolean[] isPlaySong = new boolean[3];
	
	private int[] msgProcess = new int[PAGE_NUM];
	private int[] songProgress = new int[PAGE_NUM];
	private Handler seekBarHandler = new SeekBarHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		initViewPagerData();
		getSongData();

		initView();
		setupViewPager();
		setupPlaySongButton();
		setupSongSeekBar();
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.home_homepage_viewpager);

		for (int i = 0; i < PAGE_NUM; i++) {
			volTextView[i] = (TextView) view[i]
					.findViewById(R.id.homepage_vol_textview);
			titleTextView[i] = (TextView) view[i]
					.findViewById(R.id.homepage_title_textview);
			contentTextView[i] = (TextView) view[i]
					.findViewById(R.id.homepage_content_textview);
			songSeekBar[i] = (SeekBar) view[i]
					.findViewById(R.id.homepage_song_seekbar);
			playSongButton[i] = (ImageButton) view[i]
					.findViewById(R.id.homepage_playsong_button);
			songNameTextView[i] = (TextView) view[i]
					.findViewById(R.id.homepage_songname_textview);

			mediaPlayer[i] = new MediaPlayer();

			isPlaySong[i] = false;
		}
	}

	private void initViewPagerData() {
		mViewPagerData = new ArrayList<View>();
		LayoutInflater inflater = LayoutInflater.from(this);
		view = new View[PAGE_NUM];
		for (int i = 0; i < PAGE_NUM; i++) {
			view[i] = inflater.inflate(R.layout.viewpager_homepage, null);
		}

		for (int i = 0; i < view.length; i++) {
			mViewPagerData.add(view[i]);
		}
	}

	private void setupViewPager() {
		mViewPagerAdapter = new MyViewPagerAdapter();
		mViewPager.setAdapter(mViewPagerAdapter);
	}

	private class MyViewPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return mViewPagerData.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(mViewPagerData.get(position));
			return mViewPagerData.get(position);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(mViewPagerData.get(position));
		}
	}

	private class GetSongDataListener implements DoResultListener {
		@Override
		public void doResult(String result) {
			List<SongInfo> songData = JsonTool.getSongData(result);
			setupSongList(songData);
		}
	}

	private void getSongData() {
		if (NetworkConnDetector.isNetworkConnected(getApplicationContext())) {
			HomeRequester.getSongData(new GetSongDataListener());
		} else {
			// 加载本地数据

			Toast.makeText(HomeActivity.this, "网络未连接", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void setupSongList(List<SongInfo> songData) {
		for (int i = 0; i < PAGE_NUM; i++) {
			SongInfo songInfo = songData.get(i);
			volTextView[i].setText(songInfo.getVol());
			titleTextView[i].setText(songInfo.getTitle());
			contentTextView[i].setText(songInfo.getContent());
			songNameTextView[i].setText(songInfo.getSongName() + "——"
					+ songInfo.getSinger());

			try {
				mediaPlayer[i].setDataSource(songInfo.getSongUrl());
				mediaPlayer[i].setOnCompletionListener(new SongCompletionListener(i));
				mediaPlayer[i].prepare();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 加载图片
		}
	}
	
	private class SongCompletionListener implements OnCompletionListener{
		private int i;
		public SongCompletionListener(int i){
			this.i = i;
		}
		@Override
		public void onCompletion(MediaPlayer player) {
			playSongButton[i].setBackgroundResource(R.drawable.stop_button_not_press);
			isPlaySong[i] = false;
			songSeekBar[i].setProgress(0);
			seekBarHandler.removeMessages(songProgress[i]);
		}
	}

	private void setupPlaySongButton() {
		for (int i = 0; i < PAGE_NUM; i++) {
			playSongButton[i].setOnTouchListener(new PlaySongButtonListener(i));
		}
	}

	private class PlaySongButtonListener implements OnTouchListener {
		private int i;

		public PlaySongButtonListener(int i) {
			this.i = i;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (isPlaySong[i]) {
					playSongButton[i]
							.setBackgroundResource(R.drawable.start_button_press);
				} else {
					if (NetworkConnDetector
							.isNetworkConnected(getApplicationContext())) {
						playSongButton[i]
								.setBackgroundResource(R.drawable.stop_button_press);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				if (isPlaySong[i]) {
					playSongButton[i]
							.setBackgroundResource(R.drawable.stop_button_not_press);
					seekBarHandler.removeMessages(songProgress[i]);
					isPlaySong[i] = false;
					mediaPlayer[i].pause();
				} else {
					if (NetworkConnDetector
							.isNetworkConnected(getApplicationContext())) {
						playSongButton[i]
								.setBackgroundResource(R.drawable.start_button_not_press);
						for(int k=0;k<PAGE_NUM&&k!=i;k++){
							if(mediaPlayer[k].isPlaying()){
								playSongButton[k].setBackgroundResource(R.drawable.stop_button_not_press);
								isPlaySong[k] = false;
								mediaPlayer[k].pause();
								seekBarHandler.removeMessages(songProgress[k]);
							}
						}
						isPlaySong[i] = true;
						mediaPlayer[i].start();
						seekBarHandler.sendEmptyMessage(songProgress[i]);
					} else {
						Toast.makeText(HomeActivity.this, "网络未连接",
								Toast.LENGTH_SHORT).show();
					}
				}
				break;
			}
			return false;
		}
	}
	private void setupSongSeekBar(){
		for(int i=0;i<PAGE_NUM;i++){
			songSeekBar[i].setEnabled(false);
			songSeekBar[i].setMax(mediaPlayer[i].getDuration());
		}
	}
	private class SeekBarHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			for(int i=0;i<PAGE_NUM;i++){
				if(msg.what == msgProcess[i] && songProgress[i]<songSeekBar[i].getMax()){
					songProgress[i] += 1;
					songSeekBar[i].incrementProgressBy(1);
					seekBarHandler.sendEmptyMessageDelayed(songProgress[i], 1000);
				}
			}
		}
	}

}
