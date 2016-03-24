package com.grsisfee.gfcharts.Utils;

import android.content.Context;

/**
 * Author: GrsisFee
 * Date:   2016/1/31
 * Desc:
 * All rights reserved.
 */
@SuppressWarnings("unused")
public class ScreenUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * @param context 环境上下文
     * @param dpValue 传入的DP
     * @return 转为PX
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * @param context 环境上下文
     * @param pxValue 传入的PX
     * @return 转为DP
     */
    public static float px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return pxValue / scale;
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
     * @param context 环境上下文
     * @param spValue 传入的sp
     * @return 转为PX
     */
    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     * @param context 环境上下文
     * @param pxValue 传入的sp
     * @return 转为PX
     */
    public static float px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return pxValue / scale;
    }
}
