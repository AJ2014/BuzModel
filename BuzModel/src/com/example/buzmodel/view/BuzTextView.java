package com.example.buzmodel.view;

import java.util.List;

import com.example.buzmodel.R;
import com.example.buzmodel.cache.IDBUpdateCallback;
import com.example.buzmodel.model.TBuz;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

public class BuzTextView extends TextView implements IDBUpdateCallback {

	private int buzIndex; 

	public BuzTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.TBuz);
		if (null != arr) {
			buzIndex = arr.getInteger(R.styleable.TBuz_index, -1);
			arr.recycle();
		}
		setBackgroundResource(R.drawable.shape_rect_bg);
		setTextColor(Color.parseColor("#FFFFFF"));
	}

	@Override
	public void onInsert(List<TBuz> records) {
		if (null == records || records.isEmpty()) {
			return;
		}
		for (TBuz record : records) {
			if (record.getIndex() == buzIndex) {
				updateText(String.format("%f %s", record.getValue(), record.getUnit().getFlag()));
			}
		}
	}

	private void updateText(String content) {
		setText(content);
	}

}
