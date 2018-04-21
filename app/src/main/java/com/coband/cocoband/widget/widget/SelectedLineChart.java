package com.coband.cocoband.widget.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.coband.App;
import com.coband.watchassistant.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ivan on 2017/5/5.
 */

public class SelectedLineChart extends View {

    private static final String TAG = "SelectedLineChart";
    private Context context;
    private List<Data> mDataList = new ArrayList<>();

    private int seeSize = 5;//可见个数

    private int anInt;//每个字母所占的大小；
    private TextPaint textPaint;
    private boolean firstVisible = true;
    private int width;//控件宽度
    private int height;//控件高度
    private Paint selectedPaint;//被选中文字的画笔
    private int n;
    private float downX;
    private float anOffset;
    private float selectedTextSize;
    private int selectedColor;
    private float textSize;
    private int textColor;
    private Rect rect = new Rect();

    private int textWidth = 0;
    private int textHeight = 0;
    private int centerTextHeight = 0;


    private Paint linePaint;
    private float lineWidthDP = 2f;

    private float maxValue;
    private float minValue;

    private Path linePath;

    private List<PointF> pointList = new ArrayList<>();
    private int pointAndLineColor = Color.parseColor("#ffb400");//锚点颜色
    private float pointWidthDefault = 12f;
    private Paint pointPaint;

    private OnSelectedChangedListener listener;
    private String mEmpty;
    private Paint emptyTextPaint;
    private int paddingTop = 20;

    private Paint centerLinePaint;

    public interface OnSelectedChangedListener {
        void onSelectedPositionChanged(int position);
    }

    public void setOnSelectedPositionChanged(OnSelectedChangedListener listener) {
        this.listener = listener;
    }


    public SelectedLineChart(Context context) {
        this(context, null);
    }

    public SelectedLineChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectedLineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        selectedPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        selectedPaint.setColor(selectedColor);
        selectedPaint.setTextSize(selectedTextSize);

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
        emptyTextPaint.setTextSize(com.coband.common.utils.Utils.dp2px(12f, App.getContext()));
        emptyTextPaint.setColor(textColor);
        emptyTextPaint.setTextAlign(Paint.Align.CENTER);

        centerLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerLinePaint.setStrokeWidth(1);
        centerLinePaint.setColor(Color.parseColor("#A0A0A0"));
        centerLinePaint.setPathEffect(new DashPathEffect(new float[]{10f, 10f}, 0));
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
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.SelectedLineChart, 0, 0);
        //两种字体颜色和字体大小
        seeSize = t.getInteger(R.styleable.SelectedLineChart_seeSize, 5);
        selectedTextSize = t.getDimension(R.styleable.SelectedLineChart_selectedTextSize, 20);
        selectedColor = t.getColor(R.styleable.SelectedLineChart_selectedTextColor,
                context.getResources().getColor(android.R.color.black));
        textSize = t.getDimension(R.styleable.SelectedLineChart_textSize, 20);
        textColor = t.getColor(R.styleable.SelectedLineChart_textColor,
                context.getResources().getColor(android.R.color.darker_gray));
        t.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("action", "onTouchEvent: " + event.getAction());
        if (mDataList.isEmpty()) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();//获得点下去的x坐标
                break;
            case MotionEvent.ACTION_MOVE://复杂的是移动时的判断
                float scrollX = event.getX();

                if (n != 0 && n != mDataList.size() - 1)
                    anOffset = scrollX - downX;//滑动时的偏移量，用于计算每个是数据源文字的坐标值
                else {
                    anOffset = (float) ((scrollX - downX) / 1.5);//当滑到两端的时候添加一点阻力
                }

                if (scrollX > downX) {
                    //向右滑动，当滑动距离大于每个单元的长度时，则改变被选中的文字。
                    if (scrollX - downX >= anInt) {
                        if (n > 0) {
                            anOffset = 0;
                            n = n - 1;
                            downX = scrollX;
                            if (listener != null) {
                                listener.onSelectedPositionChanged(n);
                            }
                        }
                    }
                } else {

                    //向左滑动，当滑动距离大于每个单元的长度时，则改变被选中的文字。
                    if (downX - scrollX >= anInt) {
                        if (n < mDataList.size() - 1) {
                            anOffset = 0;
                            n = n + 1;
                            downX = scrollX;
                            if (listener != null) {
                                listener.onSelectedPositionChanged(n);
                            }
                        }
                    }
                }
                linePath.reset();
                pointList.clear();
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                //抬起手指时，偏移量归零，相当于回弹。
                anOffset = 0;
                linePath.reset();
                pointList.clear();
                invalidate();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
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
            anInt = width / seeSize;
            firstVisible = false;
        }

        if (!mDataList.isEmpty()) {
            setupLineAndPoints();
            drawLabel(canvas);
            canvas.drawPath(linePath, linePaint);
            drawPoint(canvas);
            canvas.drawLine(width / 2f, paddingTop, width / 2f, height - textHeight, centerLinePaint);
        } else if (mEmpty != null) {
            drawEmptyText(canvas);
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
        linePath.moveTo((0 - n) * anInt + getWidth() / 2 + anOffset, lineChartStartY + lineChartHeight -
                mDataList.get(0).getValue() * perHeight);
        PointF prePoint = new PointF();
        prePoint.set((0 - n) * anInt + getWidth() / 2 + anOffset, lineChartStartY + lineChartHeight -
                mDataList.get(0).getValue() * perHeight);
        pointList.add(prePoint);

        for (int i = 1; i < mDataList.size(); i++) {
            Data data = mDataList.get(i);
            linePath.lineTo((i - n) * anInt + getWidth() / 2 + anOffset,
                    lineChartStartY + lineChartHeight - data.getValue() * perHeight);
            PointF point = new PointF();
            point.set((i - n) * anInt + getWidth() / 2 + anOffset, lineChartStartY + lineChartHeight
                    - data.getValue() * perHeight);
            pointList.add(point);
        }
    }

    private void drawLabel(Canvas canvas) {
        if (n >= 0 && n <= mDataList.size() - 1) {//加个保护；防止越界

            String s = mDataList.get(n).getLabel();//得到被选中的文字
            /**
             * 得到被选中文字 绘制时所需要的宽高
             */
            selectedPaint.getTextBounds(s, 0, s.length(), rect);
            //3从矩形区域中读出文本内容的宽高
            int centerTextWidth = rect.width();
            centerTextHeight = rect.height();
            canvas.drawText(mDataList.get(n).getLabel(), getWidth() / 2 - centerTextWidth / 2 + anOffset,
                    height, selectedPaint);//绘制被选中文字，注意点是y坐标

            for (int i = 0; i < mDataList.size(); i++) {//遍历strings，把每个地方都绘制出来，
                Data data = mDataList.get(i);
                textPaint.getTextBounds(data.getLabel(), 0, data.getLabel().length(), rect);
                textHeight = rect.height();
                if (i != n)
                    canvas.drawText(data.getLabel(), pointList.get(i).x, height, textPaint);

            }
        }
    }

    /**
     * 改变中间可见文字的数目
     *
     * @param seeSizes 可见数
     */
    public void setSeeSize(int seeSizes) {
        if (seeSize > 0) {
            seeSize = seeSizes;
            invalidate();
        }
    }


    public void setData(List<Data> dataList) {
        if (linePath != null) {
            linePath.reset();
        }

        pointList.clear();

        if (dataList.isEmpty()) {
            mDataList.clear();
            invalidate();
            return;
        }

        mDataList = dataList;
        n = dataList.size() - 1;

        maxValue = Collections.max(mDataList, new Comparator<Data>() {
            @Override
            public int compare(Data o1, Data o2) {
                return Float.compare(o1.getValue(), o2.getValue());
            }
        }).getValue();

        minValue = Collections.min(mDataList, new Comparator<Data>() {
            @Override
            public int compare(Data o1, Data o2) {
                return Float.compare(o1.getValue(), o2.getValue());
            }
        }).getValue();

        Log.d(TAG, "maxValue >>>> " + maxValue + " minValue >>>> " + minValue);

        linePath = new Path();

        invalidate();
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
