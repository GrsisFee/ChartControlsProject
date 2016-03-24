package com.grsisfee.gfcharts.BarChart;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.grsisfee.gfcharts.Base.BaseChart;
import com.grsisfee.gfcharts.Base.StatisticData;
import com.grsisfee.gfcharts.GraphicElements.Bar.VerticalBar;
import com.grsisfee.gfcharts.GraphicElements.CoordinateAxis.AxisOY;
import com.grsisfee.gfcharts.GraphicElements.GeoGraphicElements;
import com.grsisfee.gfcharts.GraphicElements.Label.Label;
import com.grsisfee.gfcharts.GraphicElements.Layout.AbsoluteLayout;
import com.grsisfee.gfcharts.GraphicElements.Layout.BorderLayout;
import com.grsisfee.gfcharts.GraphicElements.Legend.BarLegend;
import com.grsisfee.gfcharts.R;
import com.grsisfee.gfcharts.Utils.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: GrsisFee
 * Date:   2016/1/31
 * Desc:
 * All rights reserved.
 */
public class VerticalBarChart extends BaseChart implements GestureDetector.OnGestureListener {
    public VerticalBarChart(Context context) {
        super(context);
        initControl(context);
    }

    public VerticalBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
        initAttrbutes(attrs);
    }

    public VerticalBarChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
        initAttrbutes(attrs);
    }

    /**
     * 设置统计数据
     * @param data 统计数据
     */
    public void setData(List<StatisticData> data) {
        coordinate.setData(data);                                                   // 为图元赋值统计图数据
        this.data = data;
    }

    /**
     * 显示图例
     * @param show true 显示图例
     *             false 不显示图例
     */
    public void showLegend(boolean show) {
        if (show) {
            legend = new BarLegend(context);
            legend.setOrientation(true);
        } else {
            legend = null;
        }
    }

    /**
     * 设置x刻度旋转角度
     * @param xTickAngle 角度
     */
    public void setxTickAngle(float xTickAngle) {
        this.xTickAngle = xTickAngle;
        coordinate.setXTickAngle(xTickAngle);
    }

    /**
     * 设置统计图标题
     * @param titleStr 统计图标题
     */
    public void setTitle(String titleStr) {
        if (title != null) title = null;
        title = new Label(context, titleStr);
    }

    /**
     * 设置统计图x轴方向标签文字
     * @param xLabelStr x轴方向标签文字
     */
    public void setxLabel(String xLabelStr) {
        if (xLabel != null) xLabel = null;
        xLabel = new Label(context, xLabelStr);
        xLabel.setLabelDirection(Label.TEXT_DIRECTION_BOTTOM_HORIZONTAL);
    }

    /**
     * 设置统计图y轴方向标签文字
     * @param yLabelStr y轴方向标签文字
     */
    public void setyLabel(String yLabelStr) {
        if (yLabel != null) yLabel = null;
        yLabel = new Label(context, yLabelStr);
        yLabel.setLabelDirection(Label.TEXT_DIRECTION_LEFT_VERTICAL);
    }

    /**
     * 设置标题画笔
     * @param titlePaint 标题画笔
     */
    public void setTitlePaint(Paint titlePaint) {
        this.titlePaint = titlePaint;
    }

    /**
     * 设置x,y轴方向的标签文字画笔
     * @param labelPaint x,y轴方向的标签文字画笔
     *                   如果传入null，则会使用默认的画笔
     */
    public void setLabelPaint(Paint labelPaint) {
        this.labelPaint = labelPaint;
    }

    /**
     * 设置坐标轴画笔
     * @param coordinatePaint 坐标轴画笔
     */
    public void setCoordinatePaint(Paint coordinatePaint) {
        coordinatePaint.setStyle(Paint.Style.STROKE);
        coordinatePaint.setStrokeWidth(DP_1);
        this.coordinatePaint = coordinatePaint;
    }

    /**
     * 设置柱状颜色
     * @param barColor 柱状颜色
     */
    public void setBarColor(int [] barColor) {
        this.barColor = barColor;
    }

    /**
     * 设置是否显示y轴方向的坐标小刻度
     * @param show true 显示
     *             false 不显示
     */
    public void showSmallTick(boolean show) {
        if (coordinate == null) return;
        coordinate.setShowSmallTick(show);
    }

    /**
     * 外部手动通知重绘统计图
     */
    public void redraw() {
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (data != null) {
            resetLegend();

            Rect containerRect = new Rect(0, 0, width, height);
            layout = new BorderLayout(containerRect);

            layout.addElement(title, titlePaint, BorderLayout.POSITION_TOP);
            layout.addElement(yLabel, labelPaint, BorderLayout.POSITION_LEFT);
            layout.addElement(xLabel, labelPaint, BorderLayout.POSITION_BOTTOM);
            layout.addElement(legend, null, BorderLayout.POSITION_RIGHT);
            layout.addElement(coordinate, coordinatePaint);

            layout.preDraw(null, null);
            clipRect = coordinate.getClipRect();

            calculateBarThickness();
            calculateBarPlacement();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (layout != null) layout.onDraw(canvas);

        canvas.clipRect(clipRect);
        canvas.save();
        canvas.translate(translateX, 0);
        if (absoluteLayout != null) absoluteLayout.onDraw(canvas);
        if (!enoughSpace) drawXTicks(canvas);
        canvas.restore();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 屏蔽父控件拦截onTouchEvent事件的行为。这句代码十分重要
        getParent().requestDisallowInterceptTouchEvent(true);
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (flingAnimator != null && flingAnimator.isRunning()) flingAnimator.cancel();
        return true;                                                   // 需要继续处理触摸事件
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        motionUpEvent(new PointF(e.getX(), e.getY()));
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        translateX -= distanceX;
        limitTranslateX();
        postInvalidate();
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(velocityX) > 100) {
            int duration = 500;
            velocityX /= duration * 4.5;
            flingAnimator = ValueAnimator.ofInt(translateX, (int) (translateX + velocityX * duration));
            flingAnimator.setDuration(duration);
            flingAnimator.setInterpolator(new DecelerateInterpolator());
            flingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    translateX = (int) flingAnimator.getAnimatedValue();
                    if (limitTranslateX())
                        flingAnimator.cancel();
                    invalidate();
                }
            });
            flingAnimator.start();
        }
        return false;
    }

    /**
     * 触摸释放后的响应
     */
    private void motionUpEvent(PointF actionUpPt) {
        if (clipRect.contains(actionUpPt.x, actionUpPt.y))
            showTooltip(new Point((int) actionUpPt.x - translateX, (int) actionUpPt.y));
    }

    /**
     * 绘制X轴方向的刻度
     * @param canvas Canvas对象
     */
    private void drawXTicks(Canvas canvas) {
        String [] xTicks = coordinate.getxStrCoordinate();
        if (xTicks == null || xTicks.length <= 0) return;

        Paint tickPen = coordinate.tickPen;
        float tickPenAscent = -1 * coordinate.calPaintAscent(tickPen);
        float tickStrVPosition = coordinate.getChartRect().bottom + coordinate.SUGGEST_DIS_TICK_AXIS;

        if (xTickAngle != 0)
            for (int i = 0; i < xTicks.length; i++) {
                canvas.save();
                canvas.translate(barBasePoints[i].x, tickStrVPosition);
                canvas.rotate(xTickAngle);
                canvas.drawText(xTicks[i], 0, tickPenAscent, tickPen);
                canvas.restore();
            }
        else {
            double [][] xTicksSize = coordinate.getCoordinateTicksSize(tickPen, xTicks, xTickAngle);
            for (int i = 0; i < xTicks.length; i++) {
                canvas.drawText(
                        xTicks[i],
                        (float) (barBasePoints[i].x - xTicksSize[0][i] / 2f),
                        tickStrVPosition + tickPenAscent,
                        tickPen);
            }
        }
    }

    /**
     * 计算柱状宽度
     */
    private void calculateBarThickness() {
        int barCount = coordinate.getxStrCoordinate().length;
        RectF chartRect = coordinate.getChartRect();
        barThickness = (chartRect.width() - DP_1 * 3 * (barCount - 1)) / barCount;
        if (barThickness < SUGGEST_SMALLEST_BAR_THICKNESS) {
            barThickness = SUGGEST_SMALLEST_BAR_THICKNESS;
        } else if (barThickness > SUGGEST_BIGGEST_BAR_THICKNESS) {
            barThickness *= 0.7f;
        }
    }

    /**
     * 计算柱状摆放位置
     */
    private void calculateBarPlacement() {
        final int barCount = coordinate.getxStrCoordinate().length;
        barBasePoints = coordinate.getOxTickBasePoint();
        float ratio = coordinate.getAxisRatio(true);
        double yMinTick = coordinate.getOriginalPoint()[1].y;

        // 重新计算柱状基准点
        barBasePoints = recalculateBarBasePoints(barBasePoints);
        // 设置coordinateX轴刻度的显示或隐藏
        coordinate.showXTickString(enoughSpace);

        Rect barRc;
        absoluteLayout = new AbsoluteLayout(null);

        final List<Animator> animators = new ArrayList<>();
        Paint barPaint;

        for (int i = 0; i < barCount; i++) {
            barRc = new Rect(
                    (int) (barBasePoints[i].x - barThickness / 2),
                    (int) (barBasePoints[i].y -
                            (Float.parseFloat(data.get(i).getValue().toString()) - yMinTick) * ratio),
                    (int) (barBasePoints[i].x + barThickness / 2),
                    (int) (barBasePoints[i].y)
            );

            final VerticalBar bar = new VerticalBar(context, true);
            barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            barPaint.setStyle(Paint.Style.FILL);
            barPaint.setColor(barColor[i % barColor.length]);
            absoluteLayout.addElement(bar, barPaint, barRc);

            final ValueAnimator valueAnimator = ValueAnimator.ofInt(barRc.bottom, barRc.top);
            valueAnimator.setDuration(500);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    bar.update((int) valueAnimator.getAnimatedValue());
                    invalidate();
                }
            });
            valueAnimator.setStartDelay(100 * i);
            animators.add(valueAnimator);
        }

        if (barCount > 0) {
            absoluteLayout.preDraw(null, null);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animators);
            animatorSet.start();
        }

    }

    /**
     * 重新计算柱状基准点，用于更新左右滑动后的柱状位置
     * @param barBasePoints 基准点
     * @return 柱状基准点
     */
    private PointF [] recalculateBarBasePoints(PointF [] barBasePoints) {
        enoughSpace = true;
        translateX = maxTranslateX = 0;

        if (barBasePoints == null || barBasePoints.length <= 1) return barBasePoints;       // 注意此处是<=1，仅一条柱状是不需要滑动功能的

        final float suggestBarDis = DP_1 * 3;
        float barDis = Math.abs(barBasePoints[1].x - barBasePoints[0].x) - barThickness;

        if (barDis <= suggestBarDis) {                                                      // 如果运行到了这儿，说明柱状过多，已经无法摆下了
            enoughSpace = false;
            barDis = suggestBarDis;
            int barCount = barBasePoints.length;
            final float startBarX = coordinate.getChartRect().left + barThickness / 2f;
            for (int i = 0; i < barCount; i++) {
                barBasePoints[i].x = (barThickness + barDis) * i + startBarX;
            }
            // 计算x方向的最大偏移
            float barLayoutWidth = (barThickness + barDis) * barCount - barDis;
            maxTranslateX = (int) (coordinate.getChartRect().width() - barLayoutWidth);
        }

        return barBasePoints;
    }

    /**
     * 组织显示提示
     * @param actionUpPt 触摸弹起点
     */
    private void showTooltip(Point actionUpPt) {
        if (absoluteLayout == null) return;

        int elementCount = absoluteLayout.getElementCount();
        for (int i = 0; i < elementCount; i++) {
            GeoGraphicElements element = (GeoGraphicElements) absoluteLayout.getElement(i);

            if (element.contains(actionUpPt)) {
                Rect elementRc = absoluteLayout.getElementRect(i);
                showTooltipWindow(
                        String.valueOf(coordinate.getyNumCoordinate()[i]),
                        elementRc.left + translateX,
                        elementRc.top,
                        elementRc.top <= elementRc.bottom);

                break;
            }
        }
    }

    /**
     * 初始化tooltip窗口
     */
    private void initTooltipWindow() {
        tooltipTextView = new TextView(context);
        tooltipTextView.setPadding(16, 8, 16, 8);
        tooltipTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        tooltipTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f);
        tooltipTextView.setTextColor(Color.WHITE);

        tooltipWindow = new PopupWindow(
                tooltipTextView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                false);
        tooltipWindow.setTouchable(true);
        tooltipWindow.setOutsideTouchable(true);
        tooltipWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.tooltip_background_drawable));
    }

    /**
     * 以传入的top，left做为基准点，显示tooltip窗口
     * @param tip 待显示的提示信息
     * @param left 基准点横坐标
     * @param top 基准点纵坐标
     * @param up 柱状向上则应传入true，否则传入false
     */
    private void showTooltipWindow(String tip, int left, int top, boolean up) {
        closeTooltipWindow();

        initTooltipWindow();
        tooltipTextView.setText(tip);

        // 添加完成待显示内容后，强制绘制tooltipWindow，并获得其宽高
        tooltipWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int tooltipWidth = tooltipWindow.getContentView().getMeasuredWidth();
        int tooltipHeight = tooltipWindow.getContentView().getMeasuredHeight();

        int xof = (int) ((barThickness - tooltipWidth) / 2f) + left - 1;
        int yof = top;

        // 上下限制 yof，使tooltipWindow上下方向不会超过统计图区域
        RectF chartRect = coordinate.getChartRect();
        if (up) {
            if (yof - tooltipHeight < chartRect.top + 8) yof = -height + 8 + yof;
            else yof = -height - tooltipHeight - 8 + yof;
        } else {
            if (yof + tooltipHeight > chartRect.bottom - 8) yof = -height - tooltipHeight - 8 + yof;
            else yof = -height + 8 + yof;
        }
        tooltipWindow.showAsDropDown(this, xof, yof);
    }

    /**
     * 显示tooltip窗口，并且使tooltip窗口矩形中心和传入的矩形中心重合
     * @param tip 待显示的提示信息
     * @param barRc 确定tooltip窗口显示的位置
     */
    private void showTooltipWindow(String tip, Rect barRc) {
        closeTooltipWindow();

        initTooltipWindow();
        tooltipTextView.setText(tip);

        // 添加完成待显示内容后，强制绘制tooltipWindow，并获得其宽高
        tooltipWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int tooltipWidth = tooltipWindow.getContentView().getMeasuredWidth();
        int tooltipHeight = tooltipWindow.getContentView().getMeasuredHeight();

        int xof = (int) ((barThickness - tooltipWidth) / 2f) + barRc.left - 1;
        int yof = -height + barRc.top + (int) ((barRc.bottom - barRc.top - tooltipHeight) / 2f);

        tooltipWindow.showAsDropDown(this, xof, yof);
    }

    /**
     * 关闭并清理tooltip窗口
     */
    private void closeTooltipWindow() {
        if (tooltipWindow != null) {
            tooltipWindow.dismiss();
            tooltipWindow = null;
        }
    }

    /**
     * 限制X方向的位移
     */
    private boolean limitTranslateX() {
        if (translateX > 0) {
            translateX = 0;
            return true;
        }
        if (translateX < maxTranslateX) {
            translateX = maxTranslateX;
            return true;
        }
        return false;
    }

    /**
     * 重置图例
     */
    private void resetLegend() {
        if (legend == null || coordinate == null) return;
        legend.setColorClassify(barColor, coordinate.getxStrCoordinate());
    }

    /**
     * 初始化控件设置默认参数
     * @param context 控件环境
     */
    private void initControl(final Context context) {
        this.context = context;
        data = null;
        absoluteLayout = null;
        enoughSpace = true;
        barBasePoints = null;
        clipRect = null;
        xTickAngle = 0;
        maxTranslateX = translateX = 0;

        flingAnimator = null;

        coordinate = new AxisOY(context);

        title = xLabel = yLabel = null;
        titlePaint = labelPaint = null;

        legend = null;

        // 初始化坐标轴画笔
        coordinatePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        coordinatePaint.setStyle(Paint.Style.STROKE);
        coordinatePaint.setColor(Color.BLACK);
        coordinatePaint.setStrokeWidth(DP_1);

        SUGGEST_SMALLEST_BAR_THICKNESS = DP_1 * 18;
        SUGGEST_BIGGEST_BAR_THICKNESS = DP_1 * 40;
        barThickness = SUGGEST_SMALLEST_BAR_THICKNESS;

        // 初始化默认柱状颜色
        barColor = new int[] {
                Color.argb(225, 135, 206, 250),
                Color.argb(225, 149, 240, 173),
                Color.argb(225, 250, 203, 165),
                Color.argb(225, 229, 193, 201)
        };

        // 初始化手势探测器
        gestureDetector = new GestureDetector(context, this);
    }

    /**
     * 获得并设置控件的属性
     * @param attrs 控件属性集
     */
    private void initAttrbutes(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalBarChart);

        // 设置x刻度旋转角度
        xTickAngle = MathUtil.limitValue(
            typedArray.getFloat(R.styleable.VerticalBarChart_xTickAngle, 0f), 0f, 90f
        );
        coordinate.setXTickAngle(xTickAngle);

        // 设置统计图标题
        String titleStr = typedArray.getString(R.styleable.VerticalBarChart_title);
        if (titleStr != null && !titleStr.isEmpty())
            title = new Label(context, titleStr);

        // 设置x轴标签
        String xLabelStr = typedArray.getString(R.styleable.VerticalBarChart_xLabel);
        if (xLabelStr != null && !xLabelStr.isEmpty()) {
            xLabel = new Label(context, xLabelStr);
            xLabel.setLabelDirection(Label.TEXT_DIRECTION_BOTTOM_HORIZONTAL);
        }

        // 设置y轴标签
        String yLabelStr = typedArray.getString(R.styleable.VerticalBarChart_yLabel);
        if (yLabelStr != null && !yLabelStr.isEmpty()) {
            yLabel = new Label(context, yLabelStr);
            yLabel.setLabelDirection(Label.TEXT_DIRECTION_LEFT_VERTICAL);
        }



        typedArray.recycle();
    }

    private Context context;
    private PopupWindow tooltipWindow;                                 // tip弹窗
    private TextView tooltipTextView;                                  // tipTextView

    private List<StatisticData> data;                                  // 统计数据

    private Paint coordinatePaint;                                     // 坐标轴画笔
    private AxisOY coordinate;                                         // 坐标轴图元

    private Paint titlePaint;                                          // 标题画笔
    private Paint labelPaint;                                          // x,y方向标签画笔

    private Label title;                                               // 标题标签
    private Label xLabel;
    private Label yLabel;

    private BarLegend legend;                                          // 图例

    private BorderLayout layout;
    private AbsoluteLayout absoluteLayout;

    private float SUGGEST_SMALLEST_BAR_THICKNESS;
    private float SUGGEST_BIGGEST_BAR_THICKNESS;
    private float barThickness;
    private int [] barColor;                                            // 柱状颜色

    private boolean enoughSpace;                                        // 指示摆放柱状的空间是否足够
    private float xTickAngle;                                           // x轴刻度旋转的角度
    private PointF [] barBasePoints;                                    // 柱状的基准点
    private int translateX;                                             // x方向偏移
    private int maxTranslateX;                                          // x方向最大偏移
    private RectF clipRect;                                             // 裁剪矩形
    private ValueAnimator flingAnimator;

    private GestureDetector gestureDetector;                            // 手势探测器
}
