package com.linter.vc_yuexiang.home;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HomePlaySongService extends Service {
	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
