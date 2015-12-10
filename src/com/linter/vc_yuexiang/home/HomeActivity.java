package com.linter.vc_yuexiang.home;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.linter.vc_yuexiang.R;
import com.linter.vc_yuexiang.common.BaseActivity;
import com.linter.vc_yuexiang.login.LoginActivity;
import com.linter.vc_yuexiang.register.RegisterActivity;

public class HomeActivity extends BaseActivity {
	private static final int PAGE_NUM = 3;
	private ViewPager viewPager;
	private List<HomePageFragment> fragments;
	private PagerAdapter pagerAdapter;
	private boolean isBound = false;
	private PlaySongService songService = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		bindService();
		initFragments();
		initView();

		setupViewPager();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unBindService();
	}

	private void initFragments() {
		fragments = new ArrayList<HomePageFragment>();
		for (int i = 0; i < PAGE_NUM; i++) {
			HomePageFragment fragment = new HomePageFragment();
			fragment.setPosition(i);
			fragments.add(fragment);
		}
	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.home_homepage_viewpager);
	}

	private void setupViewPager() {
		pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),
				fragments);
		viewPager.setOffscreenPageLimit(2);
		viewPager.setAdapter(pagerAdapter);
		// viewPager.setOnPageChangeListener(new PageChangeListener());
	}

	private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		private List<HomePageFragment> fragments;

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public MyFragmentPagerAdapter(FragmentManager fm,
				List<HomePageFragment> fragments) {
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

	// 滑动监听，实现滑动某页加载数据时使用
	// private class PageChangeListener implements OnPageChangeListener {
	// @Override
	// public void onPageScrollStateChanged(int i) {
	//
	// }
	//
	// @Override
	// public void onPageScrolled(int arg0, float arg1, int arg2) {
	//
	// }
	//
	// @Override
	// public void onPageSelected(int arg0) {
	// fragments.get(arg0).isGetData();
	// }
	// }

	private void bindService() {
		Intent intent = new Intent(HomeActivity.this,
				PlaySongService.class);
		bindService(intent, conn, Context.BIND_AUTO_CREATE);
		isBound = true;
	}

	private void unBindService() {
		if (isBound) {
			unbindService(conn);
			isBound = false;
		}
	}

	private ServiceConnection conn = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			songService = ((PlaySongService.LocalBinder) service)
					.getService();
			for (int i = 0; i < PAGE_NUM; i++) {
				fragments.get(i).setService(songService);
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			songService = null;
		}
	};

	public void showDialog() {
		LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.dialog_login_register, null);
		Dialog dialog = new AlertDialog.Builder(HomeActivity.this).create();
		// dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(layout);
		dialog.setOnKeyListener(new BackButtonListener(dialog));

		Button registerButton = (Button) layout
				.findViewById(R.id.dialog_register_button);
		Button loginButton = (Button) layout
				.findViewById(R.id.dialog_login_button);
		registerButton.setOnClickListener(new DialogButtonListener(dialog));
		loginButton.setOnClickListener(new DialogButtonListener(dialog));
	}

	private class DialogButtonListener implements OnClickListener {
		private Dialog dialog;

		public DialogButtonListener(Dialog dialog) {
			this.dialog = dialog;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.dialog_register_button:
				RegisterActivity.start(HomeActivity.this);
				break;
			case R.id.dialog_login_button:
				LoginActivity.start(HomeActivity.this);
				break;
			}
			dialog.dismiss();
		}
	}

	private class BackButtonListener implements OnKeyListener {
		private Dialog dialog;

		public BackButtonListener(Dialog dialog) {
			this.dialog = dialog;
		}

		@Override
		public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
			if (arg1 == KeyEvent.KEYCODE_BACK && dialog.isShowing()) {
				dialog.dismiss();
			}
			return false;
		}

	}
}
