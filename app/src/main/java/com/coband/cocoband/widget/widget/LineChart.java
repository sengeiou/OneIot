package com.coband.cocoband.widget.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.coband.watchassistant.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ivan on 2017/5/5.
 */

public class LineChart extends View {

    private static final String TAG = "LineChart";
    private Context context;
    private List<Data> mDataList = new ArrayList<>();

    private TextPaint textPaint;
    private boolean firstVisible = true;
    private int width;//控件宽度
    private int height;//控件高度

    private float textSize;
    private int textColor;
    private Rect rect = new Rect();

    private int textHeight = 0;
    private int textWidth = 0;

    private Paint linePaint;
    private float lineWidthDP = 2f;

    private float maxValue;

    private Path linePath;

    private Path regionPath;
    private Paint regionPaint;

    private List<PointF> pointList = new ArrayList<>();
    private int pointAndLineColor = Color.parseColor("#ffb400");//锚点颜色
    private float pointWidthDefault = 20f;
    private Paint pointPaint;

    private String mEmpty;
    private Paint emptyTextPaint;
    private int paddingTop = 20;
    private int paddingLeft = 50;
    private int paddingRight = 50;


    public LineChart(Context context) {
        this(context, null);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setWillNotDraw(false);
        setClickable(true);
        initAttrs(attrs);//初始化属性
        initPaint();//初始化画笔
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);//抗锯齿
        linePaint.setStyle(Paint.Style.STROKE);//STROKE描边FILL填充
        linePaint.setColor(pointAndLineColor);
        linePaint.setStrokeWidth(dip2px(lineWidthDP));//边框宽度

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(pointAndLineColor);
        pointPaint.setStrokeWidth(dip2px(pointWidthDefault));

        emptyTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        emptyTextPaint.setTextSize(12f);
        emptyTextPaint.setColor(textColor);
        emptyTextPaint.setTextAlign(Paint.Align.CENTER);

        regionPaint = new Paint();
        regionPaint.setAntiAlias(true);//抗锯齿
        regionPaint.setStyle(Paint.Style.FILL_AND_STROKE);//STROKE描边FILL填充
        regionPaint.setColor(pointAndLineColor);
        regionPaint.setStrokeWidth(dip2px(lineWidthDP));//边框宽度
        regionPaint.setAlpha(16);
    }

    private int dip2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 初始化属性
     *
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.LineChart, 0, 0);
        textSize = t.getDimension(R.styleable.LineChart_chartLabelSize, 20);
        textColor = t.getColor(R.styleable.LineChart_chartLabelColor,
                context.getResources().getColor(android.R.color.darker_gray));
        t.recycle();
    }


    public void setEmptyText(String emptyText) {
        mEmpty = emptyText;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (firstVisible) {//第一次绘制的时候得到控件 宽高；
            width = getWidth();
            height = getHeight();
            firstVisible = false;
        }

        if (!mDataList.isEmpty()) {
            drawLabel(canvas);
            setupLineAndPoints();
            canvas.drawPath(linePath, linePaint);
            drawPoint(canvas);
            drawRegion(canvas);
        } else if (mEmpty != null) {
            drawEmptyText(canvas);
        }
    }

    private void drawRegion(Canvas canvas) {
        if (mDataList.size() > 1) {
            Log.d(TAG, "pointList size >>> " + pointList.size());
            regionPath.moveTo(pointList.get(0).x, height - textHeight - 10);
            for (PointF pointF : pointList) {
                regionPath.lineTo(pointF.x, pointF.y);
            }
            regionPath.lineTo(pointList.get(pointList.size() - 1).x, height - textHeight - 10);
            regionPath.close();

            canvas.drawPath(regionPath, regionPaint);
        }
    }

    public void setLabelAppearance(float size, int color, Typeface typeface) {
        this.textSize = size;
        this.textColor = color;

        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);

        if (typeface != null) {
            textPaint.setTypeface(typeface);
        }
    }

    private void drawEmptyText(Canvas canvas) {
        canvas.drawText(mEmpty, width / 2f, height / 2f, emptyTextPaint);
    }

    private void drawPoint(Canvas canvas) {
        for (int i = 0; i < pointList.size(); i++) {
            PointF point = pointList.get(i);
            canvas.drawCircle(point.x, point.y, pointWidthDefault / 2, pointPaint);
        }
    }


    private void setupLineAndPoints() {
        float lineChartHeight = height - textHeight - paddingTop;
        float lineChartStartY = paddingTop;
        float perHeight = lineChartHeight / maxValue;

        pointList.clear();

        if (mDataList.size() == 1) {
            PointF point = new PointF();
            point.set(width / 2f, lineChartStartY + lineChartHeight
                    - mDataList.get(0).getValue() * perHeight);
            pointList.add(point);
        } else {
            float perWidth = ((float) width - paddingLeft - paddingRight) / (mDataList.size() - 1);
            linePath.moveTo(paddingLeft,
                    lineChartStartY + lineChartHeight - mDataList.get(0).getValue() * perHeight - 10);
            PointF prePoint = new PointF();
            prePoint.set(paddingLeft, lineChartStartY + lineChartHeight
                    - mDataList.get(0).getValue() * perHeight - 10);
            pointList.add(prePoint);
            for (int i = 1; i < mDataList.size(); i++) {
                Data data = mDataList.get(i);
                linePath.lineTo(perWidth * i + paddingLeft,
                        lineChartStartY + lineChartHeight - data.getValue() * perHeight - 10);
                PointF point = new PointF();
                point.set(perWidth * i + paddingLeft, lineChartStartY + lineChartHeight
                        - data.getValue() * perHeight - 10);
                pointList.add(point);
            }
        }
    }

    private void drawLabel(Canvas canvas) {
        if (mDataList.size() == 1) {
            String label = mDataList.get(0).getLabel();
            canvas.drawText(label, width / 2f, height, textPaint);
        } else {
            float perLabelRectWidth = ((float) width - paddingLeft - paddingRight) / (mDataList.size() - 1);
            for (int i = 0; i < mDataList.size(); i++) {
                String s = mDataList.get(i).getLabel();
                textPaint.getTextBounds(s, 0, s.length(), rect);
                textHeight = rect.height();
                textWidth = rect.width();
                if (i == mDataList.size() - 1) {
                    canvas.drawText(s, i * perLabelRectWidth + paddingLeft, height, textPaint);
                } else {
                    canvas.drawText(s, i * perLabelRectWidth + paddingLeft, height, textPaint);
                }
            }
        }
    }


    public void setData(List<Data> dataList) {
        if (linePath != null) {
            linePath.reset();
        }

        if (regionPath != null) {
            regionPath.reset();
        }

        pointList.clear();

        if (dataList.isEmpty()) {
            mDataList.clear();
            invalidate();
            return;
        }

        mDataList = dataList;

        maxValue = Collections.max(mDataList, new Comparator<Data>() {
            @Override
            public int compare(Data o1, Data o2) {
                return Float.compare(o1.getValue(), o2.getValue());
            }
        }).getValue();

        linePath = new Path();

        regionPath = new Path();

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public static class Data {
        private float value;
        private String label;

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}
