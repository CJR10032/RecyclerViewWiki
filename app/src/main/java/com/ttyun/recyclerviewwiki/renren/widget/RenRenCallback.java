package com.ttyun.recyclerviewwiki.renren.widget;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.ttyun.recyclerviewwiki.renren.adapter.RenRenCardAdapter;
import com.ttyun.recyclerviewwiki.renren.bean.RenRenCardBean;

import java.util.ArrayList;

/**
 * 创建者     CJR
 * 创建时间   2017-08-12 16:15
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RenRenCallback extends ItemTouchHelper.Callback {

    private RecyclerView              mRv;
    private ArrayList<RenRenCardBean> mDataList;
    private RenRenCardAdapter         mRenRenCardAdapter;
    private RenRenCardConfig          mRenRenCardConfig;

    private final int MAX_ROTATION = 15;

    private float mHorizontalSlop;

    public RenRenCallback(RecyclerView rv, ArrayList<RenRenCardBean> dataList,
                          RenRenCardAdapter renRenCardAdapter, RenRenCardConfig renRenCardConfig) {
        mRv = rv;
        mDataList = dataList;
        mRenRenCardAdapter = renRenCardAdapter;
        mRenRenCardConfig = renRenCardConfig;

        DisplayMetrics displayMetrics = rv.getContext().getResources().getDisplayMetrics();
        mHorizontalSlop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, displayMetrics);
    }

    public float getHorizontalSlop() {
        return mHorizontalSlop;
    }

    public void setHorizontalSlop(float horizontalSlop) {
        mHorizontalSlop = horizontalSlop;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        //  去掉上下, 只有左右滑动了
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        RenRenCardBean remove = mDataList.remove(viewHolder.getLayoutPosition());
        mDataList.add(0, remove);
        mRenRenCardAdapter.notifyDataSetChanged();
    }

    public float getSwipeThreshold() {
        return mRv.getWidth() * 0.5f;
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        if (!isHorizontalSwipe(viewHolder.itemView) && isTopViewEvent(viewHolder.itemView)) {
            return Float.MAX_VALUE;
        }
        return super.getSwipeThreshold(viewHolder);
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        //  获取最后一个childViwe, 也就是现在在屏幕上的没有被遮挡的View
        View topView = mRv.getChildAt(mRv.getChildCount() - 1);
        if (!isHorizontalSwipe(topView)) {
            return Float.MAX_VALUE;
        }
        return super.getSwipeEscapeVelocity(defaultValue);
    }

    public boolean isHorizontalSwipe(View topView) {
        return Math.abs(topView.getX() + topView.getWidth() / 2 - mRv.getWidth() / 2) > mHorizontalSlop;
    }

    /**
     * @param childView 当前有event 的childView
     * @return 当前的childView是否顶层的View
     */
    public boolean isTopViewEvent(View childView) {
        View topView = mRv.getChildAt(mRv.getChildCount() - 1);
        return topView == childView;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        Log.e("vivi", "dX = " + dX
                + ", dY = " + dY
                + ", actionState = " + actionState
                + ", isCurrentlyActive = " + isCurrentlyActive
        );

        //  滑动的距离
        float swipeValue = (float) Math.sqrt(dX * dX + dY * dY);
        float fraction = swipeValue / getSwipeThreshold();
        //  边界修正,   这两个都是正数
        fraction = Math.min(1, fraction);
        final int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = recyclerView.getChildAt(i);
            int level = childCount - 1 - i;
            if (level > 0) {
                //   child.setScaleX(1 - mRenRenCardConfig.SCALE_GAP * (level - fraction));
                if (level < mRenRenCardConfig.MAX_SHOW_COUNT - 1) {
                    child.setScaleX(1 - mRenRenCardConfig.SCALE_GAP * (level - fraction));
                    child.setScaleY(1 - mRenRenCardConfig.SCALE_GAP * (level - fraction));
                    child.setTranslationY(mRenRenCardConfig.TRANS_Y_GAP * (level - fraction));
                }
            } else {    //  level = 0, 是第一层, 要转旋转效果
                float xFraction = dX / getSwipeThreshold();
                if (xFraction > 1) {        //  边界修正
                    xFraction = 1;
                } else if (xFraction < -1) {
                    xFraction = -1;
                }
                child.setRotation(MAX_ROTATION * xFraction);

                if (viewHolder instanceof RenRenCardAdapter.ViewHolder) {
                    RenRenCardAdapter.ViewHolder holder = (RenRenCardAdapter.ViewHolder) viewHolder;
                    if (xFraction < 0) {
                        //  左滑  ,  显示 delete
                        holder.delIv.setAlpha(xFraction * -1.3f);
                    } else if (xFraction > 0) {
                        //  右滑   显示favour
                        holder.favourIv.setAlpha(xFraction * 1.3f);
                    } else {
                        holder.delIv.setAlpha(0f);
                        holder.favourIv.setAlpha(0f);
                    }
                }
            }
        }
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    @Override
    public long getAnimationDuration(RecyclerView recyclerView, int animationType,
                                     float animateDx, float animateDy) {
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
    }

    /**
     * 循环利用itemView的时候 重置itemView的状态
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        //  要记得将旋转了的View复原
        viewHolder.itemView.setRotation(0);
        if (viewHolder instanceof RenRenCardAdapter.ViewHolder) {
            RenRenCardAdapter.ViewHolder holder = (RenRenCardAdapter.ViewHolder) viewHolder;
            holder.delIv.setAlpha(0f);
            holder.favourIv.setAlpha(0f);   //  注意, 这个f一定不能少, 不然就会调用重载的方法发生bug
        }
    }
}
