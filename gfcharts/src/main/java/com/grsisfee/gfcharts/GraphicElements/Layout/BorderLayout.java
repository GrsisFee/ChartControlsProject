package com.grsisfee.gfcharts.GraphicElements.Layout;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import com.grsisfee.gfcharts.GraphicElements.GraphicElements;

/**
 * Author: GrsisFee
 * Date:   2016/2/9
 * Desc:
 * All rights reserved.
 */
public class BorderLayout extends Layout {

    public BorderLayout(@NonNull Rect container) {
        super(container);
        initValues();
    }

    public void addElement(GraphicElements element, Paint pen) {
        addElement(element, pen, POSITION_CENTER);
    }

    public void addElement(GraphicElements element,
                           Paint pen, int position) throws RuntimeException {
        if (element == null) return;                                            // 不能加入空图元
        if (position < 0 || position > 4)
            throw new RuntimeException("图元加入图元布局容器的位置错误。");

        elements[position] = element;
        pens[position] = pen;
        elementRects[position] = element.getElementMinSize(pen);

        reCalculateRect();
        addElementToLayout();
    }

    private void addElementToLayout() {
        // 先清除图元
        removeAllElements();

        for (int i = 0; i < 5; i++) {
            if (elements[i] != null)
                super.addElement(elements[i], pens[i], elementRects[i]);
        }
    }

    private void reCalculateRect() {

        int l, t, r, b;
        l = container.left;
        t = container.top;
        r = container.right;
        b = container.bottom;

        elementRects[4] = new Rect(l, b - elementRects[4].height(), r, b);      // 底
        elementRects[2] = new Rect(l, t, r, t + elementRects[2].height());      // 顶
        elementRects[3] = new Rect(
                r - elementRects[3].width(),
                t + elementRects[2].height(),
                r,
                b - elementRects[4].height());                                  // 右
        elementRects[1] = new Rect(
                l,
                t + elementRects[2].height(),
                l + elementRects[1].width(),
                b - elementRects[4].height());                                  // 左
        elementRects[0] = new Rect(
                l + elementRects[1].width(),
                t + elementRects[2].height(),
                r - elementRects[3].width(),
                b - elementRects[4].height());                                  // 中
    }

    private void initValues() {
        // 该种图元布局最多只能容纳5个图元，5个图元在数组中的顺序为：中 左 顶 右 底
        elements = new GraphicElements[5];
        pens = new Paint[5];
        elementRects = new Rect[5];
        // 初始化布局中的所有图元
        for (int i = 0; i < 5; i++) {
            elements[i] = null;
            pens[i] = null;
            elementRects[i] = new Rect(0, 0, 0, 0);
        }
    }

    public static final int POSITION_CENTER = 0;                           // 中
    public static final int POSITION_LEFT = 1;                             // 左
    public static final int POSITION_TOP = 2;                              // 顶
    public static final int POSITION_RIGHT = 3;                            // 右
    public static final int POSITION_BOTTOM = 4;                           // 底

    private GraphicElements [] elements;
    private Paint [] pens;
    private Rect [] elementRects;
}
