package com.ttyun.recyclerviewwiki.sticky.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;

import com.ttyun.recyclerviewwiki.sticky.adapter.RVStickyAdapter;
import com.ttyun.recyclerviewwiki.sticky.bean.Person;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * 创建者     CJR
 * 创建时间   2017-08-08 09:26
 * 描述	      index用的Decoration
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class IndexDecoration extends RecyclerView.ItemDecoration {

    private TreeSet<String> mLetters;
    private float           mIndexWidth;        //  index的宽度, 高度这里设置为RecyclerView一致
    private float           mIndexFontSize;     //  index字体大小
    private Paint           mPaint;
    private Rect            mBounds;             //  用于存放测量的文字的rect
    private Paint           mTouchPaint;

    private final int TEXT_COLOR          = 0xff000000;
    private final int SELECTED_TEXT_COLOR = 0xffffffff;       //  选中的文字的颜色
    private final int SELECTED_BG_COLOR   = 0X90f76e26;       //  选中的背景色

    public IndexDecoration(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mIndexWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
        mIndexFontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, displayMetrics);

        mPaint = new Paint();
        mPaint.setColor(0xff000000);    //  这里设置颜色为黑色
        mPaint.setTextSize(mIndexFontSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);      //  设置抗锯齿
        //        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));

        mBounds = new Rect();

        mTouchPaint = new Paint();
        mTouchPaint.setColor(SELECTED_BG_COLOR);
        mTouchPaint.setAntiAlias(true);
        mTouchPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));

        //        setLayerType(View.LAYER_TYPE_SOFTWARE, null);       //  关闭硬件加速
    }

    public void setLetters(TreeSet<String> letters) {
        mLetters = letters;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        drawIndex(c, parent, state);
        drawSelected(c, parent, lastTouchY);
    }

    private void drawSelected(Canvas c, RecyclerView parent, float y) {

        //  没有要画的index
        if (mLetters == null || mLetters.isEmpty())
            return;
        final int right = parent.getWidth() - parent.getPaddingRight();
        final float left = right - mIndexWidth;
        final int letterSize = mLetters.size();       //  index一共有的字母数
        final int parentHeight = parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom();     //  获取RecyclerView的高度
        final float cellHeight = parentHeight * 1.0f / letterSize;    //  每一个字母的高度

        float cx = left + mIndexWidth / 2.0f;
        float cy = cellHeight * 0.5f;
        float radius = Math.min(cellHeight, mIndexWidth) * 0.5f;

        y += cellHeight * 0.5f - y % cellHeight;
        c.drawCircle(cx, y, radius, mTouchPaint);
    }

    private void drawIndex(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //  没有要画的index
        if (mLetters == null || mLetters.isEmpty())
            return;
        final int right = parent.getWidth() - parent.getPaddingRight();
        final float left = right - mIndexWidth;
        final int letterSize = mLetters.size();       //  index一共有的字母数
        final int parentHeight = parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom();     //  获取RecyclerView的高度
        final float cellHeight = parentHeight * 1.0f / letterSize;    //  每一个字母的高度

        int i = 0;
        for (String str : mLetters) {
            i++;
            float bottom = i * cellHeight;
            drawText(c, str, left, bottom, cellHeight);
        }
    }

    private void drawText(Canvas c, String str, float left, float bottom, float cellHeight) {
        mPaint.getTextBounds(str, 0, str.length(), mBounds);
        //        int x = (int) (left + (mIndexWidth - mBounds.width()) / 2.0f + 0.5);      //  这是以左下角为起点的
        float x = left + mIndexWidth * 0.5f;        //  这是以中下为起点的
        float y = bottom - (cellHeight - mBounds.height()) * 0.5f;
        c.drawText(str, x, y, mPaint);
    }

    //  =====================================   处理触摸事件   ======================================

    private float lastTouchY;

    public int performTouchEvent(RecyclerView rv, MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        if (!isIndexTouch(rv, x)) {
            //  不是indexBar的触摸事件
            return -1;
        } else {
            //  是indexBar的触摸事件
            //  获取RecyclerView的高度
            final int parentHeight = rv.getHeight() - rv.getPaddingTop() - rv.getPaddingBottom();
            //  index一共有的字母数
            final int letterSize = mLetters.size();
            //  每一个字母的高度
            final float cellHeight = parentHeight * 1.0f / letterSize;

            //  查找触摸的字母
            String letter = "A";
            int index = 0;
            for (String str : mLetters) {
                if (++index * cellHeight >= y) {
                    letter = str;
                    break;
                }
            }

            //  根据触摸的字母, 找到该字母开头的第一个联系人
            ArrayList<Person> dataList = ((RVStickyAdapter) rv.getAdapter()).getDataList();
            final int dataSize = dataList.size();
            int pos = 0;
            for (int i = 0; i < dataSize; i++) {
                if (letter.equals(dataList.get(i).firstChar)) {
                    pos = i;
                    break;
                }
            }
            LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
            layoutManager.scrollToPositionWithOffset(pos, 0);
            //  这个调用的实际上就是LayoutManager的scrollToPosition,只要该pos显示在页面上就OK了,位置不确定
            //   rv.scrollToPosition(pos);
            //   layoutManager.scrollToPosition(pos);

            lastTouchY = e.getY();
            return pos;
        }
    }

    private boolean isIndexTouch(RecyclerView rv, float x) {
        float threshold = rv.getWidth() - rv.getPaddingRight() - mIndexWidth;
        return x >= threshold;
    }
}
