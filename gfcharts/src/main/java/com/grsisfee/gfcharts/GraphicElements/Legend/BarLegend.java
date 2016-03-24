package com.grsisfee.gfcharts.GraphicElements.Legend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import com.grsisfee.gfcharts.Utils.ColorUtil;
import com.grsisfee.gfcharts.Utils.MathUtil;
import com.grsisfee.gfcharts.Utils.ScreenUtil;

/**
 * Author: GrsisFee
 * Date:   2016/3/4
 * Desc:   柱状图例
 * All rights reserved.
 */
public class BarLegend extends Legend {

    public BarLegend(Context context) {
        super(context);

        // 初始参数默认值
        SUGGEST_BARLEGEND_WIDTH = 22 * DP_1;
        SUGGEST_BARLEGEND_HEIGHT = 13 * DP_1;
        SUGGEST_BARLEGEND_DISTANCE = 3 *DP_1;

        legendTopLeftPoints = null;

        legendDataWidth = legendDataHeight = null;

        legendPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        legendPaint.setStyle(Paint.Style.FILL);
        legendPaint.setColor(Color.argb(225, 135, 206, 250));

        legendBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        legendBorderPaint.setStyle(Paint.Style.STROKE);
        legendBorderPaint.setStrokeWidth(DP_1);
        legendBorderPaint.setColor(ColorUtil.getDarkerColor(legendPaint.getColor(), 0.8f));

        classifyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        classifyPaint.setColor(Color.BLACK);
        classifyPaint.setTextSize(ScreenUtil.sp2px(context, 10f));      // 10sp
    }

    @Override
    public void preDraw(Paint pen, @NonNull Rect containerRect) throws RuntimeException {
        if (color == null || classify == null || color.length != classify.length)
            throw new RuntimeException("需要首先传入合法的图例颜色和分类数据。");
        if (pen != null) {
            legendPaint = pen;
            legendBorderPaint.setColor(ColorUtil.getDarkerColor(legendPaint.getColor(), 0.8f));
        }

        PointF chartTopLeftPoint = new PointF(
                containerRect.left + SUGGEST_BARLEGEND_DISTANCE,
                containerRect.top + SUGGEST_BARLEGEND_DISTANCE * 2
        );

        legendDataWidth = calClassifyWidth(classifyPaint);
        legendDataHeight = calClassifyHeight(classifyPaint);

        legendTopLeftPoints = new PointF[color.length];

        // 计算图例左上角点
        if (orientation) {
            // 垂直方向
            for (int i = 0; i < color.length; i++) {
                legendTopLeftPoints[i] = new PointF(
                        chartTopLeftPoint.x,
                        chartTopLeftPoint.y + (SUGGEST_BARLEGEND_HEIGHT + SUGGEST_BARLEGEND_DISTANCE) * i
                );
            }
        } else {
            // 水平方向
            float x = chartTopLeftPoint.x;
            for (int i = 0; i < color.length; i++) {
                legendTopLeftPoints[i] = new PointF(
                        x,
                        chartTopLeftPoint.y
                );
                x += (SUGGEST_BARLEGEND_WIDTH + SUGGEST_BARLEGEND_DISTANCE * 2) + legendDataWidth[i];
            }
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {

        float v = calPaintAscent(classifyPaint);
        for (int i = 0; i < classify.length; i++) {
            legendPaint.setColor(color[i]);
            legendBorderPaint.setColor(ColorUtil.getDarkerColor(color[i], 0.8f));

            canvas.drawRect(
                    legendTopLeftPoints[i].x,
                    legendTopLeftPoints[i].y,
                    legendTopLeftPoints[i].x + SUGGEST_BARLEGEND_WIDTH,
                    legendTopLeftPoints[i].y + SUGGEST_BARLEGEND_HEIGHT,
                    legendPaint
            );
            canvas.drawRect(
                    legendTopLeftPoints[i].x,
                    legendTopLeftPoints[i].y,
                    legendTopLeftPoints[i].x + SUGGEST_BARLEGEND_WIDTH,
                    legendTopLeftPoints[i].y + SUGGEST_BARLEGEND_HEIGHT,
                    legendBorderPaint
            );

            canvas.drawText(
                    classify[i],
                    legendTopLeftPoints[i].x +
                            SUGGEST_BARLEGEND_WIDTH +
                            SUGGEST_BARLEGEND_DISTANCE,
                    legendTopLeftPoints[i].y - v + (SUGGEST_BARLEGEND_HEIGHT - legendDataHeight[i]) / 2f,
                    classifyPaint);
        }
    }

    @Override
    public Rect getElementMinSize(Paint pen) {
        if (pen != null)
            classifyPaint = pen;

        // 更新分类数据宽高
        legendDataWidth = calClassifyWidth(classifyPaint);
        legendDataHeight = calClassifyHeight(classifyPaint);

        Rect minRc;
        if (orientation) {
            minRc = new Rect(
                    0, 0,
                    (int) (MathUtil.getMaxValue(calClassifyWidth(classifyPaint)) + SUGGEST_BARLEGEND_WIDTH + SUGGEST_BARLEGEND_DISTANCE * 4),
                    (int) (SUGGEST_BARLEGEND_HEIGHT * classify.length + SUGGEST_BARLEGEND_DISTANCE * (classify.length - 1))
            );
        } else {
            float w = 0, h = 0;
            h = MathUtil.getMaxValue(legendDataHeight);
            for (int i = 0; i < color.length; i++) {
                w += (SUGGEST_BARLEGEND_WIDTH + SUGGEST_BARLEGEND_DISTANCE * 2) + legendDataWidth[i];
            }
            w += SUGGEST_BARLEGEND_DISTANCE * 2;
            minRc = new Rect(0, 0, (int) w, (int) h);
        }

        return minRc;
    }

    private float SUGGEST_BARLEGEND_WIDTH;                              // 柱状图例宽度
    private float SUGGEST_BARLEGEND_HEIGHT;                             // 柱状图例高度
    private float SUGGEST_BARLEGEND_DISTANCE;                           // 柱状图例间距

    private Paint legendPaint;                                          // 图例画笔
    private Paint legendBorderPaint;                                    // 图例边框画笔
    private Paint classifyPaint;                                        // 分类画笔

    private PointF [] legendTopLeftPoints;                              // 分类数据基准点
    private float [] legendDataWidth;                                   // 分类数据宽度
    private float [] legendDataHeight;                                  // 分类数据高度
}
