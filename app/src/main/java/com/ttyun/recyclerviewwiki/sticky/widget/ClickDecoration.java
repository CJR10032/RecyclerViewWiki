package com.ttyun.recyclerviewwiki.sticky.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ttyun.recyclerviewwiki.R;
import com.ttyun.recyclerviewwiki.sticky.adapter.RVStickyAdapter;
import com.ttyun.recyclerviewwiki.sticky.bean.Person;

import java.util.ArrayList;

/**
 * 创建者     CJR
 * 创建时间   2017-08-07 09:39
 * 描述
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述
 */
public class ClickDecoration extends RecyclerView.ItemDecoration {

    private int mTitleHeight;

    /**
     * 粘性头部
     */
    private View mStickyHeader;

    /**
     * 头部的TextView
     */
    private TextView mStickyTv;

    /**
     * 记录分隔栏位置的容器,用来做title头部点击事件用的
     */
    private SparseArray<Rect> mHeaderRects = new SparseArray<>();

    public ClickDecoration(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        //  清空分隔栏位置的数据
        mHeaderRects.clear();
        int pos = parent.getChildAdapterPosition(view);
        if (pos == 0) {
            //  第一个条目肯定有title
            outRect.set(0, mTitleHeight, 0, 0);
        } else {
            ArrayList<Person> dataList = ((RVStickyAdapter) parent.getAdapter()).getDataList();
            Person cur = dataList.get(pos);
            Person pre = dataList.get(pos - 1);
            //  首字母不为空, 并且和前一个不一样,说明是新的分类
            if (!TextUtils.isEmpty(cur.firstChar) && !cur.firstChar.equals(pre.firstChar)) {
                outRect.set(0, mTitleHeight, 0, 0);
            } else {
                //   这个必要么? 不需要
                //   outRect.set(0, 0, 0, 0);
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {          //  这里也和ListView一样是屏幕+1个childCount?
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            ArrayList<Person> dataList = ((RVStickyAdapter) parent.getAdapter()).getDataList();
            int pos = layoutParams.getViewLayoutPosition();
            Person cur = dataList.get(pos);
            if (pos == 0) {     //  第一个条目肯定有title
                drawHeader(c, parent, child, pos, cur);
            } else {        //  不是第一个条目, 要根据下一个数据是不是新条目做判断
                Person pre = dataList.get(pos - 1);
                //  首字母不为空, 并且和前一个不一样,说明是新的分类
                if (!TextUtils.isEmpty(cur.firstChar) && !cur.firstChar.equals(pre.firstChar)) {
                    drawHeader(c, parent, child, pos, cur);
                }
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        //  这里的firstVisiblePos是第一个可见的位置,这个位置是在mDataList里的位置,所以用getChildAt(firstVisiblePos)肯定不对的
        final View child = parent.getChildAt(0);
        //  这里和getChildAdapterPosition是一模一样的,ViewHolder的那两个方法才有区别
        final int firstVisiblePos = parent.getChildLayoutPosition(child);
        final ArrayList<Person> dataList = ((RVStickyAdapter) parent.getAdapter()).getDataList();
        final String text = dataList.get(firstVisiblePos).firstChar;

        //  这里不考虑弹性的情况, 所以不会是最后一个 item

        boolean isTrans = false;
        if (!text.equals(dataList.get(firstVisiblePos + 1).firstChar) && child.getBottom() < mTitleHeight) {
            isTrans = true;
            //  保存canvas状态
            c.save();

            //一种头部折叠起来的视效，个人觉得也还不错~
            //可与123行 c.drawRect 比较，只有bottom参数不一样，由于
            // child.getHeight() + child.getTop() < mTitleHeight，
            // 所以绘制区域是在不断的减小，有种折叠起来的感觉
            //  CJR ex: 这里其实就是clip了一个长方形区域
            //  c.clipRect(
            //          parent.getPaddingLeft(),
            //          parent.getPaddingTop(),
            //          parent.getRight() - parent.getPaddingRight(),
            //          parent.getPaddingTop() + child.getHeight() + child.getTop());

            //  顶上去的动画效果    上移为负,所以这里是bot - titleH
            c.translate(0, child.getBottom() - mTitleHeight);
        }

        drawStickyHeader(c, parent, firstVisiblePos, dataList.get(firstVisiblePos));
        if (isTrans)
            c.restore();
    }

    private void buildStickyHeader(RecyclerView parent) {
        Log.e("vivi", "buildStickyHeader方法调用了...");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        mStickyHeader = inflater.inflate(R.layout.decoration_click, parent, false);

        mStickyTv = (TextView) mStickyHeader.findViewById(R.id.tv);

        int widthSpec;      //  用于测量widthMeasureSpec
        int heightSpec;     //  用于测量的heightMeasureSpec
        ViewGroup.LayoutParams lp = mStickyHeader.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mStickyHeader.setLayoutParams(lp);
        }
        if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            //  如果是MATCH_PARENT，则用父控件能分配的最大宽度和EXACTLY构建MeasureSpec。
            widthSpec = View.MeasureSpec.makeMeasureSpec(
                    parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(),
                    View.MeasureSpec.EXACTLY);
        } else if (lp.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            //  如果是WRAP_CONTENT，则用父控件能分配的最大宽度和AT_MOST构建MeasureSpec。
            widthSpec = View.MeasureSpec.makeMeasureSpec(
                    parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(),
                    View.MeasureSpec.AT_MOST);
        } else {
            //  否则则是具体的宽度数值，则用这个宽度和EXACTLY构建MeasureSpec。
            widthSpec = View.MeasureSpec.makeMeasureSpec(lp.width, View.MeasureSpec.EXACTLY);
        }

        if (lp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            //  如果是MATCH_PARENT，则用父控件能分配的最大宽度和EXACTLY构建MeasureSpec。
            heightSpec = View.MeasureSpec.makeMeasureSpec(
                    parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom(),
                    View.MeasureSpec.EXACTLY);
        } else if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            //  如果是WRAP_CONTENT，则用父控件能分配的最大宽度和AT_MOST构建MeasureSpec。
            heightSpec = View.MeasureSpec.makeMeasureSpec(
                    parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom(),
                    View.MeasureSpec.AT_MOST);
        } else {
            //  否则则是具体的宽度数值，则用这个宽度和EXACTLY构建MeasureSpec。
            heightSpec = View.MeasureSpec.makeMeasureSpec(lp.height, View.MeasureSpec.EXACTLY);
        }
        mStickyHeader.measure(widthSpec, heightSpec);
        int left = parent.getPaddingLeft();
        int right = left + mStickyHeader.getMeasuredWidth();
        int top = 0;
        int bottom = mTitleHeight;
        mStickyHeader.layout(left, top, right, bottom);
    }

    /**
     * 画出悬浮在顶部的header
     *
     * @param c      画板
     * @param parent RecyclerView
     */
    private void drawStickyHeader(Canvas c, RecyclerView parent, int pos, Person data) {
        if (mStickyHeader == null) {
            buildStickyHeader(parent);
        }
        mStickyTv.setText(data.firstChar);
        mHeaderRects.put(pos, new Rect(parent.getPaddingLeft(),
                parent.getPaddingTop(),
                parent.getPaddingLeft() + mStickyHeader.getMeasuredWidth(),
                parent.getPaddingTop() + mTitleHeight));
        mStickyHeader.draw(c);
    }


    /**
     * 画出分类的text
     *
     * @param c      画板
     * @param parent RecyclerView
     * @param child  子条目item
     */
    private void drawHeader(Canvas c, RecyclerView parent, View child, int pos, Person data) {
        if (mStickyHeader == null) {
            buildStickyHeader(parent);
        }
        mStickyTv.setText(data.firstChar);

        mHeaderRects.put(pos, new Rect(
                parent.getPaddingLeft(),
                child.getTop() - mTitleHeight,
                parent.getPaddingLeft() + mStickyHeader.getMeasuredWidth(),
                child.getTop()));
        c.save();
        c.translate(0, child.getTop() - mTitleHeight);
        mStickyHeader.draw(c);
        c.restore();
    }

    public int findHeaderPositionUnder(int x, int y) {
        //  遍历屏幕上header的区域，判断点击的位置是否在某个header的区域内
        final int headerSize = mHeaderRects.size();
        for (int i = 0; i < headerSize; i++) {
            Rect rect = mHeaderRects.get(mHeaderRects.keyAt(i));
            if (rect.contains(x, y)) {
                return mHeaderRects.keyAt(i);
            }
        }
        return -1;
    }

    private boolean findHeaderClickView(View view, int x, int y) {
        if (view == null)
            return false;
        for (int i = 0; i < mHeaderRects.size(); i++) {
            Rect rect = mHeaderRects.get(mHeaderRects.keyAt(i));
            if (rect.contains(x, y)) {
                Rect vRect = new Rect();
                // 需要响应点击事件的区域在屏幕上的坐标
                vRect.set(
                        rect.left + view.getLeft(),
                        rect.top + view.getTop(),
                        rect.left + view.getLeft() + view.getWidth(),
                        rect.top + view.getTop() + view.getHeight());
                return vRect.contains(x, y);
            }
        }
        return false;
    }

    public void performClick(int x, int y) {
        int pos = findHeaderPositionUnder(x, y);
        if (pos != -1) {
            Log.e("vivi", "点击了头部 --- " + mStickyTv.getText());
            if (findHeaderClickView(mStickyTv, x, y)) {
                Log.e("vivi", "点击了头部的文字 --- " + mStickyTv.getText());
            }
        }
    }
}
