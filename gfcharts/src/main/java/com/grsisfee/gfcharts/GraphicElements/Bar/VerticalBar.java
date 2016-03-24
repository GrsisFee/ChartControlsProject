package com.grsisfee.gfcharts.GraphicElements.Bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import com.grsisfee.gfcharts.GraphicElements.GeoGraphicElements;
import com.grsisfee.gfcharts.Utils.ColorUtil;
import com.grsisfee.gfcharts.Utils.ScreenUtil;

/**
 * Author： Grsis Fee
 * Date:    2016/2/17
 * Desc:
 * All rights reserved.
 */
public class VerticalBar implements GeoGraphicElements {

    public VerticalBar(Context context) {
        initValues(context, false);
    }

    public VerticalBar(Context context, boolean useAnimation) {
        initValues(context, useAnimation);
    }

    /**
     * 设置是否显示边框
     * @param showBarBorder true: 默认显示
     *                      false: 不显示
     */
    public void setShowBarBorder(boolean showBarBorder) {
        this.showBarBorder = showBarBorder;
    }

    /**
     * 判断屏幕上的一点是否包含在图元内
     * @param screenPt 屏幕上的像素点
     * @return true：包含
     *         false：不包含
     */
    @Override
    public boolean contains(Point screenPt) throws RuntimeException {
        if (container == null)
            throw new RuntimeException("容器为空，是否忘记应先调用图元的preDraw函数。");

        // 垂直扩大点击区域，解决柱状太短不好点的问题
        Rect touchContainer;
        if (container.top < container.bottom) {
            touchContainer = new Rect(
                    container.left,
                    (int) (container.top - EXTEND_DISTENCE),
                    container.right,
                    (int) (container.bottom + EXTEND_DISTENCE)
            );
        } else {
            touchContainer = new Rect(
                    container.left,
                    (int) (container.bottom - EXTEND_DISTENCE),
                    container.right,
                    (int) (container.top + EXTEND_DISTENCE)
            );

            // logcat 输出调试信息
            /*String info = String.format("Point: (%d, %d)\ttouchContainer: (%d, %d, %d, %d)\t container: (%d, %d, %d, %d)\ttouchContainer contains: %d",
                    screenPt.x, screenPt.y,
                    touchContainer.left, touchContainer.top, touchContainer.right, touchContainer.bottom,
                    container.left, container.top, container.right, container.bottom,
                    touchContainer.contains(screenPt.x, screenPt.y) ? 1 : 0);
            Log.d("Info: ", info);*/
        }

        return touchContainer.contains(screenPt.x, screenPt.y);
    }

    /**
     * 根据传入参数，更新图元Path
     * @param top 柱状图元的顶
     */
    public void update(int top) {
        if (container == null || !useAnimation) return;

        barPath.reset();
        barPath.addRect(
                container.left,
                top,
                container.right,
                container.bottom,
                Path.Direction.CCW
        );
        barPath.close();
    }

    @Override
    public void preDraw(Paint pen, @NonNull Rect containerRect) {
        if (pen != null) {
            this.pen = pen;
            borderPen.setColor(ColorUtil.getDarkerColor(pen.getColor(), 0.8f));
        }

        container = containerRect;

        barPath.reset();
        if (useAnimation) {
            barPath.addRect(
                    container.left,
                    container.bottom,
                    container.right,
                    container.bottom,
                    Path.Direction.CCW
            );
        } else {
            barPath.addRect(
                    container.left,
                    container.top,
                    container.right,
                    container.bottom,
                    Path.Direction.CCW
            );
        }
        barPath.close();

        if (showBarBorder) {
            barBorderPath.reset();
            barBorderPath.addRect(
                    container.left,
                    container.top,
                    container.right,
                    container.bottom,
                    Path.Direction.CCW
            );
            barBorderPath.close();
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        canvas.drawPath(barPath, pen);

        if (showBarBorder) {
            canvas.drawPath(barBorderPath, borderPen);
        }
    }

    @Override
    public Rect getElementMinSize(Paint pen) {
        return container;
    }

    private void initValues(Context context, boolean useAnimation) {
        showBarBorder = true;
        barBorderPath = new Path();
        barPath = new Path();
        container = null;
        this.useAnimation = useAnimation;
        // 初始化图元画笔
        pen = new Paint(Paint.ANTI_ALIAS_FLAG);
        pen.setColor(Color.argb(225, 135, 206, 250));
        pen.setStyle(Paint.Style.FILL);
        // 初始化图元边框画笔
        borderPen = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPen.setColor(ColorUtil.getDarkerColor(Color.argb(225, 135, 206, 250), 0.8f));
        borderPen.setStyle(Paint.Style.STROKE);
        borderPen.setStrokeWidth(ScreenUtil.dp2px(context, 1)); // 1dp

        EXTEND_DISTENCE = ScreenUtil.dp2px(context, 12);        // 12dp
    }

    private float EXTEND_DISTENCE;                      // 点击检测的延伸长度，方便容易点击

    private boolean useAnimation;                       // 启用动画
    private boolean showBarBorder;                      // 显示图元边框

    private Rect container;                             // 图元容器
    private Paint pen;                                  // 图元画笔
    private Paint borderPen;                            // 图元边框画笔

    private Path barPath;                               // 图元路径
    private Path barBorderPath;                         // 图元边框路径
}
