package com.example.buzmodel.view;

import java.util.List;

import com.example.buzmodel.R;
import com.example.buzmodel.cache.IDBUpdateCallback;
import com.example.buzmodel.model.EBuzQuality;
import com.example.buzmodel.model.TBuz;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BuzImageView extends ImageView implements IDBUpdateCallback {

	private int buzIndex;

	public BuzImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.TBuz);
		if (null != arr) {
			buzIndex = arr.getInteger(R.styleable.TBuz_index, -1);
			arr.recycle();
		}
		setScaleType(ScaleType.CENTER_CROP);
	}
	
	@Override
	public void onInsert(List<TBuz> records) {
		if (null == records || records.isEmpty()) {
			return;
		}
		for (TBuz record : records) {
			if (record.getIndex() == buzIndex) {
				updateSrc(record.getQuality());
			}
		}
	}

	private void updateSrc(EBuzQuality status) {
		int rid = R.drawable.shape_ring_normal;
		switch (status) {
		case NORMAL:
			rid = R.drawable.shape_ring_normal;
			break;
		case WARN:
			rid = R.drawable.shape_ring_warn;
			break;
		case ERROR:
			rid = R.drawable.shape_ring_error;
			break;
		default:
			rid = R.drawable.shape_ring_normal;
			break;
		}
		setImageResource(rid);
	}

}
