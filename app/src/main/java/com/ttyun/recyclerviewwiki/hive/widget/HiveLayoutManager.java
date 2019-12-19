package com.ttyun.recyclerviewwiki.hive.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * 创建者     CJR
 * 创建时间   2017-08-28 17:26
 * 描述	      蜂巢 样式的LayoutManager
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HiveLayoutManager extends RecyclerView.LayoutManager {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL   = 1;

    private int mOrientation = HORIZONTAL;    //    默认horizontal方向的

    public HiveLayoutManager() {
    }

    public HiveLayoutManager(int orientation) {
        mOrientation = orientation;
    }

    public void setOrientation(int orientation) {
        mOrientation = orientation;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);      //  轻量回收所有View
        if (getItemCount() < 1)      //  健壮性判断
            return;
        switch (mOrientation) {
            case HORIZONTAL:
                layoutHorizontal(recycler, state);
                break;
            case VERTICAL:
                layoutVertical(recycler, state);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int realOffset = dy;
        offsetChildrenVertical(-realOffset);    //  实际滑动的距离， 可能会在边界处被修复
        return realOffset;
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        offsetChildrenHorizontal(-dx);
        return dx;
    }

    /* 横向的正六边形 */
    private void layoutHorizontal(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int itemCount = getItemCount();
        //  找recycler要一个childItemView, 我不管它是从scrap里取, 还是从RecyclerViewPool里取, 还是从onCreateViewHolder里取
        View centerView = recycler.getViewForPosition(0);
        //  将View添加至RecyclerView中
        addView(centerView);
        //  测量View, 这个方法会考虑到View的ItemDecoration以及Margin
        measureChildWithMargins(centerView, 0, 0);
        final int viewWidth = getDecoratedMeasuredWidth(centerView);
        final int viewHeight = getDecoratedMeasuredHeight(centerView);
        final int widthSpace = getWidth() - viewWidth;      //  宽度 - itemDecoration和Margin之后剩下的空间
        final int heightSpace = getHeight() - viewHeight;   //  高度 - itemDecoration和Margin之后剩下的空间

        final int centerLeft = widthSpace / 2;                      //  中心View 的左边界
        final int centerTop = heightSpace / 2;                      //  中心View 的上边界
        final int centerRight = widthSpace / 2 + viewHeight;        //  中心View 的右边界
        final int centerBottom = heightSpace / 2 + viewHeight;      //  中心View 的下边界

        //  这里是在布局的时候做了居中处理, 将View  layout出来, 显示在屏幕上, 内部会自动追加上该View的ItemDecoration和Margin. 此时我们的View已经可见了
        layoutDecoratedWithMargins(centerView, centerLeft, centerTop,
                centerRight, centerBottom);

        final int sideLength = calcHiveLength(centerView);
        int left = 0, top = 0, right = 0, bottom = 0;
        int level = 0;       //  第0层 1个正六边形(中心,特殊), 第1层 6, 第2层 12, 第3层 18, 第4层 24 依次类推
        int sum = 1;  // sum, 第n层的时候的正六边形的和  0层的时候是1, 后面是一个 6 * n 的等差数列
        int preSum = 0;
        for (int position = 1; position < itemCount; position++) {     //  布局child
            if (position >= sum) {
                level++;
                preSum = sum;
                sum = getHiveSum(level);
            }
            int levelPos = position - preSum;//    这个正六边形在这一层的位置, 这个位置从从0 开始编号的

            if (levelPos == 0) {       //  编号为0 的
                left = centerLeft;
                top = centerTop - (int) (Math.sqrt(3) * sideLength * level);
                right = centerRight;
                bottom = centerBottom - (int) (Math.sqrt(3) * sideLength * level);
            } else if (levelPos > 0 && levelPos <= level) {     //  第1条边, 包含最后一个, 不包含第一个
                left -= 1.5 * sideLength;
                top += Math.sqrt(0.75) * sideLength;
                right -= 1.5 * sideLength;
                bottom += Math.sqrt(0.75) * sideLength;
            } else if (levelPos > level && levelPos <= level * 2) {     //  第2条边, 包含最后一个, 不包含第一个
                top += Math.sqrt(3) * sideLength;
                bottom += Math.sqrt(3) * sideLength;
            } else if (levelPos > level * 2 && levelPos <= level * 3) { //  第3条边, 包含最后一个, 不包含第一个
                left += 1.5 * sideLength;
                top += Math.sqrt(0.75) * sideLength;
                right += 1.5 * sideLength;
                bottom += Math.sqrt(0.75) * sideLength;
            } else if (levelPos > level * 3 && levelPos <= level * 4) { //  第4条边, 包含最后一个, 不包含第一个
                left += 1.5 * sideLength;
                top -= Math.sqrt(0.75) * sideLength;
                right += 1.5 * sideLength;
                bottom -= Math.sqrt(0.75) * sideLength;
            } else if (levelPos > level * 4 && levelPos <= level * 5) { //  第5条边, 包含最后一个, 不包含第一个
                top -= Math.sqrt(3) * sideLength;
                bottom -= Math.sqrt(3) * sideLength;
            } else if (levelPos > level * 5 && levelPos <= level * 6) { //  第6条边, 包含最后一个, 不包含第一个
                left -= 1.5 * sideLength;
                top -= Math.sqrt(0.75) * sideLength;
                right -= 1.5 * sideLength;
                bottom -= Math.sqrt(0.75) * sideLength;
            }

            View view = recycler.getViewForPosition(position);
            addView(view);
            measureChildWithMargins(view, 0, 0);        //  这里还是要测量的...
            layoutDecoratedWithMargins(view, left, top, right, bottom);
        }
    }

    /* 纵向的正六边形布局 */

    private void layoutVertical(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int itemCount = getItemCount();
        //  找recycler要一个childItemView, 我不管它是从scrap里取, 还是从RecyclerViewPool里取, 还是从onCreateViewHolder里取
        View centerView = recycler.getViewForPosition(0);
        //  将View添加至RecyclerView中
        addView(centerView);
        //  测量View, 这个方法会考虑到View的ItemDecoration以及Margin
        measureChildWithMargins(centerView, 0, 0);
        final int viewWidth = getDecoratedMeasuredWidth(centerView);
        final int viewHeight = getDecoratedMeasuredHeight(centerView);
        final int widthSpace = getWidth() - viewWidth;      //  宽度 - itemDecoration和Margin之后剩下的空间
        final int heightSpace = getHeight() - viewHeight;   //  高度 - itemDecoration和Margin之后剩下的空间

        final int centerLeft = widthSpace / 2;                      //  中心View 的左边界
        final int centerTop = heightSpace / 2;                      //  中心View 的上边界
        final int centerRight = widthSpace / 2 + viewHeight;        //  中心View 的右边界
        final int centerBottom = heightSpace / 2 + viewHeight;      //  中心View 的下边界

        //  这里是在布局的时候做了居中处理, 将View  layout出来, 显示在屏幕上, 内部会自动追加上该View的ItemDecoration和Margin. 此时我们的View已经可见了
        layoutDecoratedWithMargins(centerView, centerLeft, centerTop,
                centerRight, centerBottom);

        final int sideLength = calcHiveLength(centerView);
        int left = 0, top = 0, right = 0, bottom = 0;
        int level = 0;       //  第0层 1个正六边形(中心,特殊), 第1层 6, 第2层 12, 第3层 18, 第4层 24 依次类推
        int sum = 1;  // sum, 第n层的时候的正六边形的和  0层的时候是1, 后面是一个 6 * n 的等差数列
        int preSum = 0;
        for (int position = 1; position < itemCount; position++) {     //  布局child
            if (position >= sum) {
                level++;
                preSum = sum;
                sum = getHiveSum(level);
            }
            int levelPos = position - preSum;//    这个正六边形在这一层的位置, 这个位置从从0 开始编号的

            if (levelPos == 0) {       //  编号为0 的
                left = (int) (centerLeft + Math.sqrt(3) * sideLength * level);
                top = centerTop;
                right = (int) (centerRight + Math.sqrt(3) * sideLength * level);
                bottom = centerBottom;
            } else if (levelPos > 0 && levelPos <= level) {     //  第1条边, 包含最后一个, 不包含第一个
                left -= Math.sqrt(0.75) * sideLength;
                top -= 1.5 * sideLength;
                right -= Math.sqrt(0.75) * sideLength;
                bottom -= 1.5 * sideLength;
            } else if (levelPos > level && levelPos <= level * 2) {     //  第2条边, 包含最后一个, 不包含第一个
                left -= Math.sqrt(3) * sideLength;
                right -= Math.sqrt(3) * sideLength;
            } else if (levelPos > level * 2 && levelPos <= level * 3) { //  第3条边, 包含最后一个, 不包含第一个
                left -= Math.sqrt(0.75) * sideLength;
                top += 1.5 * sideLength;
                right -= Math.sqrt(0.75) * sideLength;
                bottom += 1.5 * sideLength;
            } else if (levelPos > level * 3 && levelPos <= level * 4) { //  第4条边, 包含最后一个, 不包含第一个
                left += Math.sqrt(0.75) * sideLength;
                top += 1.5 * sideLength;
                right += Math.sqrt(0.75) * sideLength;
                bottom += 1.5 * sideLength;
            } else if (levelPos > level * 4 && levelPos <= level * 5) { //  第5条边, 包含最后一个, 不包含第一个
                left += Math.sqrt(3) * sideLength;
                right += Math.sqrt(3) * sideLength;
            } else if (levelPos > level * 5 && levelPos <= level * 6) { //  第6条边, 包含最后一个, 不包含第一个
                left += Math.sqrt(0.75) * sideLength;
                top -= 1.5 * sideLength;
                right += Math.sqrt(0.75) * sideLength;
                bottom -= 1.5 * sideLength;
            }

            View view = recycler.getViewForPosition(position);
            addView(view);
            measureChildWithMargins(view, 0, 0);        //  这里还是要测量的...
            layoutDecoratedWithMargins(view, left, top, right, bottom);
        }
    }

    /**
     * 第0层 1个正六边形(中心,特殊), 第1层 6, 第2层 12, 第3层 18, 第4层 24 依次类推
     *
     * @param n 第n层
     * @return 这n层的正六边形的数量
     */
    private int getHiveSum(int n) {
        return 3 * n * (n + 1) + 1;
    }

    /**
     * 注意, 这里的边长只能是整数, 而HiveView 里面的边长是float类型, 这里可能会出现问题
     *
     * @param v childView
     * @return 正六边形的边长
     */
    private int calcHiveLength(View v) {
        int sideLength = 1;
        //  正六边形的边长
        switch (mOrientation) {
            case HORIZONTAL:
                sideLength = getDecoratedMeasuredWidth(v) / 2;
                break;
            case VERTICAL:
                sideLength = getDecoratedMeasuredHeight(v) / 2;
                break;
            default:
                break;
        }
        return sideLength;
    }
}
