package com.example.buzmodel.view;

import com.example.buzmodel.R;
import com.example.buzmodel.Utils;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.DatePicker.OnDateChangedListener;

public class MyCommonDialogMaker {
	
	public static interface DialogCallBack {
		public void onButtonClicked(Dialog dialog, int position, Object tag);
		public void onCancelDialog(Dialog dialog, Object tag);
	}
	
	public static Dialog showDatePickerDialog(Context context, String msg, String defaultDate, String[] btns,
			final DialogCallBack callBack, boolean isCanCancelabel) {
		final Dialog dialog = new Dialog(context, R.style.DialogNoTitleStyleTranslucentBg);
		View contentView = LayoutInflater.from(context)
				.inflate(R.layout.dialog_datepicker_common_layout, null);
		TextView contentTv = (TextView) contentView
				.findViewById(R.id.dialog_title_tv);
		final DatePicker datePicker = (DatePicker)contentView.findViewById(R.id.myDatePicker);
		OnClickListener lis = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != callBack) {
					String birthday = String.format("%04d-%02d-%02d", datePicker.getYear(), 
							datePicker.getMonth()+1, datePicker.getDayOfMonth());
					callBack.onButtonClicked(dialog, (Integer) v.getTag(), birthday);
				}
                dialog.dismiss();
			}

		};
		if (null != btns) {
			int len = btns.length;
			if(len > 0){
				LinearLayout btnLayout = (LinearLayout) contentView
						.findViewById(R.id.btn_layout);
				Button btn;
				View LineView;
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,Utils.dip2px(context, 44f));
				params.weight = 1.0f ;
				for (int i = 0; i < len; i++) {
					btn = new Button(context);
					btn.setText(btns[i]);
					btn.setTag(i);
					btn.setSingleLine(true);
					btn.setEllipsize(TruncateAt.END);
					btn.setOnClickListener(lis);
					btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.dip2px(context, 16f));
					btn.setTextColor(0xff007cff);
					btn.setGravity(Gravity.CENTER);
					if(0 == i && 1 == len){
						btn.setBackgroundResource(R.drawable.alert_single_btn_selector);
					}
					else if(0 == i){
						btn.setBackgroundResource(R.drawable.alert_left_btn_selector);
					}
					else if(i > 0 && len > 2){
						btn.setBackgroundResource(R.drawable.alert_mid_btn_selector);
					}
					else if(len - 1 == i){
						btn.setBackgroundResource(R.drawable.alert_right_btn_selector);
					}
					btn.setPadding(10, 10, 10, 10);
					btnLayout.addView(btn, params);
					LineView = new View(context);
					LineView.setBackgroundColor(Color.parseColor("#b2b2b2"));
					if(i < len -1 && len > 1){
						btnLayout.addView(LineView,new LinearLayout.LayoutParams(1,LinearLayout.LayoutParams.FILL_PARENT));
					}
				}
			}
			
		}
		String[] dates = defaultDate.split("-");
		datePicker.init(Integer.valueOf(dates[0]), Integer.valueOf(dates[1])-1, Integer.valueOf(dates[2]), new OnDateChangedListener() {

			@Override
			public void onDateChanged(DatePicker datepicker, int year, int month, int date) {
			}
			
		});
		
		if (TextUtils.isEmpty(msg)) {
			contentTv.setText(msg);
		}
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {

				if (null != callBack) {
					callBack.onCancelDialog((Dialog) dialog, null);
				}
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(isCanCancelabel);
		dialog.setContentView(contentView);
		//设置对话框宽度
		Window window = dialog.getWindow();
		WindowManager.LayoutParams aWmLp = window.getAttributes();
		aWmLp.width = Utils.dip2px(context, 300f);
		aWmLp.gravity = Gravity.CENTER;
		window.setAttributes(aWmLp);
		
		dialog.show();
		return dialog;
	
	}

}
