package com.grsisfee.gfcharts.GraphicElements.Label;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import com.grsisfee.gfcharts.GraphicElements.GraphicElements;
import com.grsisfee.gfcharts.Utils.ScreenUtil;


/**
 * Author: GrsisFee
 * Date:   2016/2/9
 * Desc:
 * All rights reserved.
 */
public class Label implements GraphicElements {

    public Label(Context context, String text) {
        labelText = text;
        labelDirection = TEXT_DIRECTION_TOP_HORIZONTAL;              // 文字方向默认顶部水平
        labelPt = new Point(0, 0);

        // 初始化文字默认画笔
        labelPen = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPen.setColor(Color.BLACK);
        labelPen.setFakeBoldText(true);                              // 默认粗体
        labelPen.setTextSize(ScreenUtil.sp2px(context, 11f));        // 11sp

        SUGGEST_DIS_TO_BORDER = ScreenUtil.dp2px(context, 3f);       // 3dp
    }

    public void setLabelDirection(int labelDirection) {
        switch (labelDirection) {
            case TEXT_DIRECTION_BOTTOM_HORIZONTAL:
            case TEXT_DIRECTION_TOP_HORIZONTAL:
            case TEXT_DIRECTION_LEFT_VERTICAL:
            case TEXT_DIRECTION_RIGHT_VERTICAL:
                this.labelDirection = labelDirection;
                break;
            default:
                this.labelDirection = TEXT_DIRECTION_TOP_HORIZONTAL;
        }
    }

    public int getLabelDirection() {
        return labelDirection;
    }

    /**
     * 根据统计数据和容器绘制区域大小，计算图元的位置和形状等坐标
     * @param pen 绘制图元的画笔
     * @param containerRect 容器绘制区域
     */
    @Override
    public void preDraw(Paint pen, @NonNull Rect containerRect) {
        if (pen != null) labelPen = pen;

        labelPen.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetricsInt fontMetrics = labelPen.getFontMetricsInt();

        // 计算label位置
        switch (labelDirection) {
            case TEXT_DIRECTION_LEFT_VERTICAL:
                labelPt = new Point(
                        -containerRect.centerY(),
                        containerRect.left - fontMetrics.ascent + SUGGEST_DIS_TO_BORDER);
                break;
            case TEXT_DIRECTION_TOP_HORIZONTAL:
                labelPt = new Point(
                        containerRect.centerX(),
                        containerRect.top - fontMetrics.ascent + SUGGEST_DIS_TO_BORDER);
                break;
            case TEXT_DIRECTION_RIGHT_VERTICAL:
                labelPt = new Point(
                        containerRect.centerY(),
                        -containerRect.right - fontMetrics.ascent + SUGGEST_DIS_TO_BORDER);
                break;
            case TEXT_DIRECTION_BOTTOM_HORIZONTAL:
                labelPt = new Point(
                        containerRect.centerX(),
                        containerRect.bottom - fontMetrics.bottom - (int) (SUGGEST_DIS_TO_BORDER / 3f));
                break;
        }
    }

    /**
     * 绘制图元
     * @param canvas 绘制图元的容器
     */
    @Override
    public void onDraw(@NonNull Canvas canvas) {

        canvas.save();

        switch (labelDirection) {
            case TEXT_DIRECTION_BOTTOM_HORIZONTAL:
            case TEXT_DIRECTION_TOP_HORIZONTAL:
                break;
            case TEXT_DIRECTION_LEFT_VERTICAL:
                canvas.rotate(-90);
                break;
            case TEXT_DIRECTION_RIGHT_VERTICAL:
                canvas.rotate(90);
                break;
        }

        canvas.drawText(labelText, labelPt.x, labelPt.y, labelPen);

        canvas.restore();
    }

    /**
     * 获得图元的最小大小
     * @param pen 绘制图元的画笔或null，此处传入null则会使用默认画笔
     * @return 绘图的最小大小
     */
    @Override
    public Rect getElementMinSize(Paint pen) {
        if (pen == null) pen = labelPen;

        Rect textRc = new Rect();
        pen.getTextBounds(labelText, 0, labelText.length(), textRc);

        if (labelDirection == TEXT_DIRECTION_TOP_HORIZONTAL ||
                labelDirection == TEXT_DIRECTION_BOTTOM_HORIZONTAL)
            return new Rect(0, 0, textRc.width(), textRc.height() + SUGGEST_DIS_TO_BORDER);
        else
            return new Rect(0, 0, textRc.height() + SUGGEST_DIS_TO_BORDER, textRc.width());
    }

    public int SUGGEST_DIS_TO_BORDER;                               // 推荐的坐标轴距边框的距离

    public static final int TEXT_DIRECTION_LEFT_VERTICAL = 0;       // 左边的垂直方向
    public static final int TEXT_DIRECTION_TOP_HORIZONTAL = 1;      // 顶部水平
    public static final int TEXT_DIRECTION_RIGHT_VERTICAL = 2;      // 右边的垂直方向
    public static final int TEXT_DIRECTION_BOTTOM_HORIZONTAL = 3;   // 底部水平

    private String labelText;                                       // 标签文字
    private int labelDirection;                                     // 标签文字方向
    private Paint labelPen;                                         // 标签画笔
    private Point labelPt;                                          // 容器矩形左上角点
}
