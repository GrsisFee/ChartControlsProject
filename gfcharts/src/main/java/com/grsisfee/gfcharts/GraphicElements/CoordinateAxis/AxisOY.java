package com.grsisfee.gfcharts.GraphicElements.CoordinateAxis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.grsisfee.gfcharts.Base.StatisticData;
import com.grsisfee.gfcharts.Utils.MathUtil;

import java.util.List;


/**
 * Author: GrsisFee
 * Date:   2016/2/3
 * Desc:
 * All rights reserved.
 */
public class AxisOY extends CoordinateAxis {

    public AxisOY(Context context) {
        super(context);

        xTicks = null;
        yTicks = null;
        xTicksPt = null;
        yTicksPt = null;
        xTicksBasePt = null;
        xTickAngle = 0;

        originalPt = new PointF[2];
        axisRatio = 0;

        path = new Path();
        gridPath = new Path();

        showSmallTick = showXTickStr = true;
        // 设置默认画笔
        pen = new Paint(Paint.ANTI_ALIAS_FLAG);
        pen.setColor(Color.BLACK);
        pen.setStyle(Paint.Style.STROKE);
    }

    /**
     * 重写基类函数，用于进一步判断传入统计数据的类型是否是OY类型的数据
     * @param data 传入的统计数据
     * @throws RuntimeException 除了会抛出基类函数的异常以外，还会抛出新的运行时异常
     * 注意：必须首先调用super.setData(data)对统计数据的类型进行初步判断
     */
    @Override
    public void setData(List<StatisticData> data) throws RuntimeException {
        super.setData(data);

        int axisType = getAxisType();
        if (axisType != AXIS_TYPE_OY)
            throw new RuntimeException("传入的统计数据类型不属于OY类型坐标轴。");
    }

    /**
     * 计算x轴方向刻度的中心点，且以坐标轴原点作为纵向基准点
     * @return x轴方向刻度的中心点
     */
    public PointF [] getOxTickBasePoint() {
        return xTicksBasePt;                                            // 在preDraw函数中计算保存
    }

    /**
     * 获得坐标轴原点坐标 和 原点坐标的逻辑刻度
     *      此处的原点坐标不一定是（0, 0）逻辑坐标，也不一定就是图的左下角点坐标
     *      当坐标轴刻度包含0刻度时：返回0刻度位置坐标
     *      不包含0刻度时：1、统计数据全是负数：返回左上角坐标
     *                   2、统计数据全是正数：返回左下角坐标
     * @return 返回PointF[2]类型的点数据
     *              [0] 坐标轴原点对应在屏幕的像素坐标
     *              [1] 坐标轴原点的逻辑刻度
     */
    @Override
    public PointF [] getOriginalPoint() {
        return originalPt;                                              // 在preDraw函数中计算保存
    }

    /**
     * 获得Y轴方向，屏幕像素与刻度值的比例
     * @param xAxis 此处该参数不管传入什么，都是用于计算Y轴方向的屏幕像素与刻度值的比例
     * @return Y轴方向，屏幕像素与刻度值的比例
     */
    @Override
    public float getAxisRatio(boolean xAxis) {
        return axisRatio;                                               // 在preDraw函数中计算保存
    }

    /**
     * 获得统计图像素位置（相对于控件的左上角）、像素宽度和像素高度（不包含坐标轴区域）
     * @return 返回RectF对象
     */
    @Override
    public RectF getChartRect() {
        return chartRect;                                               // 在preDraw函数中计算保存
    }

    /**
     * 获得统计图clipRect
     * @return 返回RectF对象
     */
    public RectF getClipRect() {
        return clipRect;                                                // 在preDraw函数中计算保存
    }

    /**
     * 设置是否显示X轴的坐标刻度
     * @param show true 显示
     *             false 不显示
     */
    public void showXTickString(boolean show) {
        showXTickStr = show;
    }

    /**
     * 设置是否显示Y轴的短刻度
     * @param show true 显示
     *             false 不显示
     */
    public void setShowSmallTick(boolean show) {
        showSmallTick = show;
    }

    /**
     * 设置x轴刻度旋转角度[0, 90)
     * @param xTickAngle x轴刻度旋转角度
     */
    public void setXTickAngle(float xTickAngle) {
        this.xTickAngle = xTickAngle;
    }

    /**
     * 根据统计数据和容器绘制区域大小，计算坐标轴的位置和形状等坐标
     * @param pen 绘制坐标轴的画笔
     * @param containerRect 容器绘制区域
     * @throws RuntimeException 继续向上抛出运行时异常，确保在发生异常时，能终止程序
     *         函数内部引用的 getCoordinateTicks 父类函数，会在容器过小的时候，抛出运行时异常
     */
    @Override
    public void preDraw(Paint pen, @NonNull Rect containerRect) throws RuntimeException {
        if (pen != null) this.pen = pen;

        xTicks = getxStrCoordinate();
        double [][] xTicksSize = getCoordinateTicksSize(tickPen, xTicks, xTickAngle);    // 使用父类定义的刻度画笔

        double chartWidth, chartHeight;

        chartHeight = containerRect.height() -
                MathUtil.getMaxValue(xTicksSize[1]) -
                2 * (SUGGEST_DIS_TO_BORDER + SUGGEST_DIS_TICK_AXIS);

        int numOfBlock = (int) (chartHeight / SUGGEST_DIS_TICK_MARK);                    // 两个刻度之间的块个数
        double minValue = MathUtil.getMinValue(getyNumCoordinate());
        double maxValue = MathUtil.getMaxValue(getyNumCoordinate());
        yTicks = getCoordinateTicks(numOfBlock, minValue, maxValue, 0);                  // 刻度值字符串
        double [][] yTicksSize = getCoordinateTicksSize(tickPen, yTicks);                // 刻度值字符串的长高，使用父类定义的刻度画笔

        chartHeight -= MathUtil.getMaxValue(yTicksSize[1]);                              // 微调图元高度
        chartWidth = containerRect.width() -
                MathUtil.getMaxValue(yTicksSize[0]) -
                2 * (SUGGEST_DIS_TO_BORDER + SUGGEST_DIS_TICK_AXIS);

        PointF chartTopLeftPoint =
                new PointF((float) (
                                    SUGGEST_DIS_TO_BORDER +
                                    SUGGEST_DIS_TICK_AXIS +
                                    MathUtil.getMaxValue(yTicksSize[0]) +
                                    containerRect.left),
                           (float) (
                                    SUGGEST_DIS_TO_BORDER +
                                    SUGGEST_DIS_TICK_AXIS +
                                    MathUtil.getMaxValue(yTicksSize[1]) / 2 +
                                    containerRect.top)
                );

        float yDisTickMark = (float) chartHeight / (yTicks.length - 1);                   // 此处减一，十分重要。yTicks这里包含了两端端点

        // 计算并保存刻度字符串的位置，该位置是相对于path的坐标位置
        // yTicksPt[][] 第一行为横坐标，第二行为纵坐标
        xTicksPt = new float[2][xTicks.length];
        yTicksPt = new float[2][yTicks.length];

        float tickVposition = (float) (SUGGEST_DIS_TICK_AXIS + chartHeight + chartTopLeftPoint.y);
        float tickHposition = (float) (chartTopLeftPoint.x +
                chartWidth / (xTicks.length * 2) + LONG_TICK_LENGTH);

        if (xTickAngle != 0)                                                              // 若x刻度有旋转，则此处不移动x刻度
            for (int i = 0; i < xTicks.length; i++) {
                xTicksPt[0][i] = (float) (tickHposition + (chartWidth - LONG_TICK_LENGTH * 2) / xTicks.length * i);
                xTicksPt[1][i] = tickVposition;
            }
        else
            for (int i = 0; i < xTicks.length; i++) {
                xTicksPt[0][i] = (float) (tickHposition + (chartWidth - LONG_TICK_LENGTH * 2) / xTicks.length * i - xTicksSize[0][i] / 2);
                xTicksPt[1][i] = tickVposition;
            }

        for (int i = 0; i < yTicks.length; i++) {
            yTicksPt[0][i] = (float) (chartTopLeftPoint.x - (yTicksSize[0][i] + SUGGEST_DIS_TICK_AXIS));
            yTicksPt[1][i] = (float) (chartTopLeftPoint.y + yDisTickMark * i + yTicksSize[1][i] / 2);
        }

        // 计算保存输出的数据
        originalPt[0] = new PointF(
                chartTopLeftPoint.x,
                calAxisOriginal(yTicks, chartTopLeftPoint.y, yDisTickMark)
        );                                                                                              // 计算坐标原点坐标
        originalPt[1] = new PointF(
                0, calAxisOriginalTick(yTicks)
        );                                                                                              // 计算坐标原点的逻辑刻度
        axisRatio = calAxisRatio(yTicks, chartHeight);                                                  // 计算坐标轴刻度值对应的屏幕像素与刻度逻辑值的比例
        chartRect = new RectF(
                chartTopLeftPoint.x + LONG_TICK_LENGTH,
                chartTopLeftPoint.y,
                chartTopLeftPoint.x + (float) chartWidth - LONG_TICK_LENGTH,
                chartTopLeftPoint.y + (float) chartHeight
        );                                                                                              // 获得统计图像素位置（相对于控件的左上角）、像素宽度和像素高度（不包含坐标轴区域）
        clipRect = new RectF(
                chartRect.left - DP_1,
                chartRect.top,
                chartTopLeftPoint.x + (float) chartWidth - DP_1,
                (float) (chartRect.bottom + MathUtil.getMaxValue(xTicksSize[1]) + SUGGEST_DIS_TICK_AXIS * 2)
        );
        xTicksBasePt = new PointF[xTicks.length];
        if (xTickAngle != 0)
            for (int i = 0; i < xTicks.length; i++) {                                                   // x刻度有旋转，不移动刻度，计算x轴方向刻度位置
                xTicksBasePt[i] = new PointF(xTicksPt[0][i], originalPt[0].y);
            }
        else
            for (int i = 0; i < xTicks.length; i++) {                                                   // 计算x轴方向刻度位置
                xTicksBasePt[i] = new PointF((float) (xTicksPt[0][i] + xTicksSize[0][i] / 2), originalPt[0].y);
            }

        // 计算图元path
        path.reset();
        path.addRect(
                chartTopLeftPoint.x,
                chartTopLeftPoint.y,
                (float) chartWidth + chartTopLeftPoint.x,
                (float) chartHeight + chartTopLeftPoint.y,
                Path.Direction.CCW);                                                       // 逆时针方向绘制
        // 绘制小刻度
        int sTickCount = (yTicks.length - 1) * 5;                                          // 此处减一，十分重要。yTicks这里包含了两端端点
        float sDisTickMark = yDisTickMark / 5;
        for (int i = 1; i < sTickCount; i++) {
            path.moveTo(chartTopLeftPoint.x,
                    chartTopLeftPoint.y + sDisTickMark * i);
            if (i % 5 == 0) {
                if (Double.parseDouble(yTicks[i / 5]) != 0)
                    path.lineTo(LONG_TICK_LENGTH + chartTopLeftPoint.x,
                            chartTopLeftPoint.y + sDisTickMark * i);
                else
                    path.lineTo((float) chartWidth + chartTopLeftPoint.x,
                            chartTopLeftPoint.y + sDisTickMark * i);
            } else {
                if (showSmallTick)
                    path.lineTo(TICK_LENGTH + chartTopLeftPoint.x,
                            chartTopLeftPoint.y + sDisTickMark * i);
            }
        }

        gridPath.reset();
        for (int i = 1; i < yTicks.length; i++) {
            if (Double.parseDouble(yTicks[i]) != 0) {
                gridPath.moveTo(chartTopLeftPoint.x,
                        chartTopLeftPoint.y + yDisTickMark * i);
                gridPath.lineTo((float) chartWidth + chartTopLeftPoint.x,
                        chartTopLeftPoint.y + yDisTickMark * i);
            }
        }
    }

    /**
     * 绘制图元
     * @param canvas 绘制图元的容器
     */
    @Override
    public void onDraw(@NonNull Canvas canvas) {
        // 绘制刻度字符串
        if (xTicks != null && showXTickStr) {
            float tickPenAscent = -1 * calPaintAscent(tickPen);
            if (xTickAngle != 0)
                for (int i = 0; i < xTicks.length; i++) {
                    canvas.save();
                    canvas.translate(xTicksPt[0][i], xTicksPt[1][i]);
                    canvas.rotate(xTickAngle);
                    canvas.drawText(xTicks[i], 0, tickPenAscent, tickPen);                                 // 使用父类定义的刻度画笔
                    canvas.restore();
                }
            else
                for (int i = 0; i < xTicks.length; i++) {
                    canvas.drawText(xTicks[i], xTicksPt[0][i], xTicksPt[1][i] + tickPenAscent, tickPen);   // 使用父类定义的刻度画笔
                }
        }

        if (yTicks != null) {
            for (int i = 0; i < yTicks.length; i++) {
                canvas.drawText(yTicks[i], yTicksPt[0][i], yTicksPt[1][i], tickPen);                        // 使用父类定义的刻度画笔
            }
        }

        canvas.drawPath(gridPath, gridPen);     // 使用父类定义的网格画笔
        canvas.drawPath(path, pen);
    }

    private PointF [] originalPt;               // [0] 坐标原点对应在屏幕上的位置 和 [1] 坐标原点的逻辑刻度
    private float axisRatio;                    // 坐标轴刻度值对应的屏幕像素与刻度逻辑值的比例
    private RectF chartRect;                    // 获得统计图像素位置（相对于控件的左上角）、像素宽度和像素高度（不包含坐标轴区域）
    private RectF clipRect;                     // 获得统计图像素位置（相对于控件的左上角）、像素宽度（不包含坐标轴区域）和像素高度（包含坐标轴区域）、仅用于clip

    private boolean showXTickStr;               // 是否显示x方向刻度字符串

    private String [] xTicks;                   // x轴方向刻度字符串
    private float [][] xTicksPt;                // x轴方向刻度字符串位置
    private String [] yTicks;                   // y轴方向刻度字符串
    private float [][] yTicksPt;                // y轴方向刻度字符串位置

    private PointF [] xTicksBasePt;             // x轴方向刻度位置
    private float xTickAngle;                   // x轴方向刻度旋转的角度（默认旋转40°）

    private boolean showSmallTick;              // 显示x轴方向小刻度

    private Paint pen;                          // 绘制坐标轴画笔
    private Path path;                          // 图元Path

    private Path gridPath;                      // 网格Path
}
