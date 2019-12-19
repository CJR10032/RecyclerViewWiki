package com.ttyun.recyclerviewwiki.renren.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * 创建者     CJR
 * 创建时间   2017-08-11 16:10
 * 描述	      皇帝翻牌效果的LayoutManager
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RenRenCardLayoutManager extends RecyclerView.LayoutManager {

    private RenRenCardConfig mRenRenCardConfig;

    public RenRenCardLayoutManager(RenRenCardConfig renRenCardConfig) {
        mRenRenCardConfig = renRenCardConfig;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);      //  这个方法干甚的?
        final int itemCount = getItemCount();
        if (itemCount < 1)      //  健壮性判断
            return;

        //  如果itemCount 小于 MAX_SHOW_COUNT, 就把所有的界面都显示, 否则就从最后的 MAX_SHOW_COUNT 开始显示
        //  屏幕上 的最底下那张图片
        int bottomPos = Math.max(0, itemCount - mRenRenCardConfig.MAX_SHOW_COUNT);
        //  从可见的最底层View开始Layout, 依次层叠上去
        for (int position = bottomPos; position < itemCount; position++) {
            //  找recycler要一个childItemView, 我不管它是从scrap里取,
            //  还是从RecyclerViewPool里取, 还是从onCreateViewHolder里取
            View view = recycler.getViewForPosition(position);  //
            //  将View添加至RecyclerView中
            addView(view);
            //  测量View, 这个方法会考虑到View的ItemDecoration以及Margin
            measureChildWithMargins(view, 0, 0);
            int measuredWidth = getDecoratedMeasuredWidth(view);
            int measuredHeight = getDecoratedMeasuredHeight(view);
            //  宽度 - itemDecoration和Margin之后剩下的空间
            int widthSpace = getWidth() - measuredWidth;
            //  高度 - itemDecoration和Margin之后剩下的空间
            int heightSpace = getHeight() - measuredHeight;

            //  这里是在布局的时候做了居中处理, 将View  layout出来, 显示在屏幕上,
            // 内部会自动追加上该View的ItemDecoration和Margin. 此时我们的View已经可见了
            layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2,
                    widthSpace / 2 + measuredWidth, heightSpace / 2 + measuredHeight);


            /**
             * TopView的Scale 为1, translationY 为0
             * 每一集Scale相差0.05f, translationY 相差7dp左右, 这些参数在CardConfig里设置
             * <p>
             * 观察忍忍影视的UI, 拖动时, topView被拖动, Scale不变, 一直为1
             * top-1 View 的Scale慢慢变化至1, translation也慢慢恢复0
             * top-2 View 的Scale慢慢变化至 top-1 View 的Scale, translation 也慢慢变化至top-1 View 的translation
             */

            //  第几层, 举例子, count = 4, 最后一个TopView (3) 是第0层, MAX_SHOW_COUNT = 4 的时候,
            //  在界面上就只看到了3层, 因为最底层是 被挡住的, 看不到
            int level = itemCount - 1 - position;
            //    除了顶层不需要缩小和位移
            if (level > 0) {
                //  每一层都需要X方向的缩小
                //  view.setScaleX(1 - mRenRenCardConfig.SCALE_GAP * level);
                //  这样最后一层也会有x轴的缩放动画
                //  前N层, 一次向下位移和Y方向的缩小 , 或者itemCount < MAX_SHOW_COUNT 的时候,
                //  这时候就不需要盖住最后一层了
                if (level < mRenRenCardConfig.MAX_SHOW_COUNT - 1) {
                    view.setTranslationY(mRenRenCardConfig.TRANS_Y_GAP * level);
                    view.setScaleY(1 - mRenRenCardConfig.SCALE_GAP * level);
                    view.setScaleX(1 - mRenRenCardConfig.SCALE_GAP * level);
                } else {
                    //  第N层 在向下位移和Y方向的缩小的程度与 N-1层保持一致,
                    //  因为滑动第一层之后最后一层要可见, 所以这里有多一层
                    view.setTranslationY(mRenRenCardConfig.TRANS_Y_GAP * (level - 1));
                    view.setScaleY(1 - mRenRenCardConfig.SCALE_GAP * (level - 1));
                    view.setScaleX(1 - mRenRenCardConfig.SCALE_GAP * (level - 1));
                }
            }

            //  以下 是用View动画做的, 感觉new 太多对象了
            /*float centerX = view.getWidth() * 0.5f;
            float centerY = view.getHeight() * 0.5f;
            if (level > 0) {
                //  每一层都需要X方向的缩小
                AnimationSet animationSet = new AnimationSet(true);
                if (level < mRenRenCardConfig.MAX_SHOW_COUNT - 1) {
                    animationSet.addAnimation(new TranslateAnimation(
                            0,
                            0,
                            0,
                            mRenRenCardConfig.TRANS_Y_GAP * level));
                    animationSet.addAnimation(new ScaleAnimation(
                            1,
                            1 - mRenRenCardConfig.SCALE_GAP * level,
                            1,
                            1 - mRenRenCardConfig.SCALE_GAP * level, centerX, centerY));
                } else {
                    //  第N层 在向下位移和Y方向的缩小的程度与 N-1层保持一致,
                    //  因为滑动第一层之后最后一层要可见, 所以这里有多一层
                    animationSet.addAnimation(new TranslateAnimation(
                            0,
                            0,
                            0,
                            mRenRenCardConfig.TRANS_Y_GAP * (level - 1)));
                    animationSet.addAnimation(new ScaleAnimation(
                            1,
                            1 - mRenRenCardConfig.SCALE_GAP * (level - 1),
                            1,
                            1 - mRenRenCardConfig.SCALE_GAP * (level - 1), centerX, centerY));
                }
                animationSet.setFillAfter(true);    //  动画结束后停留在该状态
                animationSet.setDuration(0);
                view.startAnimation(animationSet);*/
        }
    }

}
