package com.linter.vc_yuexiang.radio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vc_yuexiang.R;
import com.linter.vc_yuexiang.common.BaseActivity;
import com.linter.vc_yuexiang.common.DensityUtil;

public class RadioActivity extends BaseActivity {
	private static final boolean MINE_RADIO = false;
	private static final boolean FIND_RADIO = true;
	private RelativeLayout titleLayout;
	private ImageButton returnButton;
	private ImageButton searchButton;
	private TextView mineTextView;
	private TextView findTextView;

	private boolean flagOfFragment;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private Fragment mineRadioFragment;
	private Fragment findRadioFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radio);

		initView();
		initFragmentManager();
		initFragmentTransaction();
		initFirstShownFragment();
		setupTitlebar();
	}

	private void initView() {
		titleLayout = (RelativeLayout) findViewById(R.id.radio_title_layout);
		returnButton = (ImageButton) findViewById(R.id.radio_return_button);
		searchButton = (ImageButton) findViewById(R.id.radio_search_button);
		mineTextView = (TextView) findViewById(R.id.radio_mine_textview);
		findTextView = (TextView) findViewById(R.id.radio_find_textview);
	}

	private void initFragmentManager() {
		fragmentManager = getSupportFragmentManager();
	}

	private void initFragmentTransaction() {
		fragmentTransaction = fragmentManager.beginTransaction();
		mineRadioFragment = new MineRadioFragment();
		findRadioFragment = new FindRadioFragment();
		fragmentTransaction.add(R.id.radio_fragment_layout, mineRadioFragment);
		fragmentTransaction.add(R.id.radio_fragment_layout, findRadioFragment);
		flagOfFragment = MINE_RADIO;
		fragmentTransaction.commit();
	}

	private void initFirstShownFragment() {
		flagOfFragment = MINE_RADIO;
		switchFragment();
		switchTitleText();
		switchFlagOfFragment();
	}

	private void setupTitlebar() {
		TitleBarClickListener listener = new TitleBarClickListener();
		titleLayout.setOnClickListener(listener);
		returnButton.setOnClickListener(listener);
		searchButton.setOnClickListener(listener);
	}

	private class TitleBarClickListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.radio_title_layout:
				switchFragment();
				switchTitleText();
				switchFlagOfFragment();
				break;
			case R.id.radio_return_button:
				finish();
				break;
			case R.id.radio_search_button:
				Toast.makeText(RadioActivity.this, "搜索功能,敬请期待",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}

	private void switchFragment() {
		fragmentTransaction = fragmentManager.beginTransaction();
		if (flagOfFragment == MINE_RADIO) {
			fragmentTransaction.hide(mineRadioFragment);
			fragmentTransaction.show(findRadioFragment);
		} else if (flagOfFragment == FIND_RADIO) {
			fragmentTransaction.hide(findRadioFragment);
			fragmentTransaction.show(mineRadioFragment);
		}
		fragmentTransaction.commit();
	}

	private void switchTitleText() {
		if (flagOfFragment == MINE_RADIO) {
			findTextView.setTextSize(DensityUtil.sp2px(getApplicationContext(),
					20));
			findTextView.setTextColor(getResources().getColor(
					R.color.radio_textview_title_selected));
			mineTextView.setTextSize(DensityUtil.sp2px(getApplicationContext(),
					16));
			mineTextView.setTextColor(getResources().getColor(
					R.color.radio_textview_title_notselected));
		} else if (flagOfFragment == FIND_RADIO) {
			mineTextView.setTextSize(DensityUtil.sp2px(getApplicationContext(),
					20));
			mineTextView.setTextColor(getResources().getColor(
					R.color.radio_textview_title_selected));
			findTextView.setTextSize(DensityUtil.sp2px(getApplicationContext(),
					16));
			findTextView.setTextColor(getResources().getColor(
					R.color.radio_textview_title_notselected));
		}
	}

	private void switchFlagOfFragment() {
		if (flagOfFragment == MINE_RADIO) {
			flagOfFragment = FIND_RADIO;
		} else {
			flagOfFragment = MINE_RADIO;
		}
	}
}
