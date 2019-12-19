package com.ttyun.recyclerviewwiki.tantan.widget.trapezoid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 创建者     CJR
 * 创建时间   2017-08-23 15:27
 * 描述	      使用 BitmapShader 实现的梯形, ...渣渣啊, 要给梯形加圆角怎么办?? 让UI给切个图, 然后用Xfermode的scr_in模式吧
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TrapezoidViewBS extends ImageView {

    private Matrix mMatrix;     //  缩放用的Matrix
    private Paint  mPaint;
    private Paint  mBoardPaint;
    private Path   mPath;
    private Path   mBoardPath;

    private int mBorderWidth;       //  边框的宽度

    public TrapezoidViewBS(Context context) {
        this(context, null);
    }

    public TrapezoidViewBS(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrapezoidViewBS(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);

        mBoardPaint = new Paint();
        mBoardPaint.setAntiAlias(true);
        //  白色边框
        mBoardPaint.setColor(Color.WHITE);
        mBoardPaint.setStyle(Paint.Style.FILL);
        //  3px 的边框
        mBoardPaint.setStrokeWidth(10);

        mMatrix = new Matrix();

        mPath = new Path();
        //  边框的path
        mBoardPath = new Path();

        mBorderWidth = 8;
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
        // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，
        // 一定要大于我们view的宽高；所以我们这里取大值；
        float scale = Math.max(scaleX, scaleY);
        mMatrix.setScale(scale, scale);
        //  计算位移, 将图片移动到中心点
        if (scaleX < scaleY) {
            //  按照scaleY 缩放的, 那X肯定大, 所以要移动到X的中心
            //  postTranslate 是指在setScale 之后才translate, preTranslate 是在setScale 之前就translate
            // (2018-08-21 09:45 发现这个解释不对, 这里的post指的是矩阵的后乘操作)
            mMatrix.postTranslate((bmpWidth * scaleY - bmpWidth * scaleX) * 0.5f, 0);
        } else {
            //  按照scaleX 缩放的, 那Y肯定大, 所以要移动到Y的中心
            mMatrix.postTranslate(0, (bmpHeight * scaleX - bmpHeight * scaleY) * 0.5f);
        }

        //  缩放和平移  和旋转
        shader.setLocalMatrix(mMatrix);
        mPaint.setShader(shader);

        float h = getHeight() / 20f;

        //  内path
        mPath.reset();
        mPath.moveTo(mBorderWidth, h + mBorderWidth);
        mPath.lineTo(getWidth() - mBorderWidth, mBorderWidth);
        mPath.lineTo(getWidth() - mBorderWidth, getHeight() - mBorderWidth);
        mPath.lineTo(mBorderWidth, getHeight() - 2 * h - mBorderWidth);

        //  外path, 内外path之间形成白色边框
        mBoardPath.reset();
        mBoardPath.moveTo(0, h);
        mBoardPath.lineTo(getWidth(), 0);
        mBoardPath.lineTo(getWidth(), getHeight());
        mBoardPath.lineTo(0, getHeight() - 2 * h);

        //  在底层先画上 较大的 空白
        canvas.drawPath(mBoardPath, mBoardPaint);
        //  画上稍小的bitmap ,就出现边框的效果了
        canvas.drawPath(mPath, mPaint);
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
