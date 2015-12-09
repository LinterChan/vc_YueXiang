package com.linter.vc_yuexiang.common;

import android.support.v4.app.Fragment;

public abstract class LazyFragment extends Fragment {
	protected boolean isVisible;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		isVisible = isVisibleToUser;
		if (isVisibleToUser) {
			lazyLoad();
		}
	}

	protected abstract void lazyLoad();
}
