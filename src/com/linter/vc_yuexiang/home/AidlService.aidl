package com.linter.vc_yuexiang.home;
import com.linter.vc_yuexiang.home.OnStopSongListener;
interface AidlService{
	void setupPlayer(String songUrl);
	void playSong();
	void stopSong(String songUrl);
	void setOnStopSongListener(OnStopSongListener listener);
}