package com.coband.cocoband.widget.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.coband.watchassistant.R;

/**
 * Created by imco on 7/19/16.
 */
public class CardTimeLineView extends View {
    private Paint mPaint;
    private RectF mRect;

    private int width;
    private int height;

    public CardTimeLineView(Context context) {
        this(context, null);
    }

    public CardTimeLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardTimeLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mRect = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRect.set(0, 0, width, height / 3f);
        mPaint.setColor(getResources().getColor(R.color.gray));
        canvas.drawRect(mRect, mPaint);

        mPaint.setColor(getResources().getColor(R.color.gray_light));
        mRect.set(0, height / 3f * 2, width, height);
        canvas.drawRect(mRect, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }
}
