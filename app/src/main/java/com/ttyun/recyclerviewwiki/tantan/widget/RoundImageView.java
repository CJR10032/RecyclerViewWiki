package com.ttyun.recyclerviewwiki.tantan.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.ttyun.recyclerviewwiki.R;

/**
 * 创建者     CJR
 * 创建时间   2017-08-15 14:55
 * 描述       http://www.jianshu.com/p/b873b717d1ea  从这个项目里拿过来的,
 * *          适配API 21以下的CardView的ImageView
 * *         https://www.jianshu.com/p/07097b562acb
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RoundImageView extends ImageView {

    private Path  mPath;
    private RectF mRectF;
    private float[] rids = new float[8];
    private PaintFlagsDrawFilter paintFlagsDrawFilter;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
         /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
        float mRadius = array.getDimension(R.styleable.RoundImageView_radius, 0);
        rids[0] = mRadius;      //  左上角圆角的x
        rids[1] = mRadius;      //  左上角圆角的y
        rids[2] = mRadius;
        rids[3] = mRadius;
        rids[4] = 0f;
        rids[5] = 0f;
        rids[6] = 0f;
        rids[7] = 0f;
        array.recycle();
        mPath = new Path();
        //  第一个参数是 指要清除的标志位, 第二个参数是设置抗锯齿 和 对位图进行滤波处理, mPaint.setBitmapFilter(true);
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint
                .FILTER_BITMAP_FLAG);   //  设置图形、图片的抗锯齿。可用于线条等。按位或.
        setLayerType(View.LAYER_TYPE_HARDWARE, null);       //  因为 clipPath 在api 18 之前不支持硬件加速,
        // 所以关闭硬件加速
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        //  CW clockwise 顺时针, CCW
        mPath.addRoundRect(mRectF, rids, Path.Direction.CW);
        // counter-clockwise 逆时针
        canvas.setDrawFilter(paintFlagsDrawFilter);
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
}
