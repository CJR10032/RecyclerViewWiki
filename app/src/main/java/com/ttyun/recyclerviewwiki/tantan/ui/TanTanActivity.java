package com.ttyun.recyclerviewwiki.tantan.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import com.ttyun.recyclerviewwiki.R;
import com.ttyun.recyclerviewwiki.tantan.adapter.TanTanCardAdapter;
import com.ttyun.recyclerviewwiki.tantan.bean.TanTanBean;
import com.ttyun.recyclerviewwiki.tantan.widget.TanTanCallback;
import com.ttyun.recyclerviewwiki.tantan.widget.TanTanCardConfig;
import com.ttyun.recyclerviewwiki.tantan.widget.TanTanCardLayoutManager;

import java.util.ArrayList;

/**
 * 创建者     CJR
 * 创建时间   2017-08-15 14:26
 * 描述	     仿探探的翻牌效果
 * *         这里的图片是 从 mDataList.get(0) 开始展示的, renren那个Activity是从 mDataList.get(mDataList.getSize() - 1) 开始展示的
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TanTanActivity extends AppCompatActivity {

    private RecyclerView mRv;

    private ArrayList<TanTanBean> mDataList;
    private TanTanCardAdapter     mTanTanCardAdapter;

    private Rect mItemBounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tantan);

        initView();
        initData();
        initListener();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mRv = (RecyclerView) findViewById(R.id.recycler_view);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        int showCount = 3;          //  屏幕上存在的 item 数量, 这里只能看见3张, 但是是0 - 3, 也就是count = 4, 最后一张被压着看不见
        float scaleGap = 0.10f;     //  每一张卡片之间的 缩放比例相差 0.10, 第一张卡片1, 第二张卡片 0.9 第三张卡片 0.8 以此类推
        int transYRation = 14;      //  每张卡片之间的位移差比例, 第一张卡片 0, 第二张卡片 itemHeight / 14 第三张卡片 itemHeight / 14 * 2 ,以此类推
        TanTanCardConfig tanTanCardConfig = new TanTanCardConfig(showCount, scaleGap, transYRation, 15);
        mRv.setLayoutManager(new TanTanCardLayoutManager(tanTanCardConfig));
        createData();
        /*
        这种方法 做的屏蔽 还是会有一点小bug , 看见的第二张图片, 在很小的角度能滑动它
        mTanTanCardAdapter = new TanTanCardAdapter(mDataList, new TanTanCardAdapter.OnItemSwipeListener() {
            @Override
            public void onItemSwipe(TanTanCardAdapter.ViewHolder holder, int pos) {
                if (holder.itemView == mRv.getChildAt(mRv.getChildCount() - 1)) {
                    helper.startSwipe(mRv.getChildViewHolder(holder.itemView));
                }
            }
        });*/

        mTanTanCardAdapter = new TanTanCardAdapter(mDataList);
        mRv.setAdapter(mTanTanCardAdapter);
        //        mRv.setItemAnimator(new DefaultItemAnimator());       //  默认动画就算不设置, RecyclerView内部也创建了一个DefaultItemAnimator

        final TanTanCallback callback = new TanTanCallback(mRv, mTanTanCardAdapter, tanTanCardConfig);
        final ItemTouchHelper helper = new ItemTouchHelper(callback);

        helper.attachToRecyclerView(mRv);       //  ItemTouchHelper 关联到 RecyclerView

        mItemBounds = new Rect();
        mRv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                switch (MotionEventCompat.getActionMasked(e)) {
                    case MotionEvent.ACTION_DOWN:    //  按下的是在图片范围内
                        int x = (int) e.getX();
                        int y = (int) e.getY();
                        View topView = rv.getChildAt(rv.getChildCount() - 1);       //  顶层的View
                        //  动画效果结束后才有新的触摸效果
                        mItemBounds.set(topView.getLeft(), topView.getTop(), topView.getRight(), topView.getBottom());

                        if (mItemBounds.contains(x, y)) {
                            helper.startSwipe(rv.getChildViewHolder(topView));
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    default:
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }


    /**
     * 初始化监听
     */
    private void initListener() {

    }

    private void createData() {
        mDataList = new ArrayList<>();
        mDataList.add(new TanTanBean(R.drawable.img_avatar_01, "小姐姐", 12, "处女座", "IT/互联网"));
        mDataList.add(new TanTanBean(R.drawable.img_avatar_02, "小姐姐", 12, "处女座", "IT/互联网"));
        mDataList.add(new TanTanBean(R.drawable.img_avatar_03, "派拉斯特", 12, "处女座", "IT/互联网"));
        mDataList.add(new TanTanBean(R.drawable.img_avatar_04, "小姐姐", 12, "处女座", "IT/互联网"));
        mDataList.add(new TanTanBean(R.drawable.img_avatar_05, "小姐姐", 12, "处女座", "IT/互联网"));
        mDataList.add(new TanTanBean(R.drawable.img_avatar_06, "小姐姐", 12, "处女座", "IT/互联网"));
        mDataList.add(new TanTanBean(R.drawable.img_avatar_07, "小姐姐", 12, "处女座", "IT/互联网"));
    }

}
