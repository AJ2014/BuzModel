package com.example.buzmodel.view.map.lib;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TextShape extends RectShape {

	String text;
    String flag;
	private float textSize = 12f;
	
	public TextShape(Object tag, int coverColor) {
		super(tag, coverColor);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		drawPaint.setColor(Color.WHITE);
		drawPaint.setTextSize(textSize);
        Paint.FontMetrics fm = drawPaint.getFontMetrics();
        float tHeight = fm.bottom - fm.ascent;
        float tWidth = drawPaint.measureText(text);
        float cHeight = bottom - top;
        float tLeft = right - tWidth;
        float tBottom = bottom - (cHeight - tHeight) / 2f;
		canvas.drawText(null == text ? "" : text,tLeft, tBottom, drawPaint);
        canvas.drawText(flag, right + textSize / 2f, tBottom, drawPaint);
		drawPaint.setColor(color);
	}
	
	@Override
	public void scaleBy(float scale, float centerX, float centerY) {
		super.scaleBy(scale, centerX, centerY);
		textSize *= scale;
	}

    public void setText(String text, String flag) {
        this.text = text;
        this.flag = flag;
        if (null != container) {
            container.invalidate();
        }
    }
	
}
