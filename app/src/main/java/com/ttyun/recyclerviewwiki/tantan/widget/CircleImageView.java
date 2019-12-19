package com.ttyun.recyclerviewwiki.tantan.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 创建者     CJR
 * 创建时间   2017-08-24 14:26
 * 描述	      使用Shader 实现圆形图片
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class CircleImageView extends ImageView {

    private Matrix mMatrix;     //  缩放用的Matrix
    private Paint  mPaint;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //        super.onDraw(canvas);
        shaderDraw(canvas);
    }

    private void shaderDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null)
            return;

        Bitmap bmp = drawableToBitmap(drawable);
        BitmapShader shader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();

        float scaleX = getWidth() * 1f / bmpWidth;
        float scaleY = getHeight() * 1f / bmpHeight;
        // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
        float scale = Math.max(scaleX, scaleY);
        mMatrix.setScale(scale, scale);
        //  计算位移, 将图片移动到中心点
        if (scaleX < scaleY) {  //  按照scaleY 缩放的, 那X肯定大, 所以要移动到X的中心
            mMatrix.postTranslate((bmpWidth * scaleY - bmpWidth * scaleX) * 0.5f, 0);        //  postTranslate 是指在setScale 之后才translate, preTranslate 是在setScale 之前就translate
        } else {    //  按照scaleX 缩放的, 那Y肯定大, 所以要移动到Y的中心
            mMatrix.postTranslate(0, (bmpHeight * scaleX - bmpHeight * scaleY) * 0.5f);
        }
        //  缩放和平移
        shader.setLocalMatrix(mMatrix);
        mPaint.setShader(shader);

        float radiusX = getWidth() * 0.5f;
        float radiusY = getHeight() * 0.5f;
        float radius = Math.min(radiusX, radiusY);
        canvas.drawCircle(radius, radius, radius, mPaint);
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
}