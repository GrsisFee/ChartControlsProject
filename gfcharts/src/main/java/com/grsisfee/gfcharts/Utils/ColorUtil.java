package com.grsisfee.gfcharts.Utils;

import android.graphics.Color;

/**
 * Author： Grsis Fee
 * Date:    2016/2/17
 * Desc:
 * All rights reserved.
 */
public class ColorUtil {

    /**
     * 根据比值获得颜色对应的深色
     * @param color 传入的颜色
     * @param ratio 色深比率
     * @return 返回传入颜色对应的深色
     */
    public static int getDarkerColor(int color, float ratio) {
        ratio = MathUtil.limitValue(ratio, 0, 1);

        return Color.argb(
                Color.alpha(color),
                (int) (Color.red(color) * ratio),
                (int) (Color.green(color) * ratio),
                (int) (Color.blue(color) * ratio)
        );
    }

    /**
     * 根据比值获得颜色对应的浅色
     * @param color 传入的颜色
     * @param ratio 色浅比率
     * @return 返回传入颜色对应的浅色
     */
    @Deprecated
    public static int getBrighterColor(int color, float ratio) {
        ratio = MathUtil.limitValue(ratio, 0, 1);

        return Color.argb(
                Color.alpha(color),
                (int) (Color.red(color) / ratio),
                (int) (Color.green(color) / ratio),
                (int) (Color.blue(color) / ratio)
        );
    }
}
