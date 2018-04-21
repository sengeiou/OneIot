
package com.ivan.circleprogressview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class CircleProgressView extends View {
    private static final String TAG = "CircleTimerView";

    // Status
    private static final String INSTANCE_STATUS = "instance_status";
    private static final String STATUS_RADIAN = "status_radian";

    // Default dimension in dp/pt
    private static final int DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE = 5;
    private static final int DEFAULT_LINE_LENGTH = 10;
    private static final int DEFAULT_LINE_WIDTH = 1;
    private static final int DEFAULT_CIRCLE_STROKE_WIDTH = 3;
    private static final int DEFAULT_MIDDLE_TEXT_SIZE = 12;
    private static final int DEFAULT_TOP_TEXT_SIZE = 12;
    private static final int DEFAULT_BOTTOM_TEXT_SIZE = 12;


    // Default color
    private static final int DEFAULT_CIRCLE_COLOR = 0xFFB6B6B6;
    private static final int DEFAULT_LINE_COLOR = 0xFFB6B6B6;
    private static final int DEFAULT_HIGHLIGHT_LINE_COLOR = 0xFF242424;
    private static final int DEFAULT_MIDDLE_TEXT_COLOR = 0xFF000000;
    private static final int DEFAULT_TOP_TEXT_COLOR = 0xFF000000;
    private static final int DEFAULT_BOTTOM_TEXT_COLOR = 0xFF000000;

    private static final int DEFAULT_CIRCLE_HIGHLIGHT_COLOR = 0xFF242424;

    private static final int DEFAULT_LINE_COUNT = 180;

    private static final float DEFAULT_TARGET = 100.0f;


    // Paint
    private Paint circlePaint;
    private Paint highlightLinePaint;
    private Paint linePaint;
    private Paint middleTextPaint;
    private Paint topTextPaint;
    private Paint bottomTextPaint;
    private Paint progressOvalPaint;

    // Dimension
    private int gapBetweenCircleAndLine;
    private int lineLength;
    private float lineWidth;

    // Color
    private int circleColor;
    private int lineColor;
    private int lineHighlightColor;
    private int circleHighlightColor;

    // Parameters
    private float cx;
    private float cy;
    private float radius;
    private float currentRadian = 0;


    private float target = DEFAULT_TARGET;

    private float currentCount;

    private int lineCount;

    private RectF mProgressOval;

    private boolean middleTextVisible;
    private int middleTextSize;
    private int middleTextColor;

    private boolean topTextVisible;
    private int topTextSize;
    private int topTextColor;
    private String topText;

    private boolean bottomTextVisible;
    private int bottomTextSize;
    private int bottomTextColor;
    private String bottomText;

    private String middleText;

    private boolean lineVisible;
    private ValueAnimator animator;
    private Drawable originIcon;
    private Drawable animateIcon;
    private Drawable icon;
    private int originIconWidth;
    private int originIconHeight;
    private Paint mBPMPaint;
    private static final int OFFSET = 10;
    private static final String BPM = "BPM";

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context) {
        this(context, null);
    }

    private void initialize(Context context, AttributeSet attrs) {
        Log.d(TAG, "initialize");

        mProgressOval = new RectF();

        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView, 0, 0);

        lineCount = t.getInt(R.styleable.CircleProgressView_lineCount, DEFAULT_LINE_COUNT);
        lineLength = t.getDimensionPixelSize(R.styleable.CircleProgressView_lineLength, DEFAULT_LINE_LENGTH);
        lineColor = t.getColor(R.styleable.CircleProgressView_lineColor, DEFAULT_LINE_COLOR);
        lineHighlightColor = t.getColor(R.styleable.CircleProgressView_lineHighlightColor, DEFAULT_HIGHLIGHT_LINE_COLOR);
        lineWidth = t.getDimensionPixelSize(R.styleable.CircleProgressView_lineWidth, DEFAULT_LINE_WIDTH);
        circleColor = t.getColor(R.styleable.CircleProgressView_circleColor, DEFAULT_CIRCLE_COLOR);
        gapBetweenCircleAndLine = t.getDimensionPixelSize(R.styleable.CircleProgressView_gapBetweenCircleAndLine,
                DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE);
        circleHighlightColor = t.getColor(R.styleable.CircleProgressView_circleHighlightColor, DEFAULT_CIRCLE_HIGHLIGHT_COLOR);
        middleText = t.getString(R.styleable.CircleProgressView_middleText);
        middleTextVisible = t.getBoolean(R.styleable.CircleProgressView_middleTextVisible, true);
        middleTextSize = t.getDimensionPixelSize(R.styleable.CircleProgressView_middleTextSize, DEFAULT_MIDDLE_TEXT_SIZE);
        middleTextColor = t.getColor(R.styleable.CircleProgressView_middleTextColor, DEFAULT_MIDDLE_TEXT_COLOR);
        topTextColor = t.getColor(R.styleable.CircleProgressView_topTextColor, DEFAULT_TOP_TEXT_COLOR);
        topTextSize = t.getDimensionPixelSize(R.styleable.CircleProgressView_topTextSize, DEFAULT_TOP_TEXT_SIZE);
        topTextVisible = t.getBoolean(R.styleable.CircleProgressView_topTextVisible, false);
        topText = t.getString(R.styleable.CircleProgressView_topText);
        bottomText = t.getString(R.styleable.CircleProgressView_bottomText);
        bottomTextVisible = t.getBoolean(R.styleable.CircleProgressView_bottomTextVisible, false);
        bottomTextColor = t.getColor(R.styleable.CircleProgressView_bottomTextColor, DEFAULT_BOTTOM_TEXT_COLOR);
        bottomTextSize = t.getDimensionPixelSize(R.styleable.CircleProgressView_bottomTextSize, DEFAULT_BOTTOM_TEXT_SIZE);

        lineVisible = t.getBoolean(R.styleable.CircleProgressView_lineVisible, true);


        // Init all paints
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlightLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        middleTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressOvalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        topTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bottomTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


        // CirclePaint
        circlePaint.setColor(circleColor);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(DEFAULT_CIRCLE_STROKE_WIDTH);


        // LinePaint
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineWidth);

        // HighlightLinePaint
        highlightLinePaint.setColor(lineHighlightColor);
        highlightLinePaint.setStrokeWidth(lineWidth);

        // Middle text Paint
        middleTextPaint.setColor(middleTextColor);
//        middleTextPaint.setTextSize(middleTextSize);
        middleTextPaint.setTextAlign(Paint.Align.CENTER);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, middleTextSize, middleTextPaint);

        // Top text Paint
        topTextPaint.setColor(topTextColor);
//        topTextPaint.setTextSize(topTextSize);
        topTextPaint.setTextAlign(Paint.Align.CENTER);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, topTextSize, topTextPaint);

        // Bottom text Paint
        bottomTextPaint.setColor(bottomTextColor);
//        bottomTextPaint.setTextSize(bottomTextSize);
        bottomTextPaint.setTextAlign(Paint.Align.CENTER);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, bottomTextSize, bottomTextPaint);

        //Progress Paint;
        progressOvalPaint.setStyle(Paint.Style.STROKE);
        progressOvalPaint.setStrokeWidth(DEFAULT_CIRCLE_STROKE_WIDTH);
        progressOvalPaint.setColor(circleHighlightColor);


        originIcon = t.getDrawable(R.styleable.CircleProgressView_progressIcon);
        if (originIcon != null) {
            originIconWidth = originIcon.getIntrinsicWidth();
            originIconHeight = originIcon.getIntrinsicHeight();
            icon = originIcon;
            animateIcon = t.getDrawable(R.styleable.CircleProgressView_animateIcon);
        }

        mBPMPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14, mBPMPaint);
        mBPMPaint.setColor(Color.WHITE);

        t.recycle();
    }

    public void setTextSize(int unit, float size, Paint paint) {
        Context c = getContext();
        Resources r;

        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();

        paint.setTextSize(TypedValue.applyDimension(
                unit, size, r.getDisplayMetrics()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Content
        canvas.drawCircle(cx, cy, radius - lineLength - gapBetweenCircleAndLine, circlePaint);

        canvas.save();
        if (currentRadian != 0) {
            mProgressOval.set(
                    gapBetweenCircleAndLine + lineLength,
                    gapBetweenCircleAndLine + lineLength,
                    cx + radius - gapBetweenCircleAndLine - lineLength,
                    cy + radius - gapBetweenCircleAndLine - lineLength);
            canvas.drawArc(mProgressOval, -90, (float) Math.toDegrees(currentRadian), false, progressOvalPaint);
        }

        canvas.restore();

        if (lineVisible) {
            canvas.save();
            for (int i = 0; i < lineCount; i++) {
                canvas.rotate(360.0f / lineCount, cx, cy);
                if (360.0f / lineCount * i < Math.toDegrees(currentRadian)) {

                    canvas.drawLine(cx, getMeasuredHeight() / 2 - radius + DEFAULT_CIRCLE_STROKE_WIDTH / 2, cx,
                            getMeasuredHeight() / 2 - radius + DEFAULT_CIRCLE_STROKE_WIDTH / 2 + lineLength,
                            highlightLinePaint);
                } else {
                    canvas.drawLine(cx, getMeasuredHeight() / 2 - radius + DEFAULT_CIRCLE_STROKE_WIDTH / 2, cx,
                            getMeasuredHeight() / 2 - radius + DEFAULT_CIRCLE_STROKE_WIDTH / 2 + lineLength,
                            linePaint);
//                }
                }
            }

            canvas.restore();
        }


        if (middleTextVisible && middleText != null) {
            if (icon != null) {
                canvas.drawText(middleText, cx - (icon.getIntrinsicWidth() + OFFSET) / 2, cy
                        + getFontHeight(middleTextPaint) / 2, middleTextPaint);
            } else {
                canvas.drawText(middleText, cx, cy + getFontHeight(middleTextPaint) / 2, middleTextPaint);
            }
        }

        if (topTextVisible && topText != null) {
            canvas.drawText(topText, cx, (cy - (radius - gapBetweenCircleAndLine) / 2)
                    + getFontHeight(topTextPaint) / 2, topTextPaint);
        }

        if (bottomTextVisible && bottomText != null) {
            canvas.drawText(bottomText, cx, cy + (radius - gapBetweenCircleAndLine) / 2, bottomTextPaint);
        }

        if (icon != null) {
            icon.setBounds((int) ((cx + (getFontWidth(middleText, middleTextPaint) + OFFSET) / 2 -
                            icon.getIntrinsicWidth() / 2)),
                    (int) ((cy - (icon.getIntrinsicHeight() + getFontHeight(mBPMPaint)) / 2)),
                    (int) ((cx + getFontWidth(middleText, middleTextPaint) / 2 + icon.getIntrinsicWidth() / 2)),
                    (int) (((cy - (icon.getIntrinsicHeight() + getFontHeight(mBPMPaint)) / 2) +
                            icon.getIntrinsicHeight())));
            icon.draw(canvas);

            canvas.drawText(BPM, cx + (getFontWidth(middleText, middleTextPaint)) / 2 - originIconWidth / 2,
                    cy - (icon.getIntrinsicHeight() + getFontHeight(mBPMPaint)) / 2 + originIconHeight + 30, mBPMPaint);
        }

        super.onDraw(canvas);
    }

    private float getFontWidth(String text, Paint paint) {
        Rect rect = new Rect();
        if (text == null) {
            return 0;
        }

        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    private float getFontHeight(Paint paint) {
        // FontMetrics sF = paint.getFontMetrics();
        // return sF.descent - sF.ascent;
        Rect rect = new Rect();
        paint.getTextBounds("1", 0, 1, rect);
        return rect.height();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Ensure width = height
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        this.cx = width / 2;
        this.cy = height / 2;
        // Radius
        this.radius = width / 2 - DEFAULT_CIRCLE_STROKE_WIDTH / 2;
        setMeasuredDimension(width, height);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.d(TAG, "onSaveInstanceState");
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        bundle.putFloat(STATUS_RADIAN, currentRadian);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.d(TAG, "onRestoreInstanceState");
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            currentRadian = bundle.getFloat(STATUS_RADIAN);
            currentCount = (float) (60 / (2 * Math.PI) * currentRadian * 60);
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void setCurrentCount(float currentCount) {
        if (currentCount < 0) {
            Log.e(TAG, "currentCount can't less more 0");
        }

        this.currentCount = currentCount;

        if (currentCount < target) {
            currentRadian = (float) (currentCount * (2 * Math.PI) / target);
        } else {
            currentRadian = (float) (target * (2 * Math.PI) / target);
        }

        invalidate();
    }

    public void setProgress(float progress) {
        if (progress > 1.0f) {
            progress = 1.0f;
        }

        if (progress < 0.0f) {
            progress = 0.0f;
        }

        currentRadian = (float) (progress * 2 * Math.PI);
        invalidate();
    }

    public void setTarget(float target) {
        this.target = target;
        invalidate();
    }

    public float getTarget() {
        return target;
    }

    public float getCurrentCount() {
        return currentCount;
    }

    public void setMiddleTextColor(int color) {
        middleTextPaint.setColor(color);
        invalidate();
    }

    public void setHighLightLineColor(int color) {
        highlightLinePaint.setColor(color);
        invalidate();
    }

    public void setLineColor(int color) {
        linePaint.setColor(color);
        invalidate();
    }

    public void setCircleColor(int color) {
        circlePaint.setColor(color);
        invalidate();
    }

    public void setProgressOvalColor(int color) {
        progressOvalPaint.setColor(color);
    }

    public void setMiddleText(String text) {
        middleText = text;
        invalidate();
    }

    public String getMiddleText() {
        return middleText;
    }

    public void setBottomText(String text) {
        bottomText = text;
        invalidate();
    }

    public void setTopText(String text) {
        topText = text;
        invalidate();
    }

    public void startAnimation() {
        if (originIcon == null || animateIcon == null) {
            return;
        }

        animator = ValueAnimator.ofInt(0, 2);
        animator.setDuration(800);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (value % 2 == 0) {
                    icon = originIcon;
                } else {
                    icon = animateIcon;
                }

                invalidate();
            }


        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                icon = originIcon;
                invalidate();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator.setRepeatCount(-1);
        animator.start();
    }

    public void stopAnimate() {
        if (animator != null) {
            animator.cancel();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null) {
            animator.removeAllUpdateListeners();
            animator.cancel();
            animator = null;
        }
    }
}
