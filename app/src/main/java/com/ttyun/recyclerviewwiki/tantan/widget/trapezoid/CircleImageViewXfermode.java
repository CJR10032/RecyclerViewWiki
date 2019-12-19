package com.ttyun.recyclerviewwiki.tantan.widget.trapezoid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ttyun.recyclerviewwiki.R;

/**
 * 创建者     CJR
 * 创建时间   2017-08-24 14:28
 * 描述	      使用 Xfermode 画图形坑太多... 目前这个还是没成功的, 推荐使用BitmapShader
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class CircleImageViewXfermode extends ImageView {

    private Paint              mPaint;
    private PorterDuffXfermode mXfermode;

    public CircleImageViewXfermode(Context context) {
        this(context, null);
    }

    public CircleImageViewXfermode(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageViewXfermode(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OVER);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //        super.onDraw(canvas);
        //        Bitmap bmp = drawCircleImageView();
        Bitmap bmp = drawMaskImageView();
        if (bmp != null)
            canvas.drawBitmap(bmp, 0, 0, null);
    }

    private Bitmap drawCircleImageView() {
        Drawable drawable = getDrawable();
        if (drawable == null)
            return null;
        Bitmap source = drawableToBitmap(drawable);
        int min = Math.min(getWidth(), getHeight());
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);     //  产生一个画布
        mPaint.setXfermode(null);
        canvas.drawCircle(getWidth() * 0.5f, getHeight() * 0.5f, min * 0.5f, mPaint);       //  先画一个圆
        mPaint.setXfermode(mXfermode);      //  使用SRC_IN模式
        canvas.drawBitmap(source, 0, 0, mPaint);
        return target;
    }

    private Bitmap drawMaskImageView() {
        Drawable drawable = getDrawable();
        if (drawable == null)
            return null;
        int width = getWidth();
        int height = getHeight();
        Bitmap source = drawableToBitmap(drawable);
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);     //  产生一个画布

        Drawable mask = getMaskDrawable(getContext(), R.drawable.mask_flower);
        mask.setBounds(0, 0, width, height);
        mask.draw(canvas);
        mPaint.setXfermode(mXfermode);      //  使用SRC_IN模式
        canvas.drawBitmap(source, 0, 0, mPaint);
        return target;
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    private Drawable getMaskDrawable(Context context, int maskId) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getDrawable(maskId);
        } else {
            drawable = context.getResources().getDrawable(maskId);
        }

        if (drawable == null) {
            throw new IllegalArgumentException("maskId is invalid");
        }

        return drawable;
    }
}
