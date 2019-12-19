package com.ttyun.recyclerviewwiki.tantan.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.util.TypedValue;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * 创建者     CJR
 * 创建时间   2017-08-16 16:30
 * 描述	     Glide 处理圆角用的, 夏雨博客那里拷贝过来的, 然而并无软用
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class CornersTransform extends BitmapTransformation {
    private float radius;

    public CornersTransform(Context context) {
        super(context);
        radius = 10;
    }

    public CornersTransform(Context context, float r) {
        super(context);
        this.radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, r, context.getResources().getDisplayMetrics());
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return cornersCrop(pool, toTransform);
    }

    private Bitmap cornersCrop(BitmapPool pool, Bitmap source) {
        if (source == null)
            return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        Log.e("vivi", String.format("radius = %s", radius));
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
