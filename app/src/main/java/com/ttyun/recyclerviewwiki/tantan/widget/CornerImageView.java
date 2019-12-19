package com.ttyun.recyclerviewwiki.tantan.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.ttyun.recyclerviewwiki.R;

/**
 * 创建者     CJR
 * 创建时间   2017-08-15 14:55
 * 描述       http://www.jianshu.com/p/b873b717d1ea  从这个项目里拿过来的,
 * *          适配API 21以下的CardView的ImageView
 * *         https://www.jianshu.com/p/07097b562acb
 */
public class CornerImageView extends AppCompatImageView {

    /**剪出了圆角的路径*/
    private Path mPath;
    /**记录控件大小的RectF*/
    private RectF mRectF;
    /**圆角的尺寸, 依次为左上角，右上角，右下角，左下角 的xy半径*/
    private float[] mCornerRadius = new float[8];
    /**PaintFlagsDrawFilter 实例*/
    private PaintFlagsDrawFilter mPaintFlagsDrawFilter;

    /**
     * 构造方法
     *
     * @param context 上下文
     */
    public CornerImageView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context 上下文
     * @param attrs   属性
     */
    public CornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CornerImageView);
        //  左上角圆角的半径
        final float leftTopRadius =
                typedArray.getDimension(R.styleable.CornerImageView_leftTopRadius, 0);
        //  右上角圆角的半径
        final float rightTopRadius =
                typedArray.getDimension(R.styleable.CornerImageView_rightTopRadius, 0);
        //   右下角圆角的半径
        final float rightBottomRadius =
                typedArray.getDimension(R.styleable.CornerImageView_rightBottomRadius, 0);
        //  左下角圆角的半径
        final float leftBottomRadius =
                typedArray.getDimension(R.styleable.CornerImageView_leftBottomRadius, 0);

        //  设置左上角圆角半径
        setLeftTopRadius(leftTopRadius)
                //  设置右上角圆角半径
                .setRightTopRadius(rightTopRadius)
                //  设置右下角圆角半径
                .setRightBottomRadius(rightBottomRadius)
                //  设置左下角圆角半径
                .setLeftBottomRadius(leftBottomRadius);

        //  回收资源
        typedArray.recycle();

        //  创建路径
        mPath = new Path();
        /*
         * 第一个参数是 指要清除的标志位, 第二个参数是设置抗锯齿 和 对位图进行滤波处理,
         * mPaint.setBitmapFilter(true);
         *
         *  设置图形、图片的抗锯齿。可用于线条等。按位或.
         */
        mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0,
                Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        //  因为 clipPath 在api 18 之前不支持硬件加速, 所以关闭硬件加速
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        //  CW clockwise 顺时针, CCW
        mPath.addRoundRect(mRectF, mCornerRadius, Path.Direction.CW);
        // counter-clockwise 逆时针
        canvas.setDrawFilter(mPaintFlagsDrawFilter);
        canvas.save();
        canvas.clipPath(mPath);
        super.onDraw(canvas);
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF = new RectF(0, 0, w, h);
    }

    /**
     * 设置左上角圆角半径
     *
     * @param leftTopRadius 左上角圆角半径
     * @return 当前对象, 形成链式调用
     */
    public CornerImageView setLeftTopRadius(float leftTopRadius) {
        //  左上角圆角的x
        mCornerRadius[0] = leftTopRadius;
        //  左上角圆角的y
        mCornerRadius[1] = leftTopRadius;
        return this;
    }

    /**
     * 设置右上角圆角半径
     *
     * @param rightTopRadius 右上角圆角半径
     * @return 当前对象, 形成链式调用
     */
    public CornerImageView setRightTopRadius(float rightTopRadius) {
        //  右上角圆角的x
        mCornerRadius[2] = rightTopRadius;
        //  右上角圆角的y
        mCornerRadius[3] = rightTopRadius;
        return this;
    }

    /**
     * 设置右下角圆角半径
     *
     * @param rightBottomRadius 右下角圆角半径
     * @return 当前对象, 形成链式调用
     */
    public CornerImageView setRightBottomRadius(float rightBottomRadius) {
        //  右下角圆角的x
        mCornerRadius[4] = rightBottomRadius;
        //  右下角圆角的y
        mCornerRadius[5] = rightBottomRadius;
        return this;
    }

    /**
     * 设置左下角圆角半径
     *
     * @param leftBottomRadius 左下角圆角半径
     * @return 当前对象, 形成链式调用
     */
    public CornerImageView setLeftBottomRadius(float leftBottomRadius) {
        //  左下角圆角的x
        mCornerRadius[6] = leftBottomRadius;
        //  左下角圆角的y
        mCornerRadius[7] = leftBottomRadius;
        return this;
    }

}
