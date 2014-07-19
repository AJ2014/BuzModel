package com.example.buzmodel.view.map.lib;

import android.graphics.PointF;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * author : chenyoca@gmail.com
 * date   : 13-5-19
 * The bubble wrapper.
 */
public class Bubble extends FrameLayout{

    static final boolean IS_API_11_LATER = Build.VERSION.SDK_INT >= 11;

    public final View view;
    public final PointF position = new PointF();

    private RenderDelegate renderDelegate;

    public Bubble(View view){
		super(view.getContext());
        this.view = view;
        final int wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;
        LayoutParams params = new LayoutParams(wrapContent,wrapContent);
        this.view.setLayoutParams(params);
        this.view.setClickable(true);
		addView(view);
    }

	/**
	 * Bubble鐣岄潰娓叉煋浠ｇ悊銆傜敤浠ュ鐞咮ubble鐣岄潰鐨勬暟鎹～鍏呫��
	 */
	public interface RenderDelegate {
        void onDisplay(Shape shape, View bubbleView);
    }

    /**
     * 涓築ubble鐣岄潰璁剧疆涓�涓覆鏌撲唬鐞嗘帴鍙�
     * @param renderDelegate 娓叉煋浠ｇ悊鎺ュ彛
     */
    public void setRenderDelegate (RenderDelegate renderDelegate) {
        this.renderDelegate = renderDelegate;
    }

    /**
     * Show the bubble view controller on the shape.
     * @param shape the shape to show on
     */
    public void showAtShape(Shape shape){
        if(view == null) return;
        shape.createBubbleRelation(this);
        setBubbleViewAtPosition(shape.getCenterPoint());
        if (renderDelegate != null){
            renderDelegate.onDisplay(shape, view);
        }
        view.setVisibility(View.VISIBLE);
    }

    private void setBubbleViewAtPosition(PointF center){
        float posX = center.x - view.getWidth()/2;
        float posY = center.y - view.getHeight();
		setBubbleViewAtPosition(posX, posY);
    }

    private void setBubbleViewAtPosition(float x, float y){

		// BUG : HTC SDK 2.3.3 鐣岄潰浼氳涓嶅仠鐨勯噸缁�,杩欎釜閲嶇粯璇锋眰鏄疺iew.onDraw()鏂规硶鍙戣捣鐨勩��
		if(position.equals(x,y)) return;

        position.set(x,y);

		if(IS_API_11_LATER){
            view.setX(x);
            view.setY(y);
        }else{
            LayoutParams params = (LayoutParams) view.getLayoutParams();
			int left = (int)x;
			int top = (int)y;
			// HTC SDK 2.3.3 Required
			params.gravity = Gravity.CENTER_VERTICAL | Gravity.TOP;
			params.leftMargin = left;
			params.topMargin = top;
			view.setLayoutParams(params);
        }
    }

}
