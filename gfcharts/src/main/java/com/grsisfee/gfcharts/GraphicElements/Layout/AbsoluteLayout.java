package com.grsisfee.gfcharts.GraphicElements.Layout;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import com.grsisfee.gfcharts.GraphicElements.GraphicElements;

/**
 * Author： Grsis Fee
 * Date:    2016/2/17
 * Desc:
 * All rights reserved.
 */
public class AbsoluteLayout extends Layout {

    public AbsoluteLayout(Rect container) {
        super(container);
    }

    @Override
    public void addElement(GraphicElements element,
                           Paint pen,
                           @NonNull Rect elementRect) {
        if (element == null) return;                        // 不能加入空图元
        super.addElement(element, pen, elementRect);
    }

    @Override
    public void removeAllElements() {
        super.removeAllElements();
    }
}
