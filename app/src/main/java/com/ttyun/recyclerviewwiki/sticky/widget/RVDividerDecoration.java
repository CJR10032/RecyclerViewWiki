package com.ttyun.recyclerviewwiki.sticky.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.ttyun.recyclerviewwiki.sticky.adapter.RVStickyAdapter;
import com.ttyun.recyclerviewwiki.sticky.bean.Person;

import java.util.ArrayList;

/**
 * 创建者     CJR
 * 创建时间   2017-08-04 09:43
 * 描述	      这是配合悬停分类的分割线(分类那一栏是不需要分割线的)
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RVDividerDecoration extends RecyclerView.ItemDecoration {

    private Paint    mPaint;
    private Drawable mDivider;
    private              int   mDividerHeight = 2;//分割线高度，默认为1px
    private static final int[] ATTRS          = new int[]{android.R.attr.listDivider};

    public RVDividerDecoration(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        final int pos = parent.getChildAdapterPosition(view);
        if (pos < state.getItemCount() - 1) {       //  不是最后一个条目才有可能需要分割线
            ArrayList<Person> dataList = ((RVStickyAdapter) parent.getAdapter()).getDataList();
            Person cur = dataList.get(pos);
            Person next = dataList.get(pos + 1);
            if (!TextUtils.isEmpty(cur.firstChar) && cur.firstChar.equals(next.firstChar)) {
                outRect.set(0, 0, 0, mDividerHeight);
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawVertical(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    //  RecyclerView是vertical方向的, 绘制 item 分割线(一条横线)
    private void drawVertical(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        final int childCount = parent.getChildCount();
        final int lastItem = state.getItemCount() - 1;
        final ArrayList<Person> dataList = ((RVStickyAdapter) parent.getAdapter()).getDataList();

        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int pos = layoutParams.getViewAdapterPosition();
            if (pos < lastItem) {       //  不是最后一个条目才有可能需要分割线
                Person cur = dataList.get(pos);
                Person next = dataList.get(pos + 1);
                if (!TextUtils.isEmpty(cur.firstChar) && cur.firstChar.equals(next.firstChar)) {
                    final int top = child.getBottom() + layoutParams.bottomMargin;
                    final int bottom = top + mDividerHeight;

                    if (mPaint != null) {
                        canvas.drawRect(left, top, right, bottom, mPaint);
                    } else if (mDivider != null) {
                        mDivider.setBounds(left, top, right, bottom);
                        mDivider.draw(canvas);
                    }
                }
            }
        }
    }
}
