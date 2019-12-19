package com.ttyun.recyclerviewwiki.tantan.widget;

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
public class TanTanCardLayoutManager extends RecyclerView.LayoutManager {

    private TanTanCardConfig mTanTanCardConfig;

    public TanTanCardLayoutManager(TanTanCardConfig renRenCardConfig) {
        mTanTanCardConfig = renRenCardConfig;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);      //  轻量回收所有View
        final int itemCount = getItemCount();
        if (itemCount < 1)      //  健壮性判断
            return;

        //  如果itemCount - 1 小于 MAX_SHOW_COUNT, 就把所有的界面都显示, 否则就从0 显示到 MAX_SHOW_COUNT
        //  屏幕上 的最底下那张图片
        int bottomPos = Math.min(itemCount - 1, mTanTanCardConfig.MAX_SHOW_COUNT);
        //  从可见的最底层View开始Layout, 依次层叠上去
        for (int position = bottomPos; position >= 0; position--) {
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
            //  内部会自动追加上该View的ItemDecoration和Margin. 此时我们的View已经可见了
            layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2,
                    widthSpace / 2 + measuredWidth, heightSpace / 2 + measuredHeight);


            /**
             * TopView的Scale 为1, translationY 为0
             * 每一级Scale相差0.05f, translationY 相差7dp左右, 这些参数在CardConfig里设置
             * <p>
             * 观察忍忍影视的UI, 拖动时, topView被拖动, Scale不变, 一直为1
             * top-1 View 的Scale慢慢变化至1, translation也慢慢恢复0
             * top-2 View 的Scale慢慢变化至 top-1 View 的Scale, translation
             *  也慢慢变化至top-1 View 的translation
             */
            //  view.setScaleX放外面的话是连最后一张 看不见的 都对x进行了缩放
            //            view.setScaleX(1 - mTanTanCardConfig.SCALE_GAP * position);
            //    除了顶层 position = 0的时候不需要缩小和位移
            if (position == mTanTanCardConfig.MAX_SHOW_COUNT) {
                //  第N层 在向下位移和Y方向的缩小的程度与 N-1层保持一致,
                //  因为滑动第一层之后最后一层要可见, 所以这里有多一层
                view.setTranslationY(view.getHeight()
                        / mTanTanCardConfig.TRANS_Y_RATIO * (position - 1));
                view.setScaleY(1 - mTanTanCardConfig.SCALE_GAP * (position - 1));
                view.setScaleX(1 - mTanTanCardConfig.SCALE_GAP * (position - 1));
            } else if (position > 0) {
                //  前N层, 一次向下位移和Y方向的缩小
                //  每一层都需要X方向的缩小
                view.setTranslationY(view.getHeight() / mTanTanCardConfig.TRANS_Y_RATIO * position);
                view.setScaleY(1 - mTanTanCardConfig.SCALE_GAP * position);
                view.setScaleX(1 - mTanTanCardConfig.SCALE_GAP * position);
            }
        }

    }
}
