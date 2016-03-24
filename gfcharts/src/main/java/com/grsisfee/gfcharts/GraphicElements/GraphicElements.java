package com.grsisfee.gfcharts.GraphicElements;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Author: GrsisFee
 * Date:   2016/2/3
 * Desc:   图元接口
 *         使用图元的基本方法是：先调用preDraw，再调用onDraw绘制图元
 * All rights reserved.
 */
public interface GraphicElements {
    /**
     * 根据统计数据和容器绘制区域大小，计算图元的位置和形状等坐标
     * @param pen 绘制图元的画笔
     * @param containerRect 容器绘制区域
     */
    void preDraw(Paint pen, Rect containerRect);

    /**
     * 绘制图元
     * @param canvas 绘制图元的容器
     */
    void onDraw(Canvas canvas);

    /**
     * 获得图元的最小大小
     * @param pen 绘制图元的画笔
     * @return 绘图的最小大小
     * 注意：有些的图元的最小大小，与绘制图元画笔无关，则此处传入null亦可
     */
    Rect getElementMinSize(Paint pen);
}
