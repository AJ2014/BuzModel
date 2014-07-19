package com.example.buzmodel;

import com.example.buzmodel.view.BuzImageView;
import com.example.buzmodel.view.BuzTextView;
import com.example.buzmodel.view.map.lib.CircleShape;
import com.example.buzmodel.view.map.lib.Shape;
import com.example.buzmodel.view.map.lib.ShapeExtension.OnShapeActionListener;
import com.example.buzmodel.view.map.lib.TextShape;
import com.example.buzmodel.view.map.view.HighlightImageView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

	BuzTextView mTv;
	BuzImageView mImg;
	
	HighlightImageView mHImg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
//		mTv = (BuzTextView) findViewById(R.id.tv_buz);
//		mImg = (BuzImageView) findViewById(R.id.img_buz);
		mHImg = (HighlightImageView) findViewById(R.id.himg_buz);
		
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main, new BitmapFactory.Options());
		mHImg.setImageBitmap(bitmap);
		
		CircleShape shape = new CircleShape("Tag", Color.RED);
		TextShape tShape = new TextShape("tag1", Color.BLACK);
		
		shape.setValues(dip2px(getApplicationContext(), 119f),
				dip2px(getApplicationContext(), 54f),20);
		tShape.setValues(dip2px(getApplicationContext(), 139f), 
				dip2px(getApplicationContext(), 44f),
				dip2px(getApplicationContext(), 184f),
				dip2px(getApplicationContext(), 54f));

		mHImg.addShape(shape);
		mHImg.addShape(tShape);
		mHImg.setOnShapeClickListener(new OnShapeActionListener() {
			@Override
			public void onShapeClick(Shape shape, float xOnImage, float yOnImage) {
				if ("Tag".equals(shape.tag)) {
					Toast.makeText(getApplicationContext(), "圆形被点击", Toast.LENGTH_SHORT).show();
				} else if ("tag1".equals(shape.tag)) {
					Toast.makeText(getApplicationContext(), "矩形被点击", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	 /** 
     * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����) 
     */  
    public int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * �����ֻ��ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp 
     */  
    public int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    } 
}
