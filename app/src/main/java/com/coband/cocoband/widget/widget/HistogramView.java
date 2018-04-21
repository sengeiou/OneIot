package com.coband.cocoband.widget.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.coband.App;
import com.coband.common.utils.Utils;
import com.coband.watchassistant.R;

/**
 * Created by tgc on 17-9-13.
 */

public class HistogramView extends View {
    // = new int[]{Color.RED, Color.BLUE, Color.GREEN};
    private Paint paintRect = new Paint();
    private Paint paintOcclusion = new Paint();
    private Paint paintTriangle = new Paint();
    private Paint paintRectBkg = new Paint();
    private int[] colorArray = new int[]{getResources().getColor(R.color.color_f24e22),
            getResources().getColor(R.color.color_ffc814),
            getResources().getColor(R.color.color_00b4ff),
            getResources().getColor(R.color.color_3cdca5)};
    private int blockNum = 20;
    private int rectTop = Utils.dp2px(156, App.getContext()) + 10; // 10
    private int triangleLength = Utils.dp2px(8, App.getContext());
    private int i = Utils.dp2px(156, App.getContext()) + 10;
    private int left = Utils.dp2px(10, App.getContext());
    private int right = Utils.dp2px(30, App.getContext());

    public HistogramView(Context context) {
        this(context, null);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs, int blockNum, int[] colorArray) {
        this(context, attrs, 0);
        this.blockNum = blockNum;
        this.colorArray = colorArray;
    }

    private void initPaint() {
        LinearGradient gradient = new LinearGradient(0, 0, right - left, rectTop,
                colorArray, null, Shader.TileMode.CLAMP);
        paintRect.setShader(gradient);

        paintOcclusion.setColor(getResources().getColor(R.color.color_f5f5f5));
        paintOcclusion.setStyle(Paint.Style.FILL);

        paintTriangle.setColor(getResources().getColor(R.color.color_9f9f9f));
        paintTriangle.setStyle(Paint.Style.FILL);

        paintRectBkg.setColor(getResources().getColor(R.color.color_c8c8c8));
        paintRectBkg.setStyle(Paint.Style.FILL);
    }

    public void setData(int rectTop) {
        this.rectTop = rectTop;
        i -= rectTop / 20;
        if (i <= 0) {
            i = Utils.dp2px(156, App.getContext()) + 10;
        }
        invalidate();
    }

    public void setHigh(int rectTop) {
        this.i = rectTop;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int px = Utils.dp2px(156, App.getContext());
        px+=10;
        canvas.drawRect(left, 10, right, px, paintRectBkg);
        canvas.drawRect(left, i, right, px, paintRect);

        Path path = new Path();
        path.moveTo(0, i - triangleLength / 2);
        path.lineTo(Utils.dp2px(7, App.getContext()), i);
        path.lineTo(0, i + triangleLength / 2);
        path.close();
        canvas.drawPath(path, paintTriangle);

        int j = 0;
        int top = 10, bottom = 18;
        while (j < blockNum) {
            canvas.drawRect(left, top, right, bottom, paintOcclusion);
            if (blockNum > 100 || blockNum < 0) {
                blockNum = 100;
            }
            j++;
            top += px / 20;
            bottom += px / 20;
        }
    }
}
