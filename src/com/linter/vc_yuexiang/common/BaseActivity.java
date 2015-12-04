package com.linter.vc_yuexiang.common;

import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {
	private boolean isDestroyed = false;

	@Override
	protected void onDestroy() {
		isDestroyed = true;
		super.onDestroy();
	}
	
	public boolean isFinished(){
		return isDestroyed;
	}
}
