package com.grsisfee.gfcharts.GraphicElements.Layout;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import com.grsisfee.gfcharts.GraphicElements.GraphicElements;
import com.grsisfee.gfcharts.Utils.ColorUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: GrsisFee
 * Date:   2016/2/9
 * Desc:   Layout图元是特殊的图元
 *         使用前需要首先调用addElement添加子图元，然后调用preDraw，最后调用onDraw绘制
 * All rights reserved.
 */
public abstract class Layout implements GraphicElements {

    public Layout (Rect container) {
        this.container = container;

        elements = new ArrayList<>();
        pens = new ArrayList<>();
        elementRects = new ArrayList<>();
    }

    /**
     * 根据统计数据和容器绘制区域大小，计算图元的位置和形状等坐标
     * @param pen           绘制图元的画笔
     * @param containerRect 容器绘制区域
     */
    @Override
    public void preDraw(Paint pen, Rect containerRect) {
        // 因为Layout图元本身是不显示的，所以此处的传入参数并没有使用，因此此处的传入参数可以为NULL
        for (int i = 0; i < elements.size(); i++) {
            elements.get(i).preDraw(pens.get(i), elementRects.get(i));
        }
    }

    /**
     * 绘制图元
     * @param canvas 绘制图元的容器
     */
    @Override
    public void onDraw(@NonNull Canvas canvas) {
        for (int i = 0; i < elements.size(); i++) {
            elements.get(i).onDraw(canvas);
        }
    }

    /**
     * 获得图元的最小大小
     * @param pen 此处传入画笔或NUll
     * @return 绘图的最小大小
     */
    @Override
    public Rect getElementMinSize(Paint pen) {
        return container;
    }

    /**
     * 获得布局中的某个图元
     * @param location 图元下标
     * @return 图元
     */
    public GraphicElements getElement(int location) {
        return elements.get(location);
    }

    /**
     * 获得布局中的某个图元矩形
     * @param location 图元下标
     * @return 图元矩形
     */
    public Rect getElementRect(int location) {
        return elementRects.get(location);
    }

    /**
     * 获得布局中的图元个数
     * @return 布局中的图元个数
     */
    public int getElementCount() {
        return elements == null ? 0 : elements.size();
    }

    protected void addElement(@NonNull GraphicElements element,
                              Paint pen,
                              @NonNull Rect elementRect) {
        elements.add(element);
        pens.add(pen);
        elementRects.add(elementRect);
    }

    protected void removeAllElements() {
        elements.clear();
        pens.clear();
        elementRects.clear();
    }

    protected Rect container;                                       // 盛放Layout的容器

    private int selectedElementLocation;                            // 选中的图元位置，初始为 -1
    private List<GraphicElements> elements;                         // Layout中的所有图元
    private List<Paint> pens;                                       // Layout中每个图元对应的画笔
    private List<Rect> elementRects;                                // Layout中每个图元相对于容器的位置
}
