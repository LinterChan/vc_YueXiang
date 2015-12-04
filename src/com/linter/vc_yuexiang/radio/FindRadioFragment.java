package com.linter.vc_yuexiang.radio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.vc_yuexiang.R;
import com.linter.vc_yuexiang.http.HttpRequestHelper.HandleResultListener;

public class FindRadioFragment extends Fragment {
	private RadioActivity activity;
	private ListView radioListView;
	private List<Map<String, String>> listData;
	private RadioListAdapter listAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_findradio, container,
				false);
		getParentActivity();
		initListData();
		initView(view);
		setupRadioListView();
		return view;
	}

	private void getParentActivity() {
		activity = (RadioActivity) getActivity();
	}

	private void initView(View view) {
		radioListView = (ListView) view
				.findViewById(R.id.findradio_radio_listview);
	}

	private void initListData() {
		listData = new ArrayList<Map<String, String>>();
		String photoUrl = "http://www.33lc.com/article/UploadPic/2012-8/201281010265276953.jpg";
		for (int i = 0; i < 100; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("photoUrl", photoUrl);
			listData.add(map);
		}
	}

	private void setupRadioListView() {
		listAdapter = new RadioListAdapter(activity, listData);
		radioListView.setAdapter(listAdapter);
	}

	private class RadioListAdapter extends BaseAdapter {
		private List<Map<String, String>> listData;
		private LayoutInflater inflater;

		public RadioListAdapter(Context context,
				List<Map<String, String>> listData) {
			inflater = LayoutInflater.from(context);
			this.listData = listData;
		}

		@Override
		public int getCount() {
			return listData.size();
		}

		@Override
		public Object getItem(int arg0) {
			return listData.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(R.layout.list_item_radio, null);
				viewHolder.headphotoImageView = (ImageView) convertView
						.findViewById(R.id.radio_item_headphoto_imageview);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			RadioModel.loadImage(listData.get(position).get("photoUrl"),
					new SetHeadPhotoListener(viewHolder.headphotoImageView));
//			 viewHolder.headphotoImageView.setImageResource(R.drawable.ic_launcher);
			return convertView;
		}

		class ViewHolder {
			public ImageView headphotoImageView;
		}
	}

	private class SetHeadPhotoListener implements HandleResultListener {
		private ImageView headphotoImageView;

		public SetHeadPhotoListener(ImageView headphotoImageView) {
			this.headphotoImageView = headphotoImageView;
		}

		@Override
		public void doResult(Object result) {
			if (!activity.isFinishing() && !activity.isFinished()) {
				Bitmap bitmap = (Bitmap) result;
				headphotoImageView.setImageBitmap(bitmap);
			}
		}
	}
}
