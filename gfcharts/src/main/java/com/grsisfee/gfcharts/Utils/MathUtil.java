package com.grsisfee.gfcharts.Utils;

import android.support.annotation.NonNull;

/**
 * Author: GrsisFee
 * Date:   2016/2/5
 * Desc:
 * All rights reserved.
 */
public class MathUtil {

    /**
     * 取得数组中的最大值
     * @param array 传入数组
     * @return 返回该数组中的最大值
     */
    public static float getMaxValue(@NonNull float [] array) {
        float max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) max = array[i];
        }
        return max;
    }

    /**
     * 取得数组中的最小值
     * @param array 传入数组
     * @return 返回该数组中的最小值
     */
    public static float getMinValue(@NonNull float [] array) {
        float min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (min > array[i]) min = array[i];
        }
        return min;
    }

    /**
     * 取得数组中的最大值
     * @param array 传入数组
     * @return 返回该数组中的最大值
     */
    public static double getMaxValue(@NonNull double [] array) {
        double max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) max = array[i];
        }
        return max;
    }

    /**
     * 取得数组中的最小值
     * @param array 传入数组
     * @return 返回该数组中的最小值
     */
    public static double getMinValue(@NonNull double [] array) {
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (min > array[i]) min = array[i];
        }
        return min;
    }

    public static int limitValue(int value, int min, int max) {
        return (int) limitValue((double) value, (double) min, (double) max);
    }

    public static float limitValue(float value, float min, float max) {
        return (float) limitValue((double) value, (double) min, (double) max);
    }

    /**
     * 限制数据范围
     * @param value 待限制的数据
     * @param min 数据最小可取
     * @param max 数据最大可取
     * @return 返回限制后的数据
     */
    public static double limitValue(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
}
