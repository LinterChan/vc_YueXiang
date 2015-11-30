package com.linter.vc_yuexiang.home;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.example.vc_yuexiang.R;

public class HomeActivity extends FragmentActivity {
	private static final int PAGE_NUM = 3;
	private boolean isFinished = false;
	private ViewPager viewPager;
	private List<HomePageFragment> fragments;
	private PagerAdapter pagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		initFragments();
		initView();

		setupViewPager();
	}

	protected boolean isFinished() {
		return isFinished;
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
		viewPager.setOnPageChangeListener(new PageChangeListener());
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

	private class PageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int i) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			fragments.get(arg0).isGetData();
		}
	}
}
