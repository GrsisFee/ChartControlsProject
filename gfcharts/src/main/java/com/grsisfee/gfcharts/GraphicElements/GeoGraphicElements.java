package com.grsisfee.gfcharts.GraphicElements;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

/**
 * Author: GrsisFee
 * Date:   2016/2/15
 * Desc:   需要响应点击操作的几何图元
 * All rights reserved.
 */
public interface GeoGraphicElements extends GraphicElements {

    /**
     * 判断屏幕上的一点是否包含在图元内
     * @param screenPt 屏幕上的像素点
     * @return true：包含
     *         false：不包含
     */
    boolean contains(Point screenPt);
}
