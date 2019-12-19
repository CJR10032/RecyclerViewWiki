package com.ttyun.recyclerviewwiki.tantan.widget;

import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.ttyun.recyclerviewwiki.tantan.adapter.TanTanCardAdapter;
import com.ttyun.recyclerviewwiki.tantan.bean.TanTanBean;

import java.util.ArrayList;

/**
 * 创建者     CJR
 * 创建时间   2017-08-16 09:14
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TanTanCallback extends ItemTouchHelper.Callback {

    private RecyclerView      mRv;
    private TanTanCardAdapter mTanTanCardAdapter;
    private TanTanCardConfig  mTanTanCardConfig;

    private float mHorizontalSlop;

    public TanTanCallback(RecyclerView rv, TanTanCardAdapter tanTanCardAdapter,
                          TanTanCardConfig tanTanCardConfig) {
        mRv = rv;
        mTanTanCardAdapter = tanTanCardAdapter;
        mTanTanCardConfig = tanTanCardConfig;

        DisplayMetrics displayMetrics = rv.getContext().getResources().getDisplayMetrics();
        mHorizontalSlop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50,
                displayMetrics);

    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
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
    public boolean isItemViewSwipeEnabled() {
        //  这里就不处理了, 需要Swipe的时候让Adapter传出
        //  return super.isItemViewSwipeEnabled();
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.e("vovo", "direction == " + direction);
        ArrayList<TanTanBean> dataList = mTanTanCardAdapter.getDataList();
        TanTanBean remove = dataList.remove(viewHolder.getLayoutPosition());
        dataList.add(remove);
        mTanTanCardAdapter.notifyDataSetChanged();
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

    /**
     * @param childView 当前有event 的childView
     * @return 当前的childView是否顶层的View
     */
    public boolean isTopViewEvent(View childView) {
        View topView = mRv.getChildAt(mRv.getChildCount() - 1);
        return topView == childView;
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
        return Math.abs(topView.getX() + topView.getWidth()
                / 2 - mRv.getWidth() / 2) > mHorizontalSlop;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder
            , float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Log.e("vivi", "dX = " + dX
                + ", dY = " + dY
                + ", actionState = " + actionState
                + ", isCurrentlyActive = " + isCurrentlyActive
        );

        //        不是手指的滑动 && 状态是 swipe 失败
        if (!isCurrentlyActive && mAnimationType == ItemTouchHelper.ANIMATION_TYPE_SWIPE_CANCEL
                && mAnimateDx != 0 && mAnimateDy != 0) {
            //            Log.e("vivi", "纠正之前 dX = " + dX + ", dY = " + dY);

            //  Interpolator 效果 , 这里如果少了这几个 1 - 的话, fraction就是从 1 - 0 的过程,
            //  而Interpolator是要 0 - 1做插补的, 所以要加上这几个1 -
            float dXFraction = 1 - mInterpolator.getInterpolation(1 - dX / mAnimateDx);
            float dYFraction = 1 - mInterpolator.getInterpolation(1 - dY / mAnimateDy);
            dX = mAnimateDx * dXFraction;
            dY = mAnimateDy * dYFraction;
                    /*Log.e("vivi", "纠正之后 ----------  dX = " + dX + ", dY = " + dY + ", dXFraction = "
                            + dXFraction + ", dYFraction = " + dYFraction + ", mAnimateDx = "
                            + mAnimateDx + ", mAnimateDy = " + mAnimateDy);*/
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        float swipeValue = (float) Math.sqrt(dX * dX + dY * dY);       //  滑动的距离
        float fraction = swipeValue / getSwipeThreshold();
        fraction = Math.min(1, fraction);   //  边界修正,   这两个都是正数
        final int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = recyclerView.getChildAt(i);

            int level = childCount - 1 - i;
            if (level == 0) {    //  level = 0, 是第一层, 要转旋转效果
                float xFraction = dX / getSwipeThreshold();
                if (xFraction > 1) {        //  边界修正
                    xFraction = 1;
                } else if (xFraction < -1) {
                    xFraction = -1;
                }
                child.setRotation(mTanTanCardConfig.MAX_ROTATION * xFraction);

                if (viewHolder instanceof TanTanCardAdapter.ViewHolder) {
                    TanTanCardAdapter.ViewHolder holder = (TanTanCardAdapter.ViewHolder) viewHolder;
                    if (xFraction < 0) {    //  左滑  ,  显示 delete
                        holder.deleteIv.setAlpha(xFraction * -1.3f);
                    } else if (xFraction > 0) {    //  右滑   显示favour
                        holder.favourIv.setAlpha(xFraction * 1.3f);
                    } else {
                        holder.deleteIv.setAlpha(0f);
                        holder.favourIv.setAlpha(0f);
                    }
                }
            } else {
                //  > 0, 不是第一层的都要缩放
                //  这是连最后一张也做了缩放的
                //  child.setScaleX(1 - mTanTanCardConfig.SCALE_GAP * (level - fraction));
                if (level < mTanTanCardConfig.MAX_SHOW_COUNT) {
                    child.setScaleX(1 - mTanTanCardConfig.SCALE_GAP * (level - fraction));
                    child.setScaleY(1 - mTanTanCardConfig.SCALE_GAP * (level - fraction));
                    child.setTranslationY(child.getHeight()
                            / mTanTanCardConfig.TRANS_Y_RATIO * (level - fraction));
                }
            }
        }
    }

    /**
     * 循环利用itemView的时候 重置itemView的状态
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        //  要记得将旋转了的View复原
        viewHolder.itemView.setRotation(0);
        if (viewHolder instanceof TanTanCardAdapter.ViewHolder) {
            TanTanCardAdapter.ViewHolder holder = (TanTanCardAdapter.ViewHolder) viewHolder;
            holder.deleteIv.setAlpha(0f);
            holder.favourIv.setAlpha(0f);   //  注意, 这个f一定不能少, 不然就会调用重载的方法发生bug
        }
    }

    @Override
    public long getAnimationDuration(RecyclerView recyclerView, int animationType,
                                     float animateDx, float animateDy) {
        mAnimationType = animationType;
        mAnimateDx = -animateDx;
        mAnimateDy = -animateDy;
        //        Log.e("vivi", "mAnimateDx = " + mAnimateDx + ", animateDy = " + animateDy);

        //  以下这种写法是 把系统的ItemTouchHelper 和 ItemTouchUIUtilImpl给复制 过来,
        //  稍微改了ItemTouchHelper的源码, 加了Interpolator的效果
        int addTime = 0;
        if (animationType == ItemTouchHelper.ANIMATION_TYPE_SWIPE_SUCCESS) {
            //  ANIMATION_TYPE_SWIPE_SUCCESS 的时候 没有Interpolator, 所以动画的时间应该小一点
            addTime = 200;
        } else if (animationType == ItemTouchHelper.ANIMATION_TYPE_SWIPE_CANCEL) {
            //  ANIMATION_TYPE_SWIPE_CANCEL 的时候, 因为有Interpolator, 所以动画的时间要大一点
            addTime = 500;
        }
        return super.getAnimationDuration(recyclerView, animationType,
                animateDx, animateDy) + addTime;
    }

    //  --------------------  以下代码是不copy系统源码的情况下实现Interpolator效果  --------------------
    private int              mAnimationType = -1;
    private TimeInterpolator mInterpolator  = new OvershootInterpolator(3);
    private float            mAnimateDx     = 0;
    private float            mAnimateDy     = 0;

}
