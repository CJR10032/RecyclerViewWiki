package com.ttyun.recyclerviewwiki.hive.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.ttyun.recyclerviewwiki.R;

/**
 * 创建者     CJR
 * 创建时间   2017-08-29 09:58
 * 描述	     蜂窝 RecyclerView用的正六边形, 正六边形的特殊性, 所以width 最好要等于 height 这里先不画边框,
 * *         关于点击事件, 网上有一种做法是 以中心为圆心, 边长 * 2分之根号3 为半径, 这个区域内的点击有效, 这样会去掉一些边角的事件
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HiveView extends ImageView {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL   = 1;

    private Matrix mMatrix;     //  缩放用的Matrix
    private Paint  mPaint;
    //    private Paint  mBoardPaint;
    private Path   mPath;
    private float  mSideLength;
    private float  mGap;        //  正六边形的空隙(竖直的 是和 width 的空隙, 水平的是和 height 之间的空隙, 这里其实不算空隙也可以, 画path的时候以 中间开始算就可以 )
    //    private Path   mBoardPath;

    //    private int mBorderWidth;       //  边框的宽度

    private int mOrientation;

    private OnHiveClickListener mHiveClickListener;
    private float               mDownX, mDownY;
    //    private int mSlop;      //  临界值
    private boolean mClick = false;

    public HiveView(Context context) {
        this(context, null);
    }

    public HiveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.HiveView);
        mOrientation = array.getInt(R.styleable.HiveView_orientation, HORIZONTAL);        //  默认是horizontal 方向的
        array.recycle();

        init();
    }

    public void setOrientation(int orientation) {
        mOrientation = orientation;
    }

    public void setOnHiveClickListener(OnHiveClickListener hiveClickListener) {
        mHiveClickListener = hiveClickListener;
    }

    private void init() {
       /* ViewConfiguration vc = ViewConfiguration.get(getContext());
        mSlop = vc.getScaledTouchSlop();        //  临界值*/

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);

        //        mBoardPaint = new Paint();
        //        mBoardPaint.setAntiAlias(true);
        //        mBoardPaint.setColor(Color.WHITE); //  白色边框
        //        mBoardPaint.setStyle(Paint.Style.FILL);
        //        mBoardPaint.setStrokeWidth(10);     //  3px 的边框

        mMatrix = new Matrix();

        mPath = new Path();
        //        mBoardPath = new Path();        //  边框的path
        //
        //        mBorderWidth = 8;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        switch (mOrientation) {
            case HORIZONTAL:                    //  画水平方向的hive
                //  正六边形的边长
                mSideLength = width * 0.5f;
                mGap = (width - mSideLength * (float) Math.sqrt(3)) * 0.5f;
                break;
            case VERTICAL:                      //  画竖直方向的hive
                //  正六边形的边长
                mSideLength = height * 0.5f;
                mGap = (height - mSideLength * (float) Math.sqrt(3)) * 0.5f;
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //  正六边形的边长
        switch (mOrientation) {
            case HORIZONTAL:                    //  画水平方向的hive
                //  正六边形的边长
                mSideLength = w * 0.5f;
                mGap = (w - mSideLength * (float) Math.sqrt(3)) * 0.5f;
                break;
            case VERTICAL:                      //  画竖直方向的hive
                //  正六边形的边长
                mSideLength = h * 0.5f;
                mGap = (h - mSideLength * (float) Math.sqrt(3)) * 0.5f;
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
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

        //  缩放和平移  和旋转
        shader.setLocalMatrix(mMatrix);
        mPaint.setShader(shader);

        switch (mOrientation) {
            case HORIZONTAL:                    //  画水平方向的hive
                drawHorizontalHive(canvas);
                break;
            case VERTICAL:                      //  画竖直方向的hive
                drawVerticalHive(canvas);
                break;
            default:
                break;
        }
    }

    /* 画水平方向的hive */
    private void drawHorizontalHive(Canvas canvas) {
        mPath.reset();          //  内path
        mPath.moveTo(0, mSideLength * (float) Math.sqrt(0.75) + mGap);    //  竖着的与y轴相交的第一个点 0
        mPath.lineTo(mSideLength * 0.5f, mGap);
        mPath.lineTo(mSideLength * 1.5f, mGap);
        mPath.lineTo(mSideLength * 2, mSideLength * (float) Math.sqrt(0.75) + mGap);
        mPath.lineTo(mSideLength * 1.5f, mSideLength * (float) Math.sqrt(3) + mGap);
        mPath.lineTo(mSideLength * 0.5f, mSideLength * (float) Math.sqrt(3) + mGap);

        //  外path, 内外path之间形成白色边框   目前没考虑border

        //        canvas.drawPath(mBoardPath, mBoardPaint);       //  在底层先画上 较大的 空白
        canvas.drawPath(mPath, mPaint);     //  画上稍小的bitmap ,就出现边框的效果了
    }

    /* 画竖直方向的hive */
    private void drawVerticalHive(Canvas canvas) {
        mPath.reset();          //  内path
        mPath.moveTo(mGap, mSideLength * 0.5f);     //  竖着的与y轴相交的第一个点 0
        mPath.lineTo(mGap + mSideLength * (float) Math.sqrt(0.75), 0);      //  Math.sqrt(0.75) 是 2分之根号3 , 正余弦 1
        mPath.lineTo(mGap + mSideLength * (float) Math.sqrt(3), mSideLength * 0.5f); //  2
        mPath.lineTo(mGap + mSideLength * (float) Math.sqrt(3), mSideLength * 1.5f); //  3
        mPath.lineTo(mGap + mSideLength * (float) Math.sqrt(0.75), mSideLength * 2);  //  4
        mPath.lineTo(mGap, mSideLength * 1.5f);  //  5

        //  外path, 内外path之间形成白色边框   目前没考虑border

        //        canvas.drawPath(mBoardPath, mBoardPaint);       //  在底层先画上 较大的 空白
        canvas.drawPath(mPath, mPaint);     //  画上稍小的bitmap ,就出现边框的效果了
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                mClick = confirmClick(mDownX, mDownY);
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                if (!(confirmClick(x, y) /*&& Math.abs(x - mDownX) < mSlop && Math.abs(y - mDownY) < mSlop*/))
                    mClick = false;
                break;
            case MotionEvent.ACTION_UP:
                if (mClick && mHiveClickListener != null)
                    mHiveClickListener.onHiveClick(this);
                mClick = false;
                break;
            default:
                break;
        }
        //        return super.onTouchEvent(event);
        return mHiveClickListener != null;    //  消费触摸事件
    }

    private boolean confirmClick(float x, float y) {
        if (mOrientation == HORIZONTAL) {
            return confirmHorizontalClick(x, y);
        } else if (mOrientation == VERTICAL) {
            return confirmVerticalClick(x, y);
        }
        return false;
    }

    /* 竖直的正六边形 判断是否点击事件 */
    private boolean confirmVerticalClick(float x, float y) {
        if (y < 0 || y > getHeight())
            return false;
        float min = mGap;
        if (y >= 0 && y < 0.5f * mSideLength) {
            min = mGap + (mSideLength - 2 * y) * (float) Math.sqrt(0.75);
        } else if (y > getHeight() - 0.5f * mSideLength) {
            min = mGap + (2 * y - 3 * mSideLength) * (float) Math.sqrt(0.75);
        }
        float max = getWidth() - min;   //  左右是对称的
        return x >= min && x <= max;
    }

    /* 水平的正六边形 判断是否点击事件 */

    private boolean confirmHorizontalClick(float x, float y) {
        if (x < 0 || x > getWidth())
            return false;
        float min = mGap;
        if (x >= 0 && x < 0.5f * mSideLength) {
            min = mGap + (mSideLength - 2 * x) * (float) Math.sqrt(0.75);
        } else if (x > getWidth() - 0.5f * mSideLength) {
            min = mGap + (2 * x - 3 * mSideLength) * (float) Math.sqrt(0.75);
        }
        float max = getHeight() - min;  //  上下是对称的
        return y >= min && y <= max;
    }

    /**
     * 正六边形 的点击监听
     */
    public interface OnHiveClickListener {
        void onHiveClick(View v);
    }
}
