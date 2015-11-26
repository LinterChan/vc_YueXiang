package com.linter.vc_yuexiang.home;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vc_yuexiang.R;
import com.linter.vc_yuexiang.common.BaseActivity;
import com.linter.vc_yuexiang.common.SharedPreferenceUtil;
import com.linter.vc_yuexiang.common.SongInfo;
import com.linter.vc_yuexiang.http.HttpRequestHelper.HandleResultListener;
import com.linter.vc_yuexiang.network.NetworkConnDetector;

public class HomeActivity extends FragmentActivity {
	private static final int PAGE_NUM = 3;
	private boolean isFinished = false;
	private ViewPager mViewPager;
	private List<HomePageFragment> mFragments;
	private PagerAdapter mAdapter;
	
//	private View[] view;
//	private List<View> mViewPagerData;
//	private PagerAdapter mViewPagerAdapter;

//	private List<SongInfo> songData = null;
//	private ImageView[] backgroundImageView = new ImageView[PAGE_NUM];
//	private TextView[] volTextView = new TextView[PAGE_NUM];
//	private TextView[] titleTextView = new TextView[PAGE_NUM];
//	private TextView[] contentTextView = new TextView[PAGE_NUM];
//	private SeekBar[] songSeekBar = new SeekBar[PAGE_NUM];
//	private ImageView[] playSongButton = new ImageView[PAGE_NUM];
//	private TextView[] songNameTextView = new TextView[PAGE_NUM];
//	private ImageView[] shareButton = new ImageView[PAGE_NUM];
//	private ImageView[] loveButton = new ImageView[PAGE_NUM];

//	private MediaPlayer[] mediaPlayer = new MediaPlayer[PAGE_NUM];

//	private boolean[] isPlaySong = new boolean[PAGE_NUM];
//	private boolean[] isLoved = new boolean[PAGE_NUM];

//	private int[] msgProcess = new int[PAGE_NUM];
//	private int[] songProgress = new int[PAGE_NUM];
//	private Handler seekBarHandler = new SeekBarHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		initFragments();
		initView();
		
		setupViewPager();
//		initViewPagerData();
//		initView();
//		getSongData();
//
//		setupViewPager();
//		setupPlaySongButton();
//		setupLoveButton();
//		setupShareButton();
//		setupSongSeekBar();
	}

	@Override
	protected void onPause() {
//		for (int i = 0; i < PAGE_NUM; i++) {
//			if (mediaPlayer[i].isPlaying()) {
//				mediaPlayer[i].pause();
//				isPlaySong[i] = false;
//				seekBarHandler.removeMessages(songProgress[i]);
//				playSongButton[i]
//						.setBackgroundResource(R.drawable.stop_button_not_press);
//				break;
//			}
//		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		isFinished = true;
//		for (int i = 0; i < PAGE_NUM; i++) {
//			mediaPlayer[i].release();
//		}
//		super.onDestroy();
	}
	
	private boolean isFinished(){
		return isFinished;
	}

	private void initFragments(){
		mFragments = new ArrayList<HomePageFragment>();
		for(int i=0;i<PAGE_NUM;i++){
			mFragments.add(new HomePageFragment());
		}
	}
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.home_homepage_viewpager);

//		for (int i = 0; i < PAGE_NUM; i++) {
//			backgroundImageView[i] = (ImageView) view[i]
//					.findViewById(R.id.homepage_background_imageview);
//			volTextView[i] = (TextView) view[i]
//					.findViewById(R.id.homepage_vol_writer_textview);
//			titleTextView[i] = (TextView) view[i]
//					.findViewById(R.id.homepage_title_textview);
//			contentTextView[i] = (TextView) view[i]
//					.findViewById(R.id.homepage_content_textview);
//			songSeekBar[i] = (SeekBar) view[i]
//					.findViewById(R.id.homepage_song_seekbar);
//			playSongButton[i] = (ImageView) view[i]
//					.findViewById(R.id.homepage_playsong_button);
//			songNameTextView[i] = (TextView) view[i]
//					.findViewById(R.id.homepage_songname_textview);
//			shareButton[i] = (ImageView) view[i]
//					.findViewById(R.id.homepage_share_imageview);
//			loveButton[i] = (ImageView) view[i]
//					.findViewById(R.id.homepage_love_imageview);
//
//			mediaPlayer[i] = new MediaPlayer();
//
//			isPlaySong[i] = false;
//			isLoved[i] = false;
//		}
	}

//	private void initViewPagerData() {
//		mViewPagerData = new ArrayList<View>();
//		LayoutInflater inflater = LayoutInflater.from(this);
//		view = new View[PAGE_NUM];
//		for (int i = 0; i < PAGE_NUM; i++) {
//			view[i] = inflater.inflate(R.layout.fragment_homepage, null);
//			mViewPagerData.add(view[i]);
//		}
//	}

	private void setupViewPager() {
		mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
		mViewPager.setAdapter(mAdapter);
	}

	private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		private List<HomePageFragment> fragments;
		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		public MyFragmentPagerAdapter(FragmentManager fm,List<HomePageFragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragments.get(arg0);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
		
	}

	private class GetSongDataListener implements HandleResultListener {
		@Override
		public void doResult(Object result) {
			if (!isFinishing() && !isFinished()) {
				songData = HomeModel.getSongData((String) result);
				setupSongList();
			}
		}
	}

	private void getSongData() {
		if (NetworkConnDetector.isNetworkConnected(getApplicationContext())) {
			HomeModel.getSongData(new GetSongDataListener());
		} else {
			// 加载本地数据 Or 显示“重新加载”,若重新加载则隐藏所有控件

			Toast.makeText(HomeActivity.this, "网络未连接", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void setupSongList() {
		for (int i = 0; i < PAGE_NUM; i++) {
			SongInfo songInfo = songData.get(i);
			volTextView[i].setText(songInfo.getVol());
			titleTextView[i].setText(songInfo.getTitle());
			contentTextView[i].setText(songInfo.getContent());
			songNameTextView[i].setText(songInfo.getSongName() + "——"
					+ songInfo.getSinger());

			try {
				mediaPlayer[i].setDataSource(songInfo.getSongUrl());
				mediaPlayer[i]
						.setOnCompletionListener(new SongCompletionListener(i));
				mediaPlayer[i].prepare();
			} catch (Exception e) {
				e.printStackTrace();
			}

			HomeModel
					.loadImage(songInfo.getImageUrl(), new SetImageListener(i));

			songSeekBar[i].setMax(mediaPlayer[i].getDuration());
		}
	}

	private class SetImageListener implements HandleResultListener {
		private int i;

		public SetImageListener(int i) {
			this.i = i;
		}

		@Override
		public void doResult(Object result) {
			if (!isFinishing() && !isFinished()) {
				Bitmap bitmap = (Bitmap) result;
				backgroundImageView[i].setImageBitmap(bitmap);
			}
		}
	}

	private class SongCompletionListener implements OnCompletionListener {
		private int i;

		public SongCompletionListener(int i) {
			this.i = i;
		}

		@Override
		public void onCompletion(MediaPlayer player) {
			playSongButton[i]
					.setBackgroundResource(R.drawable.stop_button_not_press);
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

	private void setupLoveButton() {
		for (int i = 0; i < PAGE_NUM; i++) {
			loveButton[i].setOnClickListener(new LoveButtonListener(i));
		}
	}

	private void setupShareButton() {
		for (int i = 0; i < PAGE_NUM; i++) {
			shareButton[i].setOnClickListener(new ShareButtonListener(i));
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
						for (int k = 0; k < PAGE_NUM && k != i; k++) {
							if (mediaPlayer[k].isPlaying()) {
								playSongButton[k]
										.setBackgroundResource(R.drawable.stop_button_not_press);
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

	private class ShareButtonListener implements OnClickListener {
		private int i;

		public ShareButtonListener(int i) {
			this.i = i;
		}

		@Override
		public void onClick(View v) {
			// 暂时不做
			Toast.makeText(HomeActivity.this, "该功能还未开发", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private class LoveButtonListener implements OnClickListener {
		private int i;

		public LoveButtonListener(int i) {
			this.i = i;
		}

		@Override
		public void onClick(View v) {
			if (NetworkConnDetector.isNetworkConnected(getApplicationContext())) {
				if (SharedPreferenceUtil.getLoginFlag(getApplicationContext())) {
					if (isLoved[i]) {
						HomeModel.cancelLoveSong(songData.get(i).getSid(),
								SharedPreferenceUtil
										.getLoginInfo(getApplicationContext()),
								new CancelLoveSongListener(i));
					} else {
						HomeModel.loveSong(songData.get(i).getSid(),
								SharedPreferenceUtil
										.getLoginInfo(getApplicationContext()),
								new LoveSongListener(i));
					}
				} else {
					Toast.makeText(HomeActivity.this, "请先登录",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(HomeActivity.this, "网络未连接", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	private void setupSongSeekBar() {
		for (int i = 0; i < PAGE_NUM; i++) {
			songSeekBar[i].setEnabled(false);
		}
	}

	private class SeekBarHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			for (int i = 0; i < PAGE_NUM; i++) {
				if (msg.what == msgProcess[i]
						&& songProgress[i] < songSeekBar[i].getMax()) {
					songProgress[i] += 1;
					songSeekBar[i].incrementProgressBy(1);
					seekBarHandler.sendEmptyMessageDelayed(songProgress[i],
							1000);
				}
			}
		}
	}

	private class LoveSongListener implements HandleResultListener {
		private int i;

		public LoveSongListener(int i) {
			this.i = i;
		}

		@Override
		public void doResult(Object result) {
			if (!isFinishing() && !isFinished()) {
				if (Boolean.parseBoolean((String) result)) {
					loveButton[i].setImageResource(R.drawable.home_icon_love2);
					Toast.makeText(HomeActivity.this, "收藏成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(HomeActivity.this, "收藏失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private class CancelLoveSongListener implements HandleResultListener {
		private int i;

		public CancelLoveSongListener(int i) {
			this.i = i;
		}

		@Override
		public void doResult(Object result) {
			if (!isFinishing() && !isFinished()) {
				if (Boolean.parseBoolean((String) result)) {
					loveButton[i].setImageResource(R.drawable.home_icon_love1);
					Toast.makeText(HomeActivity.this, "取消收藏成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(HomeActivity.this, "取消收藏失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

}
