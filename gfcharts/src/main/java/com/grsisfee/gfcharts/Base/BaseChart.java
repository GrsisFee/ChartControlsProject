package com.grsisfee.gfcharts.Base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.grsisfee.gfcharts.Utils.ScreenUtil;

/**
 * Author: GrsisFee
 * Date:   2016/2/1
 * Desc:
 * All rights reserved.
 */
@SuppressWarnings("unused")
public class BaseChart extends View {
    public BaseChart(Context context) {
        super(context);
        initControl(context);
    }

    public BaseChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
    }

    public BaseChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 更新控件宽高
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        // 更新控件位置
        getGlobalVisibleRect(globalVisibleLocation);
        globalX = globalVisibleLocation.left;
        globalY = globalVisibleLocation.top;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 绘制边框
        float halfBorder = borderPaint.getStrokeWidth() / 2;
        if (showOutBorder)
            canvas.drawRect(                                        // 外边框
                    halfBorder,
                    halfBorder,
                    width - halfBorder,
                    height - halfBorder,
                    borderPaint
            );
        canvas.drawRect(                                            // 内边框
                halfBorder + DP_1 * 2,
                halfBorder + DP_1 * 2,
                width - halfBorder - DP_1 * 2,
                height - halfBorder - DP_1 * 2,
                borderPaint
        );

        // 绘制背景
        canvas.drawRect(
                halfBorder + DP_1 * 2,
                halfBorder + DP_1 * 2,
                width - halfBorder - DP_1 * 2,
                height - halfBorder - DP_1 * 2,
                backgroundPaint
        );
    }

    protected void setBorderWidth(float borderWidth) {
        borderPaint.setStrokeWidth(borderWidth * DP_1);             // 设置边框宽度 dp为单位
    }

    protected void setBorderColor(int color) {
        borderPaint.setColor(color);
    }

    protected void setBkColor(int color) {
        backgroundPaint.setColor(Color.argb(128, 135, 206, 250));
    }

    protected void showOutsideBorder(boolean show) {
        showOutBorder = show;
    }

    private void initControl(Context context) {
        DP_1 = ScreenUtil.dp2px(context, 1);
        showOutBorder = true;

        // 初始化边框画笔
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(DP_1);
        borderPaint.setColor(Color.argb(255, 217, 217, 217));

        // 初始化背景画笔
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(Color.argb(64, 135, 206, 250));

        globalVisibleLocation = new Rect();

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 初始获得控件宽高
                width = getMeasuredWidth();
                height = getMeasuredHeight();
                // 初始获得控件位置
                getGlobalVisibleRect(globalVisibleLocation);
                globalX = globalVisibleLocation.left;
                globalY = globalVisibleLocation.top;
            }
        });
    }


    /**
     * 受保护的属性
     */
    protected float DP_1;                                           // 1dp 对应的 px

    protected int width;                                            // 当前控件view宽，单位px
    protected int height;                                           // 当前控件view高，单位px

    protected int globalX;                                          // 当前控件view的横坐标位置，单位px。基于屏幕坐标系，但不包含通知栏
    protected int globalY;                                          // 当前控件view的纵坐标位置，单位px，基于屏幕坐标系，但不包含通知栏
    protected Rect globalVisibleLocation;                           // 当前控件相对于屏幕的矩形位置区域，单位px，基于屏幕坐标系，但不包含通知栏
    /**
     * 类私有属性
     */
    private Paint borderPaint;                                      // 边框画笔
    private Paint backgroundPaint;                                  // 背景画笔
    private boolean showOutBorder;                                  // 是否显示外边框
}
