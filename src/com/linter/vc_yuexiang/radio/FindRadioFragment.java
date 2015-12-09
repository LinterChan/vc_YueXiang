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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.vc_yuexiang.R;
import com.linter.vc_yuexiang.http.HttpRequestHelper.HandleResultListener;
import com.linter.vc_yuexiang.http.ImageLoader;

public class FindRadioFragment extends Fragment {
	private boolean firstFlag = true;
	private RadioActivity activity;
	private ListView radioListView;
	private List<Map<String, String>> listData;
	private RadioListAdapter listAdapter;
	private ImageLoader imageLoader = new ImageLoader();
	private int startItem = 0, endItem = 0;

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
		String[] photoUrl = new String[] {
				"http://img2.imgtn.bdimg.com/it/u=249296375,4206828661&fm=21&gp=0.jpg",
				"http://b.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=76a81c8a8882b9013df8cb3746bd8541/a686c9177f3e67097069725239c79f3df8dc5535.jpg",
				"http://h.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=462fdee14ec2d562f25dd8ebd721bcd7/7acb0a46f21fbe099474b3956a600c338744ad0d.jpg",
				"http://img5.imgtn.bdimg.com/it/u=3889276843,4252495472&fm=21&gp=0.jpg",
				"http://www.qqpk.cn/Article/UploadFiles/201202/20120212114847277.jpg",
				"http://f.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=b11d937b7acb0a468577833f5b53da1c/8326cffc1e178a8204ef0e21f703738da877e864.jpg",
				"http://img0.imgtn.bdimg.com/it/u=3356092180,388829354&fm=21&gp=0.jpg",
				"http://img1.3lian.com/gif/more/11/201211/50df7426282374c102d348df75f38659.jpg",
				"http://www.qqpk.cn/Article/UploadFiles/201111/20111108212457370.jpg",
				"http://img3.imgtn.bdimg.com/it/u=722916498,1659074121&fm=21&gp=0.jpg",
				"http://www.2cto.com/uploadfile/2013/0402/20130402081505181.jpg",
				"http://wenwen.soso.com/p/20100824/20100824210252-26443441.jpg",
				"http://www.ttoou.com/qqtouxiang/allimg/120906/co120Z6222601-15-lp.jpg",
				"http://img1.imgtn.bdimg.com/it/u=1358849563,2313233544&fm=21&gp=0.jpg",
				"http://up.qqjia.com/z/10/tu12060_12.jpg",
				"http://up.qqjia.com/z/04/tu6183_55.jpg" };
		// String photoUrl =
		// "http://img1.imgtn.bdimg.com/it/u=1277545764,1877374477&fm=21&gp=0.jpg";
		// String photoUrl =
		// "http://www.33lc.com/article/UploadPic/2012-8/201281010265276953.jpg";
		for (int i = 0; i < photoUrl.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("photoUrl", photoUrl[i]);
			listData.add(map);
		}
	}

	private void setupRadioListView() {
		listAdapter = new RadioListAdapter(activity, listData);
		radioListView.setAdapter(listAdapter);
		radioListView.setOnScrollListener(new RadioListScrollListener());
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
			viewHolder.headphotoImageView.setTag(listData.get(position).get(
					"photoUrl"));
			// 1.首先listview中的item不全部进行图片加载
			// 2.消除多次刷新的现象,否则会出现滑动后显示listview缓存的图片
			showImage(listData.get(position).get("photoUrl"),
					viewHolder.headphotoImageView);
			return convertView;
		}

		class ViewHolder {
			public ImageView headphotoImageView;
		}

	}

	private void showImage(String url, ImageView imageView) {
		Bitmap bitmap = imageLoader.getBitmapFromMemoryCaches(url);
		if (bitmap == null) {
			imageView.setImageResource(R.drawable.ic_launcher);
		} else {
			imageView.setImageBitmap(bitmap);
		}
	}

	private class SetHeadPhotoListener implements HandleResultListener {
		private String photoUrl;
		private ImageView headphotoImageView;

		public SetHeadPhotoListener(String photoUrl,
				ImageView headphotoImageView) {
			this.photoUrl = photoUrl;
			this.headphotoImageView = headphotoImageView;
		}

		@Override
		public void doResult(Object result) {
			if (!activity.isFinishing() && !activity.isFinished()) {
				if (headphotoImageView.getTag().equals(photoUrl)) {
					Bitmap bitmap = (Bitmap) result;
					headphotoImageView.setImageBitmap(bitmap);
				}
			}
		}
	}

	private class RadioListScrollListener implements OnScrollListener {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalVisibleItem) {
			startItem = firstVisibleItem;
			endItem = firstVisibleItem + visibleItemCount - 1;
			if (firstFlag && visibleItemCount > 0) {
				loadVisibleItem(view, startItem, endItem);
				firstFlag = false;
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == SCROLL_STATE_IDLE) {
				loadVisibleItem(view, startItem, endItem);
			} else {
				imageLoader.cancelAllTasks();
			}
		}

	}

	private void loadVisibleItem(AbsListView view, int startItem, int endItem) {
		for (int i = startItem; i <= endItem; i++) {
			String photoUrl = listData.get(i).get("photoUrl");
			ImageView imageView = (ImageView) view.findViewWithTag(photoUrl);
			imageLoader
					.loadImage(photoUrl, imageView.getWidth(), imageView
							.getHeight(), new SetHeadPhotoListener(photoUrl,
							imageView));
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (hidden) {
			imageLoader.cancelAllTasks();
		} else {
			loadVisibleItem(radioListView, startItem, endItem);
		}
	}
}
