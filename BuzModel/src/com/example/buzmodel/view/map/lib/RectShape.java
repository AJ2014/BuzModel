package com.example.buzmodel.view.map.lib;

import android.graphics.Canvas;
import android.graphics.PointF;

public class RectShape extends Shape{

    protected float left;
    protected float top;
    protected float bottom;
    protected float right;

    public RectShape(Object tag, int coverColor) {
        super(tag, coverColor);
    }

    /**
     * set Left,Top,Right,Bottim
     * @param coords left,top,right,buttom
     */
    @Override
    public void setValues(float... coords) {
        if(coords == null || coords.length != 4){
            throw new IllegalArgumentException("Please set values with 4 paramters: left,top,right,buttom");
        }
        left = coords[0];
        top = coords[1];
        right = coords[2];
        bottom = coords[3];
    }

	@Override
	public void onScale (float scale) {
		left *= scale;
		top *= scale;
		right *= scale;
		bottom *= scale;
	}

	@Override
    public void draw(Canvas canvas) {
        drawPaint.setAlpha(alaph);
        // draw stroke
        drawPaint.setColor(strokeColor);
        canvas.drawRect(left - strokeWidth,
                top - strokeWidth, right + strokeWidth,
                bottom + strokeWidth, drawPaint);
        drawPaint.setColor(color);
        canvas.drawRect(left,top,right,bottom,drawPaint);

    }

    @Override
    public void scaleBy (float scale, float centerX, float centerY) {

        PointF leftTop = Utility.scaleByPoint(left, top, centerX, centerY, scale);
        left = leftTop.x;
        top = leftTop.y;

        PointF righBottom = Utility.scaleByPoint(right, bottom, centerX, centerY, scale);
        right = righBottom.x;
        bottom = righBottom.y;
    }

    @Override
    public void translate(float deltaX, float deltaY) {
        left += deltaX;
        right += deltaX;
        top += deltaY;
        bottom += deltaY;
    }

    @Override
    public boolean inArea(float x, float y) {
        boolean ret = false;
        if ((x > left) && (x < right)) {
            if ((y > top) && (y < bottom)) {
                ret = true;
            }
        }
        return ret;
    }

    @Override
    public PointF getCenterPoint() {
        float centerX = (left + right)/2.0f;
        float centerY = (top + bottom)/2.0f;
        return new PointF(centerX,centerY);
    }
}
