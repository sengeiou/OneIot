package com.coband.cocoband.widget.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.coband.cocoband.mvp.model.bean.SleepBarEntry;
import com.coband.cocoband.mvp.model.bean.SleepDayDateBean;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.Logger;
import com.coband.watchassistant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imco on 4/29/16.
 */
public class SleepBarChart extends View {

    private static final String TAG = "SleepBarChart";
    private List<SleepBarEntry> entries = new ArrayList<>();
    private List<String> labels = new ArrayList<>();
    private int width;
    private int height;
    private Paint mRectPaint;
    private Paint mLabelPaint;
    private Paint mLinePaint;
    private Paint mEmptyPaint;

    private static final int DEEP = 0;
    private static final int LIGHT = 1;
    private static final int AWAKE = 2;

    private static final int DEFAULT_DEEP_COLOR = Color.parseColor("#5078C8");
    private static final int DEFAULT_LIGHT_COLOR = Color.parseColor("#82AAFF");
    private static final int DEFAULT_AWAKE_COLOR = Color.parseColor("#F0F2FF");
    private static final int DEFAULT_EMPTY_COLOR = Color.parseColor("#A0A0A0");
    private static final int DEFAULT_LABEL_COLOR = Color.parseColor("#c8c8c8");
    private static final int DEFAULT_LABEL_SIZE = 8;
    private static final int DEFAULT_EMPTY_TEXT_SIZE = 10;
    private static final String DEFAULT_EMPTY_TEXT = "No data";


    private int deepColor;
    private int lightColor;
    private int awakeColor;
    private float widthOffset;

    private String mEmptyText;

    private int labelColor;
    private int labelSize;
    private Typeface labelType;
    private float labelHeight;


    public SleepBarChart(Context context) {
        this(context, null);
    }

    public SleepBarChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SleepBarChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.SleepBarChart, 0, 0);
        deepColor = t.getColor(R.styleable.SleepBarChart_deepColor, DEFAULT_DEEP_COLOR);
        lightColor = t.getColor(R.styleable.SleepBarChart_lightColor, DEFAULT_LIGHT_COLOR);
        awakeColor = t.getColor(R.styleable.SleepBarChart_awakeColor, DEFAULT_AWAKE_COLOR);
        labelColor = t.getColor(R.styleable.SleepBarChart_labelColor, DEFAULT_LABEL_COLOR);
        labelSize = t.getDimensionPixelSize(R.styleable.SleepBarChart_labelSize, dip2px(DEFAULT_LABEL_SIZE));
        mEmptyText = t.getString(R.styleable.SleepBarChart_emptyText);

        mRectPaint = new Paint();
        mRectPaint.setColor(labelColor);

        mLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLabelPaint.setColor(labelColor);
        mLabelPaint.setTextAlign(Paint.Align.CENTER);
        mLabelPaint.setTextSize(labelSize);
        Paint.FontMetrics fontMetrics = mLabelPaint.getFontMetrics();
        labelHeight = fontMetrics.descent - fontMetrics.ascent;

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.DKGRAY);

        mEmptyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEmptyPaint.setColor(DEFAULT_EMPTY_COLOR);
        mEmptyPaint.setTextAlign(Paint.Align.CENTER);
        mEmptyPaint.setTextSize(dip2px(DEFAULT_EMPTY_TEXT_SIZE));

        t.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        widthOffset = 0.0f;
        super.onDraw(canvas);
        int labelWidth;
        if (!labels.isEmpty()) {
            labelWidth = width - getTextWidth(labels.get(labels.size() - 1), mLabelPaint);
        } else {
            if (mEmptyText == null) {
                mEmptyText = DEFAULT_EMPTY_TEXT;
            }
            canvas.drawText(mEmptyText, width / 2f, height / 2f, mEmptyPaint);
            return;
        }

        if (entries.isEmpty()) {
            return;
        }

        for (int i = 0; i < labels.size(); i++) {
            String label = labels.get(i);
            if (i == 0) {
                canvas.drawText(label, getTextWidth(label, mLabelPaint), height - 3, mLabelPaint);
            } else if (i == labels.size() - 1) {
                canvas.drawText(label, width - getTextWidth(label, mLabelPaint), height - 3, mLabelPaint);
            } else {
                canvas.drawText(label,
                        (labelWidth - getTextWidth(label, mLabelPaint)) / 3 * i + getTextWidth(label, mLabelPaint),
                        height - 3,
                        mLabelPaint);
            }
        }

        for (SleepBarEntry entry : entries) {
            float barSpace = entry.getSpacePrecent() * width;
            switch (entry.getType()) {
                case SleepBarEntry.AWAKE:
                    mRectPaint.setColor(awakeColor);
                    canvas.drawRect(widthOffset, height * 0.4f, widthOffset + barSpace, height - labelHeight, mRectPaint);
                    widthOffset += barSpace;
                    canvas.save();
                    break;
                case SleepBarEntry.DEEP:
                    mRectPaint.setColor(deepColor);
                    canvas.drawRect(widthOffset, height * 0.2f, widthOffset + barSpace, height - labelHeight, mRectPaint);// l t r b
                    widthOffset += barSpace;
                    canvas.save();
                    break;
                case SleepBarEntry.LIGHT:
                    mRectPaint.setColor(lightColor);
                    canvas.drawRect(widthOffset, height * 0.3f, widthOffset + barSpace, height - labelHeight, mRectPaint);
                    widthOffset += barSpace;
                    canvas.save();
                    break;
                default:
                    break;
            }
        }
        canvas.drawLine(0, height - labelHeight, width, height - labelHeight, mLinePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        super.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    public void setLabelAppearance(int labelSize, int labelColor, Typeface typeface) {
        this.labelType = typeface;
        this.labelSize = labelSize;
        this.labelColor = labelColor;

        mLabelPaint.setColor(labelColor);
        mLabelPaint.setTextAlign(Paint.Align.CENTER);
        mLabelPaint.setTextSize(labelSize);

        if (typeface != null) {
            mLabelPaint.setTypeface(typeface);
        }

        Paint.FontMetrics fontMetrics = mLabelPaint.getFontMetrics();
        labelHeight = fontMetrics.descent - fontMetrics.ascent;
    }

    public void setSleepDataBean(SleepDayDateBean bean) {
        if (bean == null || bean.getTimePointArray() == null ||
                bean.getDurationTimeArray() == null ||
                bean.getSleepStatusArray() == null) {
            return;
        }

        if (bean.getTimePointArray().length >= 1) {
            entries.clear();
            int[] durationTimeArray = bean.getDurationTimeArray();
            int[] timePointArray = bean.getTimePointArray();
            int[] statusArray = bean.getSleepStatusArray();
            float totalTime = 0.0f;
            for (int time : durationTimeArray) {
                totalTime += time;
            }

            for (int i = 0; i < statusArray.length; i++) {
                int status = statusArray[i];
                int durationTime = durationTimeArray[i];
                SleepBarEntry entry = new SleepBarEntry();
                entry.setSpacePercent((float) durationTime / totalTime);
                switch (status) {
                    case DEEP:
                        entry.setType(SleepBarEntry.DEEP);
                        break;
                    case LIGHT:
                        entry.setType(SleepBarEntry.LIGHT);
                        break;
                    case AWAKE:
                        entry.setType(SleepBarEntry.AWAKE);
                        break;
                }
                entries.add(entry);

            }

            labels.clear();

            int firstTimeNode = timePointArray[0];
            int totalDurationTime = 0;
            for (int i = 0; i < durationTimeArray.length; i++) {
                totalDurationTime += durationTimeArray[i];
            }


            int fourthTimeNode = (firstTimeNode + totalDurationTime) % 1440;
            int secondTimeNode = (firstTimeNode - durationTimeArray[0] + totalDurationTime / 3) % 1440;
            Logger.d(TAG, "second time node >>>>> " + secondTimeNode);
            Logger.d(TAG, "totalDurationTime >>>>> " + totalDurationTime);
            Logger.d(TAG, "per duration time >>>>> " + (totalDurationTime / 3));

            int thirdTimeNode = (secondTimeNode + totalDurationTime / 3) % 1440;


            labels.add(DateUtils.getTimeByMinute("HH:mm", firstTimeNode - durationTimeArray[0]));
            labels.add(DateUtils.getTimeByMinute("HH:mm", secondTimeNode));
            labels.add(DateUtils.getTimeByMinute("HH:mm", thirdTimeNode));
            labels.add(DateUtils.getTimeByMinute("HH:mm", timePointArray[timePointArray.length - 1]));
//            invalidate();

        } else {
            entries.clear();
            labels.clear();
            int firstTimeNode = 1080; //18:00
            int secondTimeNode = 1320; // 22:00
            int thirdTimeNode = 120; // 02:00
            int fourthTimeNode = 360; // 06:00
            labels.add(DateUtils.getTimeByMinute("HH:mm", firstTimeNode));
            labels.add(DateUtils.getTimeByMinute("HH:mm", secondTimeNode));
            labels.add(DateUtils.getTimeByMinute("HH:mm", thirdTimeNode));
            labels.add(DateUtils.getTimeByMinute("HH:mm", fourthTimeNode));
        }
        invalidate();
    }

    private int getTextWidth(String text, Paint paint) {
        Rect rect = new Rect(0, 0, 0, 0);
        paint.getTextBounds(text, 0, text.length() - 1, rect);
        return rect.right - rect.left;
    }

    public void clear() {
        entries.clear();
        labels.clear();
        invalidate();
    }

    private int dip2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
