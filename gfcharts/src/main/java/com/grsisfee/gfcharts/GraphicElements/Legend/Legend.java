package com.grsisfee.gfcharts.GraphicElements.Legend;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import com.grsisfee.gfcharts.GraphicElements.GraphicElements;
import com.grsisfee.gfcharts.Utils.MathUtil;
import com.grsisfee.gfcharts.Utils.ScreenUtil;

/**
 * Author: GrsisFee
 * Date:   2016/3/4
 * Desc:   图例基类
 * All rights reserved.
 */
public abstract class Legend implements GraphicElements {

    public Legend(Context context) {
        // 初始设置参数默认值
        orientation = false;
        color = null;
        classify = null;
        this.context = context;

        DP_1 = ScreenUtil.dp2px(context, 1f);
    }

    /**
     * 设置图例颜色和分类数据
     * @param color 图例颜色
     * @param classify 分类数据
     *                 备注：
     *                 1、若color.length>classify.length则忽略多出来的图例颜色
     *                 2、若color.length<classify.length则重复循环使用图例颜色
     *                 3、需保证传入该函数的color与classify已经是对应顺序传入
     */
    public void setColorClassify(@NonNull int [] color, @NonNull String [] classify) {
        int length = Math.min(color.length, classify.length);
        this.color = new int[length];
        this.classify = new String[length];

        for (int i = 0; i < length; i++) {
            this.color[i] = color[i];
            this.classify[i] = classify[i];
        }

        if (color.length < classify.length) {
            for (int i = color.length; i < classify.length; i++) {
                this.classify[i % length] += "," + classify[i];
            }
        }
    }

    /**
     * 设置图例摆放方向
     * @param orientation true 垂直摆放
     *                    false 水平摆放
     */
    public void setOrientation(boolean orientation) {
        this.orientation = orientation;
    }

    /**
     * 计算分类数据宽度
     * @param pen 分类数据画笔
     * @return 分类数据宽度
     */
    protected float [] calClassifyWidth(@NonNull Paint pen) {
        float [] classifyWidth = new float[classify.length];
        for (int i = 0; i < classify.length; i++) {
            classifyWidth[i] = pen.measureText(classify[i]);
        }
        return classifyWidth;
    }

    /**
     * 计算画笔从baseline到文字最高点的距离ascent
     * 因为是相对于baseline的，所以这个值是负值
     * @param pen 画笔
     * @return 返回画笔ascent值
     */
    protected float calPaintAscent(@NonNull Paint pen) {
        Paint.FontMetrics fontMetrics = pen.getFontMetrics();
        return fontMetrics.ascent;
    }

    /**
     * 计算分类数据高度
     * @param pen 分类数据画笔
     * @return 分类数据高度
     */
    protected float [] calClassifyHeight(@NonNull Paint pen) {
        float [] classifyHeight = new float[classify.length];
        for (int i = 0; i < classify.length; i++) {
            Rect textRc = new Rect();
            pen.getTextBounds(classify[i], 0, classify[i].length(), textRc);
            classifyHeight[i] = textRc.height();
        }
        return classifyHeight;
    }


    protected boolean orientation;                              // 图例摆放方向
    // 此处的图例颜色和分类数据是长度和顺序一一对应的
    protected int [] color;                                     // 图例颜色
    protected String [] classify;                               // 分类数据

    protected float DP_1;                                       // 1dp 对应的 px

    private Context context;                                    // 环境上下文
}
