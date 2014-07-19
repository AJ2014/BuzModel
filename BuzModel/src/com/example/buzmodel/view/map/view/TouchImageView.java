package com.example.buzmodel.view.map.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

/**
 * TouchImageView设计为一个具有独立完整功能的View。可缩放，拖动图片�??
 * TouchImageView - A full View with Scale/Drag support.
 */
public class TouchImageView extends ImageView {

	private final Matrix imageUsingMatrix = new Matrix();
	private final Matrix imageSavedMatrix = new Matrix();
	private final Matrix overLayerMatrix = new Matrix();

	/* 惯�?�系�? */
    private static final float FRICTION = 0.9f;

	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;

	private int mode = NONE;

	private float redundantXSpace;
	private float redundantYSpace;

	private float right, bottom, origWidth, origHeight, bmWidth, bmHeight;

	/* View的宽度和高度 */
    private float viewWidth;
	private float viewHeight;

	private PointF last = new PointF();
	private PointF mid = new PointF();
	private  PointF start = new PointF();

	private float[] matrixValues;

	/* 绝对偏移量：View原点与图片原点的偏移量，无论缩放或�?�平移，这对值都表达它们的实际距离�??
	 * 注：以View和图片的左上角为原点，向下为Y轴正向，向右为X轴正向�??
	 * */
	private float absoluteOffsetX;
	private float absoluteOffsetY;

	private float saveScale = 1f;
	private float minScale = 1f;
	private float maxScale = 5f;
	private float oldDist = 1f;

	private PointF lastDelta = new PointF(0, 0);
	private float velocity = 0;
	private long lastDragTime = 0;
    
    private Context mContext;
    private ScaleGestureDetector mScaleDetector;

    public boolean onLeftSide = false, onTopSide = false, onRightSide = false, onBottomSide = false;

    public TouchImageView(Context context) {
        this(context,null);
    }
    
    public TouchImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        super.setClickable(true);
        this.mContext = context;
        initialized();
    }
    
    private OnTouchListener touchListener = new OnTouchListener() {

    	final static float MAX_VELOCITY = 1.2f;
    	
    	private long dragTime ;
    	private float dragVelocity;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
            mScaleDetector.onTouchEvent(event);

            fillAbsoluteOffset();
            PointF curr = new PointF(event.getX(), event.getY());
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mode = DRAG;
                    float xOnView =  event.getX(0);
                    float yOnView = event.getY(0);
                    onViewClick(xOnView, yOnView);
                    imageSavedMatrix.set(imageUsingMatrix);
                    last.set(event.getX(), event.getY());
                    start.set(last);

                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        imageSavedMatrix.set(imageUsingMatrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    break;

                case MotionEvent.ACTION_UP:
                	if(mode == DRAG){
                		velocity = dragVelocity;
                	}
                    mode = NONE;
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    velocity = 0;
                    imageSavedMatrix.set(imageUsingMatrix);
                    oldDist = spacing(event);
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                    	dragTime = System.currentTimeMillis();
                    	dragVelocity = (float)distanceBetween(curr, last) / (dragTime - lastDragTime) * FRICTION;
                    	dragVelocity = Math.min(MAX_VELOCITY,dragVelocity);
                        lastDragTime = dragTime;
                        float deltaX = curr.x - last.x;
                        float deltaY = curr.y - last.y;
                        checkAndSetTranslate(deltaX, deltaY);
                        lastDelta.set(deltaX, deltaY);
                        last.set(curr.x, curr.y);
                    }
                    break;
                }

            setImageMatrix(imageUsingMatrix);
            invalidate();
            return false;
		}
    };
    
	protected void initialized () {
        matrixValues = new float[9];
        setScaleType(ScaleType.MATRIX);
        setImageMatrix(imageUsingMatrix);
        mScaleDetector = new ScaleGestureDetector(mContext, new ScaleListener());
        setOnTouchListener(touchListener);

		float scale = saveScale = 1.0f;
		imageUsingMatrix.setScale(scale, scale);
		overLayerMatrix.setScale(scale, scale);
    }

	/**
	 * View被点�?
	 * @param xOnView View上的X坐标
	 * @param yOnView View上的Y坐标
	 */
    protected void onViewClick (float xOnView, float yOnView){}
	
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        scrolling();
    }

	public PointF getAbsoluteCenter (){
		fillAbsoluteOffset();
		return new PointF( Math.abs(absoluteOffsetX) + viewWidth/2,Math.abs(absoluteOffsetY) + viewHeight/2 );
	}

	public void moveBy (float deltaX, float deltaY){
		checkAndSetTranslate(deltaX, deltaY);
		setImageMatrix(imageUsingMatrix);
	}

	/**
	 * 惯�?�滚�?
	 */
	private void scrolling (){
		final float deltaX = lastDelta.x * velocity;
		final float deltaY = lastDelta.y * velocity;
		if (deltaX > viewWidth || deltaY > viewHeight) return;
		velocity *= FRICTION;
		if (Math.abs(deltaX) < 0.1 && Math.abs(deltaY) < 0.1) {
			return;
		}
		moveBy(deltaX, deltaY);
	}

	/**
	 * 提交平移变换
	 * @param deltaX 平移距离X
	 * @param deltaY 平移距离Y
	 */
    protected void postTranslate(float deltaX, float deltaY){
    	imageUsingMatrix.postTranslate(deltaX, deltaY);
    	overLayerMatrix.postTranslate(deltaX, deltaY);
		fillAbsoluteOffset();
    }

	/**
	 * 提交缩放
	 * @param scaleFactor 缩放比例
	 * @param scaleCenterX 缩放中心X
	 * @param scaleCenterY 缩放中心Y
	 */
    protected void postScale(float scaleFactor, float scaleCenterX, float scaleCenterY){
    	imageUsingMatrix.postScale(scaleFactor, scaleFactor, scaleCenterX, scaleCenterY);
        overLayerMatrix.postScale(scaleFactor, scaleFactor, scaleCenterX, scaleCenterY);
		fillAbsoluteOffset();
    }

	/**
	 * �?测平移边界并设置平移
	 * @param deltaX 平移距离X
	 * @param deltaY 平移距离Y
	 */
    private void checkAndSetTranslate(float deltaX, float deltaY)
    {
        float scaleWidth = Math.round(origWidth * saveScale);
        float scaleHeight = Math.round(origHeight * saveScale);
        fillAbsoluteOffset();
		final float x = absoluteOffsetX;
		final float y = absoluteOffsetY;
        if (scaleWidth < viewWidth) {
            deltaX = 0;
            if (y + deltaY > 0)
                deltaY = -y;
            else if (y + deltaY < -bottom)
                deltaY = -(y + bottom);
        } else if (scaleHeight < viewHeight) {
            deltaY = 0;
            if (x + deltaX > 0)
                deltaX = -x;
            else if (x + deltaX < -right)
                deltaX = -(x + right);
        }
        else {
            if (x + deltaX > 0)
                deltaX = -x;
            else if (x + deltaX < -right)
                deltaX = -(x + right);

            if (y + deltaY > 0)
                deltaY = -absoluteOffsetY;
            else if (y + deltaY < -bottom)
                deltaY = -(y + bottom);
        }
        postTranslate(deltaX, deltaY);
        checkSiding();
    }

	/**
	 * 取得平移�?
	 * @return 平移�?
	 */
	public PointF getAbsoluteOffset (){
		fillAbsoluteOffset();
		return new PointF(absoluteOffsetX, absoluteOffsetY);
	}

	public float getScale(){
		return saveScale;
	}

    private void checkSiding() {
        fillAbsoluteOffset();
		final float x = absoluteOffsetX;
		final float y = absoluteOffsetY;
        float scaleWidth = Math.round(origWidth * saveScale);
        float scaleHeight = Math.round(origHeight * saveScale);
        onLeftSide = onRightSide = onTopSide = onBottomSide = false;
        if (-x < 10.0f ) onLeftSide = true;
        if ((scaleWidth >= viewWidth && (x + scaleWidth - viewWidth) < 10) ||
            (scaleWidth <= viewWidth && -x + scaleWidth <= viewWidth)) onRightSide = true;
        if (-y < 10.0f) onTopSide = true;
        if (Math.abs(-y + viewHeight - scaleHeight) < 10.0f) onBottomSide = true;
    }
    
    private void calcPadding(){
        right = viewWidth * saveScale - viewWidth - (2 * redundantXSpace * saveScale);
        bottom = viewHeight * saveScale - viewHeight - (2 * redundantYSpace * saveScale);
    }
    
    private void fillAbsoluteOffset (){
        imageUsingMatrix.getValues(matrixValues);
        absoluteOffsetX = matrixValues[Matrix.MTRANS_X];
        absoluteOffsetY = matrixValues[Matrix.MTRANS_Y];
//		System.out.println(String.format("::: 图像的绝对偏移坐�? (%f,%f) ::::",absoluteOffsetX,absoluteOffsetY));
	}
    
    @Override
    public void setImageBitmap(Bitmap bm) {
        bmWidth = bm.getWidth();
        bmHeight = bm.getHeight();
        super.setImageBitmap(bm);
    }


    
    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		float newWidth = MeasureSpec.getSize(widthMeasureSpec);
		float newHeight= MeasureSpec.getSize(heightMeasureSpec);

		viewWidth = newWidth;
        viewHeight = newHeight;

		initSize();
		calcPadding();
    }

	private void initSize(){
		// 初始时，图片与View边缘之间的距�?
		redundantYSpace = viewHeight - (saveScale * bmHeight) ;
		redundantXSpace = viewWidth - (saveScale * bmWidth);

		redundantYSpace /= (float)2;
		redundantXSpace /= (float)2;

		origWidth = viewWidth - 2 * redundantXSpace;
		origHeight = viewHeight - 2 * redundantYSpace;
	}

	private double distanceBetween(PointF left, PointF right){
        return Math.sqrt(Math.pow(left.x - right.x, 2) + Math.pow(left.y - right.y, 2));
    }

	/**
	 * 计算两个手指之间的距�?
	 * @param event 触摸整的
	 * @return 距离
	 */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

	public void postScaleToImage(float scaleFactor,float scaleFocusX,float scaleFocusY){
		float origScale = saveScale;
		saveScale *= scaleFactor;
		if (saveScale > maxScale) {
			saveScale = maxScale;
			scaleFactor = maxScale / origScale;
		} else if (saveScale < minScale) {
			saveScale = minScale;
			scaleFactor = minScale / origScale;
		}
		right = viewWidth * saveScale - viewWidth - (2 * redundantXSpace * saveScale);
		bottom = viewHeight * saveScale - viewHeight - (2 * redundantYSpace * saveScale);

		// 显示的图片比源图片要小时进行缩放
		if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight) {
			final float scaleCenterX = viewWidth / 2;
			final float scaleCenterY = viewHeight / 2;
			postScale(scaleFactor, scaleCenterX, scaleCenterY);

			//在边缘时缩小图片的平移修�?
			if (scaleFactor < 1) {
				fillAbsoluteOffset();
				final float x = absoluteOffsetX;
				final float y = absoluteOffsetY;
				if (scaleFactor < 1) {
					if (Math.round(origWidth * saveScale) < viewWidth) {
						float deltaX = 0,deltaY = 0;
						if (y < -bottom){
							deltaY = -(y + bottom);
							postTranslate(deltaX, deltaY);
						}
						else if (y > 0){
							deltaY = -y;
							postTranslate(deltaX, deltaY);
						}
					} else {
						float deltaX = 0,deltaY = 0;
						if (x < -right){
							deltaX = -(x + right);
							postTranslate(deltaX, deltaY);
						}
						else if (x > 0){
							deltaX = -x;
							postTranslate(deltaX, deltaY);
						}
					}
				}
			}
		} else {
			// 图片已经被放大，以触摸点为中心，缩放图片�?
			postScale(scaleFactor, scaleFocusX, scaleFocusY);
			fillAbsoluteOffset();
			final float x = absoluteOffsetX;
			final float y = absoluteOffsetY;

			// 缩小图片时，如果在图片边缘缩小，为避免边缘出现空白，对图片进行平移处�?
			if (scaleFactor < 1) {
				float deltaX = 0,deltaY = 0;
				if (x < -right){
					deltaX = -(x + right);
					deltaY = 0;
					postTranslate(deltaX, deltaY);
				}
				else if (x > 0){
					deltaX = -x;
					deltaY = 0;
					postTranslate(deltaX, deltaY);
				}
				if (y < -bottom){
					deltaX = 0;
					deltaY = -(y + bottom);
					postTranslate(deltaX, deltaY);
				}
				else if (y > 0){
					deltaX = 0;
					deltaY = -y;
					postTranslate(deltaX, deltaY);
				}
			}
		}
		postInvalidate();
	}
    
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            postScaleToImage(scaleFactor,detector.getFocusX(),detector.getFocusY());
            return true;
        }
    }
}