package com.example.buzmodel.view;

import com.example.buzmodel.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MyCommonPopWindow {

	public static interface CommonBottomUpPopWondowCallBack{
		public void onClickItem(View clickedView,int position);
	}
		
	public static Dialog showCommonBottomUpPopWondow(Context context, String[] itemsName, CommonBottomUpPopWondowCallBack callback){
	       return showCommonPopWondow(context, itemsName, callback);
	}
	
	public static Dialog showCommonPopWondow(Context context, 
			String[] itemsName, final CommonBottomUpPopWondowCallBack callBack){
		View aPopContentView = LayoutInflater.from(context).inflate(R.layout.popwindow_common_bottom_up_layout,null);
		final Dialog aPopWindowDialog = new Dialog(context, R.style.DialogPopwindowStyle);
		ListView aPopConentLv = (ListView) aPopContentView.findViewById(R.id.listview);
		
		aPopConentLv.setAdapter(new PopupListAdapter(context, itemsName, new PopupListAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				if (null != aPopWindowDialog && aPopWindowDialog.isShowing()) {
					aPopWindowDialog.dismiss();
				}
				if (null != callBack) {
					callBack.onClickItem(view, position);
				}
			}
		}));
		aPopConentLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (null != callBack) {
					callBack.onClickItem(arg1, arg2);
				}
			}
		});
		aPopWindowDialog.setContentView(aPopContentView);
		//设置对话框宽度和动画
		Window window = aPopWindowDialog.getWindow();
		WindowManager.LayoutParams aWmLp = window.getAttributes();
		aWmLp.width = WindowManager.LayoutParams.MATCH_PARENT;
		aWmLp.gravity = Gravity.BOTTOM;
		aWmLp.x = 0;
		aWmLp.y = 0;
		window.setAttributes(aWmLp);
		window.setWindowAnimations(R.style.AnimationStyleDialogPopwindow_frombottom);
		
		aPopWindowDialog.setCanceledOnTouchOutside(true);
		aPopWindowDialog.show();
		return aPopWindowDialog;
	
	}
	
	public static class PopupListAdapter extends BaseAdapter {

		Context context;
		String[] titles;
		OnItemClickListener listener;
		
		public static interface OnItemClickListener {
			public void onItemClick(View view, int position);
		}
		
		public PopupListAdapter(Context context, String[] titles, OnItemClickListener listener) {
			this.context = context;
			this.titles = titles;
			this.listener = listener;
		}
		
		@Override
		public int getCount() {
			return null == titles ? 0 : titles.length;
		}

		@Override
		public Object getItem(int position) {
			return titles[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			TextView tvTitle = null;
			if (null == convertView) {
				convertView = LayoutInflater.from(context).inflate(R.layout.popwindow_common_bottom_up_item_layout, null);
			}
			final View view = convertView;
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (null != listener) {
						listener.onItemClick(view, position);
					}
				}
			});
			tvTitle = (TextView) convertView.findViewById(R.id.tv_item);
			tvTitle.setText((CharSequence) getItem(position));
			return convertView;
		}
		
	}
	
}
