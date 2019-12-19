package com.ttyun.recyclerviewwiki.renren.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;

import com.ttyun.recyclerviewwiki.R;
import com.ttyun.recyclerviewwiki.renren.adapter.RenRenCardAdapter;
import com.ttyun.recyclerviewwiki.renren.bean.RenRenCardBean;
import com.ttyun.recyclerviewwiki.renren.widget.RenRenCardConfig;
import com.ttyun.recyclerviewwiki.renren.widget.RenRenCardLayoutManager;
import com.ttyun.recyclerviewwiki.renren.widget.RenRenCallback;

import java.util.ArrayList;

/**
 * 创建者     CJR
 * 创建时间   2017-06-30 17:01
 * 描述	     用来展示人人影视翻牌效果的activity , 这里有个bug, 翻的不是最顶部的牌的时候就会出现bug
 * *         这里的图片是 从 mDataList.get(mDataList.getSize() - 1) 开始展示的, tantan那个Activity是从 mDataList.get(0) 开始展示的
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RenRenActivity extends AppCompatActivity {

    private RecyclerView mRv;

    private ArrayList<RenRenCardBean> mDataList;
    private RenRenCardAdapter         mRenRenCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renren);

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
        int showCount = 4;
        float scaleGap = 0.05f;
        int transYGap = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13, getResources().getDisplayMetrics());
        RenRenCardConfig renRenCardConfig = new RenRenCardConfig(showCount, scaleGap, transYGap);
        mRv.setLayoutManager(new RenRenCardLayoutManager(renRenCardConfig));
        createData();
        mRenRenCardAdapter = new RenRenCardAdapter(mDataList);
        mRv.setAdapter(mRenRenCardAdapter);
        mRv.setItemAnimator(new DefaultItemAnimator());

        RenRenCallback callback = new RenRenCallback(mRv, mDataList, mRenRenCardAdapter, renRenCardConfig);
        ItemTouchHelper helper = new ItemTouchHelper(callback);

        helper.attachToRecyclerView(mRv);       //  ItemTouchHelper 关联到 RecyclerView
    }


    /**
     * 初始化监听
     */
    private void initListener() {

    }

    private void createData() {
        mDataList = new ArrayList<>();
        mDataList.add(new RenRenCardBean(0, R.drawable.a1, "黑长直"));
        mDataList.add(new RenRenCardBean(1, R.drawable.a2, "新垣绫濑"));
        mDataList.add(new RenRenCardBean(2, R.drawable.a3, "约战"));
        mDataList.add(new RenRenCardBean(3, R.drawable.a4, "双马尾"));
        mDataList.add(new RenRenCardBean(4, R.drawable.a5, "克劳德"));
        mDataList.add(new RenRenCardBean(5, R.drawable.a0, "折纸鸢一"));
        mDataList.add(new RenRenCardBean(6, R.drawable.avatar_middle, "虎纹鲨鱼"));
    }
}
