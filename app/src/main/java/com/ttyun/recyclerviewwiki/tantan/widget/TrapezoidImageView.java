package com.ttyun.recyclerviewwiki.tantan.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * 创建者     CJR
 * 创建时间   2017-08-22 16:51
 * 描述	      通过clipPath 画出的梯形
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TrapezoidImageView extends ImageView {
    private Path                 mPath;
    private RectF                mRectF;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;

    public TrapezoidImageView(Context context) {
        this(context, null);
    }

    public TrapezoidImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrapezoidImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        mPath = new Path();
        //  第一个参数是 指要清除的标志位, 第二个参数是设置抗锯齿 和 对位图进行滤波处理, mPaint.setBitmapFilter(true);
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);   //  设置图形、图片的抗锯齿。可用于线条等。按位或.
        mRectF = new RectF();
        setLayerType(View.LAYER_TYPE_HARDWARE, null);       //  因为 clipPath 在api 18 之前不支持硬件加速, 所以关闭硬件加速
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        //        mPath.addRoundRect(mRectF, rids, Path.Direction.CW);    //  CW clockwise 顺时针, CCW counter-clockwise 逆时针
        int h = 20;
        mPath.moveTo(0, h);
        mPath.lineTo(getWidth(), 0);
        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(0, getHeight() - 2 * h);
        canvas.setDrawFilter(paintFlagsDrawFilter);
        canvas.save();
        canvas.clipPath(mPath);

        super.onDraw(canvas);
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.set(0, 0, w, h);
    }
}
