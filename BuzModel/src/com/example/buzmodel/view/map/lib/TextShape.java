package com.example.buzmodel.view.map.lib;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

public class TextShape extends RectShape {

	String text;
	float textSize = 12f;
	
	public TextShape(Object tag, int coverColor) {
		super(tag, coverColor);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		drawPaint.setColor(Color.WHITE);
		drawPaint.setTextSize(textSize);
		canvas.drawText("HelloWord", left, bottom, drawPaint);
		drawPaint.setColor(color);
	}
	
	@Override
	public void scaleBy(float scale, float centerX, float centerY) {
		super.scaleBy(scale, centerX, centerY);
		textSize *= scale;
		Log.i("junjiang2", "onScale = " + textSize);
	}
	
}
