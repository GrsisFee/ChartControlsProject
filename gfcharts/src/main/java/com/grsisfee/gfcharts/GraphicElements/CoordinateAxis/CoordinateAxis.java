package com.grsisfee.gfcharts.GraphicElements.CoordinateAxis;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.grsisfee.gfcharts.Base.StatisticData;
import com.grsisfee.gfcharts.GraphicElements.GraphicElements;
import com.grsisfee.gfcharts.Utils.MathUtil;
import com.grsisfee.gfcharts.Utils.ScreenUtil;

import java.util.List;

/**
 * Author: GrsisFee
 * Date:   2016/2/3
 * Desc:
 * All rights reserved.
 */
@SuppressWarnings("unused")
public abstract class CoordinateAxis implements GraphicElements {

    public CoordinateAxis(Context context) {
        initValues(context);
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
    public abstract PointF [] getOriginalPoint();

    /**
     * 获得X轴或Y轴方向，刻度值与屏幕像素的比例
     * @param xAxis true 指示获得X轴方向刻度值与屏幕像素的比例
     *              false 指示获得Y轴方向刻度值与屏幕像素的比例
     * @return 返回刻度值与屏幕像素的比例
     */
    public abstract float getAxisRatio(boolean xAxis);

    /**
     * 获得统计图像素位置（相对于控件的左上角）、像素宽度和像素高度（不包含坐标轴区域）
     * @return 返回RectF对象
     */
    public abstract RectF getChartRect();

    /**
     * 获得统计图clipRect
     * @return 返回RectF对象
     */
    public abstract RectF getClipRect();
    /**
     * 初步检查统计数据的合法性，并判断出统计数据属于的坐标轴类型
     * @param data 传入的统计数据
     * @throws RuntimeException 运行时异常，传入的统计数据格式不合法
     */
    public void setData(List<StatisticData> data) throws RuntimeException {
        if (!checkData(data))
            throw new RuntimeException("传入的统计数据格式不合法。");
    }

    public int getAxisType() {
        return axisType;
    }

    public String [] getxStrCoordinate() {
        return xStrCoordinate;
    }

    public double [] getxNumCoordinate() {
        return xNumCoordinate;
    }

    public String [] getyStrCoordinate() {
        return yStrCoordinate;
    }

    public double [] getyNumCoordinate() {
        return yNumCoordinate;
    }

    /**
     * 计算画笔从baseline到文字最高点的距离ascent
     * 因为是相对于baseline的，所以这个值是负值
     * @param pen 画笔
     * @return 返回画笔ascent值
     */
    public float calPaintAscent(@NonNull Paint pen) {
        Paint.FontMetrics fontMetrics = pen.getFontMetrics();
        return fontMetrics.ascent;
    }

    /**
     * 根据统计数据在某一坐标轴下的最大值和最小值，计算坐标轴刻度
     * @param numOfBlock 屏幕能够显示的最大 两个刻度之间的块个数
     * @param minValue 统计数据的最小值
     * @param maxValue 统计数据的最大值
     * @param decimalPlaces 设置返回坐标刻度值的小数位数
     * @return 返回坐标轴刻度的数值字符串
     * @throws RuntimeException 运行时异常，绘制统计图的容器过小，则会触发该异常
     */
    public String [] getCoordinateTicks(int numOfBlock,
                                        double minValue,
                                        double maxValue,
                                        int decimalPlaces) throws RuntimeException {
        if (numOfBlock <= 0)                                                               // 当container大小较小时，可能会触发该异常
            throw new RuntimeException("绘制统计图的容器过小。");
        if (maxValue < minValue)
            throw new RuntimeException("CoordinateAxis.getCoordinateTicks传入参数错误。");

        minValue -= Math.abs(minValue) * 0.01;
        maxValue += Math.abs(maxValue) * 0.01;

        double valueBlock = (maxValue - minValue) / numOfBlock;
        valueBlock = calValueInSequence(valueBlock);
        int numOfTick = (int) Math.ceil((maxValue - minValue) / valueBlock) + 1;           // 向上取整，加1包含两端端点
        if (maxValue * minValue < 0) {
            // 异号：包含0
            if (Math.abs(minValue) % valueBlock != 0) {
                numOfTick ++;
                minValue = Math.ceil(Math.abs(minValue) / valueBlock) * valueBlock * -1;
            }
        } else {
            // 同号：可能包含0
            numOfTick ++;
            if (minValue >= 0) {
                // 同为正
                if (minValue <= valueBlock) minValue = 0;
                else minValue -= valueBlock;
            } else {
                // 同为负
                if (Math.abs(maxValue) <= valueBlock) minValue = -valueBlock * (numOfTick - 1);
                else minValue -= valueBlock;
            }
        }

        if (numOfTick > numOfBlock + 2)
            throw new RuntimeException("自动分隔出的刻度个数大于屏幕能分隔出刻度的最大数目。");

        String [] inverseTicks = new String [numOfTick];
        String fStr = "%." + String.valueOf(decimalPlaces) + "f";
        inverseTicks[0] = String.format(fStr, minValue);
        for (int i = 1; i < numOfTick; i++) {
            inverseTicks[i] = String.format(fStr, minValue + valueBlock * i);
        }

        // 逆序保存
        String [] ticks = new String[numOfTick];
        for (int i = inverseTicks.length - 1, index = 0; i >= 0; i--, index++)
            ticks[index] = inverseTicks[i];

        return ticks;
    }

    /**
     * 根据坐标轴刻度的数值字符串，计算刻度字符串所占的长度，高度（像素）
     * @param pen 绘制刻度字符串的画笔
     * @param ticks 传入的刻度数值字符串
     * @return 返回的是刻度字符串的长度和高度。
     *         数据结构为：一个二维数组，第一行为所有刻度的长度，第二行为所有刻度对应的高度
     */
    public double [][] getCoordinateTicksSize(@NonNull Paint pen, @NonNull String [] ticks) {
        return getCoordinateTicksSize(pen, ticks, 0f);
    }

    /**
     * 根据坐标轴刻度的数值字符串，计算刻度字符串所占的长度，高度（像素）
     * @param pen 绘制刻度字符串的画笔
     * @param ticks 传入的刻度数值字符串
     * @param rotateAngle 坐标刻度字符串顺时针旋转的角度[0, 90)（以文字左上角为旋转中心）
     * @return 返回的是刻度字符串的长度和高度。
     *         数据结构为：一个二维数组，第一行为所有刻度的长度，第二行为所有刻度对应的高度
     */
    public double [][] getCoordinateTicksSize(@NonNull Paint pen, @NonNull String [] ticks, float rotateAngle) {
        if (ticks.length <= 0) return null;

        rotateAngle = Math.abs(rotateAngle);
        rotateAngle %= 90;
        rotateAngle *= (2 * Math.PI / 360);                       // 转化为弧度值

        double [][] ticksSize = new double [2][ticks.length];     // 这种分配空间的方法，不适用于C++的二维指针
        for (int i = 0; i < ticks.length; i++) {
            Rect textRc = new Rect();
            pen.getTextBounds(ticks[i], 0, ticks[i].length(), textRc);
            double tw = textRc.width();
            double th = textRc.height();
            ticksSize[0][i] = tw * Math.cos(rotateAngle) + th * Math.sin(rotateAngle);
            ticksSize[1][i] = tw * Math.sin(rotateAngle) + th * Math.cos(rotateAngle);
        }

        return ticksSize;
    }

    /**
     * 获得图元的最小大小
     * @param pen 此处传入画笔或NUll
     * @return 绘图的最小大小
     */
    @Override
    public Rect getElementMinSize(Paint pen) {
        return axisMinSize; // 最小大小固定为： Rect(0, 0, 200, 200);
    }

    /**
     * 计算原点位置坐标
     * @param ticks X轴或Y轴的刻度
     * @param start 起始坐标
     * @param tickDis 两个刻度间距
     * @return 返回原点的横坐标或纵坐标
     */
    protected float calAxisOriginal(@NonNull String [] ticks, float start, float tickDis) {
        double [] ticksValue = new double[ticks.length];
        for (int i = 0; i < ticks.length; i++)
            ticksValue[i] = Double.parseDouble(ticks[i]);
        if (MathUtil.getMaxValue(ticksValue) <= 0) return start;

        boolean inIf = false;
        for (double aTicksValue : ticksValue) {
            if (aTicksValue == 0) {
                inIf = true;
                break;
            }

            start += tickDis;
        }

        return inIf ? start : start - tickDis;
    }

    /**
     * 计算原点位置对应的逻辑刻度值
     * @param ticks X轴或Y轴的刻度（必须具有顺序，Y轴方向需要传入的刻度从大到小的传入，X轴方向--还未测试）
     * @return 原点位置对应的逻辑刻度值
     */
    protected float calAxisOriginalTick(@NonNull String [] ticks) {
        float [] ticksValue = new float[ticks.length];
        for (int i = 0; i < ticks.length; i++)
            ticksValue[i] = Float.parseFloat(ticks[i]);
        if (MathUtil.getMaxValue(ticksValue) <= 0) return ticksValue[0];

        for (float val : ticksValue) {
            if (val == 0) return 0;
        }

        return ticksValue[ticksValue.length - 1];
    }

    /**
     * 计算X轴或Y轴方向刻度对应的屏幕像素与逻辑刻度的比例
     * @param ticks X轴或Y轴的逻辑刻度
     * @param axisLength X轴或Y轴的像素长度
     * @return 返回X轴或Y轴方向刻度对应的屏幕像素与逻辑刻度的比例
     */
    protected float calAxisRatio(@NonNull String [] ticks, double axisLength) {
        double [] ticksValue = new double[ticks.length];
        for (int i = 0; i < ticks.length; i++)
            ticksValue[i] = Double.parseDouble(ticks[i]);

        return (float) (axisLength / (MathUtil.getMaxValue(ticksValue) - MathUtil.getMinValue(ticksValue)));
    }

    private float calValueInSequence(double limitValue) {
        // 取limitValue最接近序列 1, 2, 4, 8, 16, 32, 64, 128…… 的值
        float value = 1f;
        while (value < limitValue) {
            value *= 2;
        }
        return value;
    }

    private boolean checkData(List<StatisticData> uncheckData) {
        int oldAxisType = AXIS_TYPE_UNKNOWN;
        isValidData = false;

        for (StatisticData tmp : uncheckData) {
            int newAxisType = judgeAxisType(tmp);               // 判断数据对应的坐标轴类型
            if (newAxisType == AXIS_TYPE_UNKNOWN) return false; // 无法判断传入的数据应属于何种类型的坐标轴
            if (oldAxisType == AXIS_TYPE_UNKNOWN)
                oldAxisType = newAxisType;
            else {
                if (oldAxisType != newAxisType) return false;   // 传入的一组统计数据，属于不同的坐标轴类型
            }
        }
        // 获得坐标轴类型
        axisType = oldAxisType;
        // 根据坐标轴类型，获得坐标数据
        switch (axisType) {
            case AXIS_TYPE_XOY:
                xNumCoordinate = new double [uncheckData.size()];
                yNumCoordinate = new double [uncheckData.size()];
                for (int i = 0; i < xNumCoordinate.length; i++) {
                    xNumCoordinate[i] =
                            Double.parseDouble(uncheckData.get(i).getKey().toString());
                    yNumCoordinate[i] =
                            Double.parseDouble(uncheckData.get(i).getValue().toString());
                }
                break;
            case AXIS_TYPE_OY:
                xStrCoordinate = new String [uncheckData.size()];
                yNumCoordinate = new double [uncheckData.size()];
                for (int i = 0; i < xStrCoordinate.length; i++) {
                    xStrCoordinate[i] =
                            uncheckData.get(i).getKey().toString();
                    yNumCoordinate[i] =
                            Double.parseDouble(uncheckData.get(i).getValue().toString());
                }
                break;
            case AXIS_TYPE_OX:
                xNumCoordinate = new double [uncheckData.size()];
                yStrCoordinate = new String [uncheckData.size()];
                for (int i = 0; i < xNumCoordinate.length; i++) {
                    xNumCoordinate[i] =
                            Double.parseDouble(uncheckData.get(i).getKey().toString());
                    yStrCoordinate[i] =
                            uncheckData.get(i).getValue().toString();
                }
                break;
            default: return false;
        }

        isValidData = true;                                     // 传入的统计数据合法
        // data = uncheckData;                                  // 保存统计数据
        return true;
    }

    private int judgeAxisType(StatisticData tmp) {
        Object tmpKey, tmpValue;
        tmpKey = tmp.getKey();
        tmpValue = tmp.getValue();

        if ((tmpKey instanceof Double ||
             tmpKey instanceof Float ||
             tmpKey instanceof Integer) &&
                (tmpValue instanceof Double ||
                 tmpValue instanceof Float ||
                 tmpValue instanceof Integer))
            return AXIS_TYPE_XOY;

        if (tmpKey instanceof String &&
                (tmpValue instanceof Double ||
                 tmpValue instanceof Float ||
                 tmpValue instanceof Integer))
            return AXIS_TYPE_OY;

        if ((tmpKey instanceof Double ||
             tmpKey instanceof Float ||
             tmpKey instanceof Integer) &&
                tmpValue instanceof String)
            return AXIS_TYPE_OX;

        return AXIS_TYPE_UNKNOWN;
    }

    private void initValues(Context context) {
        axisType = AXIS_TYPE_UNKNOWN;
        xStrCoordinate = yStrCoordinate = null;
        xNumCoordinate = yNumCoordinate = null;

        axisMinSize = new Rect(0, 0, 200, 200);

        SUGGEST_DIS_TO_BORDER = ScreenUtil.dp2px(context, 4);       // 4dp
        SUGGEST_DIS_TICK_MARK = ScreenUtil.dp2px(context, 38);      // 38dp
        SUGGEST_DIS_TICK_AXIS = ScreenUtil.dp2px(context, 2);       // 2dp
        LONG_TICK_LENGTH = ScreenUtil.dp2px(context, 8);            // 8dp
        TICK_LENGTH = ScreenUtil.dp2px(context, 5);                 // 5dp
        DP_1 = ScreenUtil.dp2px(context, 1);                        // 1dp

        // 初始化画笔
        gridPen = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPen.setColor(Color.argb(255, 168, 168, 168));
        gridPen.setStyle(Paint.Style.STROKE);
        gridPen.setStrokeWidth(ScreenUtil.dp2px(context, 1));

        PathEffect effects = new DashPathEffect(new float[] {5, 5, 5, 5}, 1);
        gridPen.setPathEffect(effects);

        tickPen = new Paint(Paint.ANTI_ALIAS_FLAG);
        tickPen.setColor(Color.BLACK);
        tickPen.setStyle(Paint.Style.STROKE);
        tickPen.setTextSize(ScreenUtil.sp2px(context, 10));          // 10sp
    }

    // 坐标轴类型
    public static final int AXIS_TYPE_UNKNOWN = -1;             // 未知的坐标轴类型
    public static final int AXIS_TYPE_XOY = 0;                  // XOY 横纵坐标均为数值类型
    public static final int AXIS_TYPE_OY = 1;                   // OY 横坐标为字符串，纵坐标为数值
    public static final int AXIS_TYPE_OX = 2;                   // OX 横坐标为数值，纵坐标为字符串

    // 推荐距离
    public int SUGGEST_DIS_TO_BORDER;                           // 推荐的坐标轴距边框的距离
    public int SUGGEST_DIS_TICK_MARK;                           // 推荐的坐标轴每两个刻度之间的距离
    public int SUGGEST_DIS_TICK_AXIS;                           // 推荐的坐标轴与刻度标记的距离
    public int LONG_TICK_LENGTH;                                // 长刻度长度
    public int TICK_LENGTH;                                     // 短刻度长度

    public Paint gridPen;                                       // 网格画笔
    public Paint tickPen;                                       // 刻度字符串画笔

    protected boolean isValidData;                              // 指示传入的统计数据是否合法
    protected float DP_1;                                       // 1dp 对应的 px

    private Rect axisMinSize;                                   // 坐标轴最小大小
    private int axisType;                                       // 当前坐标轴类型，取值为[-1, 2]的整数
    // private List<StatisticData> data;                           // 保存统计数据
    private String [] xStrCoordinate;                           // 字符串横坐标
    private double [] xNumCoordinate;                           // 数值横坐标
    private String [] yStrCoordinate;                           // 字符串纵坐标
    private double [] yNumCoordinate;                           // 数值纵坐标
}
