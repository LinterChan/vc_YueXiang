package com.linter.vc_yuexiang.common;

import android.app.Activity;

public class BaseActivity extends Activity {
	private boolean isDestroyed = false;

	@Override
	protected void onDestroy() {
		isDestroyed = true;
		super.onDestroy();
	}
	
	protected boolean isFinished(){
		return isDestroyed;
	}
}
