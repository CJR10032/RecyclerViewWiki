package com.ttyun.recyclerviewwiki.sticky.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.ttyun.recyclerviewwiki.sticky.adapter.RVStickyAdapter;
import com.ttyun.recyclerviewwiki.sticky.bean.Person;

import java.util.ArrayList;

/**
 * 创建者     CJR
 * 创建时间   2017-08-03 11:21
 * 描述	     分类title的decoration, 因为悬停的分组的效果, 所以只能配合LinearLayoutManager使用,
 * *         LinearLayout是为了获取第一个可见的position (好吧,受博客影响...这里其实不需要linearLayoutManager,
 * *         第一个条目可以直接用parent.getChildAt(0)获取)
 * 参考网址   http://www.jianshu.com/p/0d49d9f51d2c
 * </p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TitleItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private Rect  mBounds;                          //  用于存放测量的文字的rect

    private int mTitleHeight;                                   //  title 的高度
    private static final int COLOR_TITLE_BG   = 0xffdfdfdf;     //  title的背景色
    private static final int COLOR_TITLE_FONT = 0xff000000;     //  title文字的颜色
    private float mTitleFontSize;                               //  title文字的大小

    public TitleItemDecoration(Context context) {
        mPaint = new Paint();
        mBounds = new Rect();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
        mTitleFontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, displayMetrics);
        mPaint.setTextSize(mTitleFontSize);
        //  anti 反对, 这里是设置抗锯齿
        mPaint.setAntiAlias(true);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        int viewLayoutPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();

        int pos = parent.getChildAdapterPosition(view);
        if (pos == 0) {     //  第一个条目肯定有title
            outRect.set(0, mTitleHeight, 0, 0);
        } else {
            ArrayList<Person> dataList = ((RVStickyAdapter) parent.getAdapter()).getDataList();
            Person cur = dataList.get(pos);
            Person pre = dataList.get(pos - 1);
            if (!TextUtils.isEmpty(cur.firstChar) && !cur.firstChar.equals(pre.firstChar)) {
                //  首字母不为空, 并且和前一个不一样,说明是新的分类
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

        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {          //  这里也和ListView一样是屏幕+1个childCount?
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            ArrayList<Person> dataList = ((RVStickyAdapter) parent.getAdapter()).getDataList();
            int pos = layoutParams.getViewLayoutPosition();
            Person cur = dataList.get(pos);
            if (pos == 0) {     //  第一个条目肯定有title
                drawTitleArea(c, cur.firstChar, left, right, child, layoutParams, pos);
            } else {        //  不是第一个条目, 要根据下一个数据是不是新条目做判断
                Person pre = dataList.get(pos - 1);
                if (!TextUtils.isEmpty(cur.firstChar) && !cur.firstChar.equals(pre.firstChar)) {
                    //  首字母不为空, 并且和前一个不一样,说明是新的分类
                    drawTitleArea(c, cur.firstChar, left, right, child, layoutParams, pos);
                }
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        /*//  这里要用LinearLayoutManager来获取第一个可见位置
        final LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        final int firstVisiblePos = layoutManager.findFirstVisibleItemPosition();
        //  这里的firstVisiblePos是第一个可见的位置,这个位置是在mDataList里的位置,所以用getChildAt(firstVisiblePos)肯定不对的
        final View child = parent.findViewHolderForLayoutPosition(firstVisiblePos).itemView;
        */

        View child = parent.getChildAt(0);
        int firstVisiblePos = parent.getChildLayoutPosition(child);

        final ArrayList<Person> dataList = ((RVStickyAdapter) parent.getAdapter()).getDataList();
        final String text = dataList.get(firstVisiblePos).firstChar;

        //  设置title背景色
        mPaint.setColor(COLOR_TITLE_BG);
        c.drawRect(
                parent.getPaddingLeft(),
                parent.getPaddingTop(),
                parent.getWidth() - parent.getPaddingRight(),
                parent.getPaddingTop() + mTitleHeight, mPaint);
        mPaint.setColor(COLOR_TITLE_FONT);
        mPaint.getTextBounds(text, 0, text.length(), mBounds);
        //  开始画文字的起始坐标, 注意, canvas画文字的时候,坐标是以左下角为0,0点的,
        //  这里的paddingleft要和itemView的一致
        final int x = child.getPaddingLeft();
        final int y = (parent.getTop() + (mTitleHeight / 2 + mBounds.height() / 2));
        c.drawText(text, x, y, mPaint);
    }

    private void drawTitleArea(Canvas c, String text, int left, int right, View child,
                               RecyclerView.LayoutParams layoutParams, int pos) {
        //  TODO: 这里有个疑问, 万能分割线的写法这里是加的, 参考链接的博客是减的? 先具体看看效果
        final int bottom = child.getTop()/* - layoutParams.topMargin*/;
        //  这里是分类title, 画在item之上的, 所以用getTop + topMargin, 如果是分割线, 就用getBottom + bottomMargin
        final int top = bottom - mTitleHeight;
        //  设置title背景色
        mPaint.setColor(COLOR_TITLE_BG);
        //  画title的背景
        c.drawRect(left, top, right, bottom, mPaint);
        //  设置title文字颜色
        mPaint.setColor(COLOR_TITLE_FONT);
        //  获取title文字的bounds
        mPaint.getTextBounds(text, 0, text.length(), mBounds);
        //  开始画文字的起始坐标, 注意, canvas画文字的时候,坐标是以左下角为0,0点的
        final int x = child.getPaddingLeft();
        final int y = (child.getTop() - (mTitleHeight / 2 - mBounds.height() / 2));
        c.drawText(text, x, y, mPaint);
    }
}
