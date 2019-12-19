package com.ttyun.recyclerviewwiki.hive.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.ttyun.recyclerviewwiki.R;
import com.ttyun.recyclerviewwiki.hive.adapter.RVAdapter;
import com.ttyun.recyclerviewwiki.hive.widget.HiveLayoutManager;
import com.ttyun.recyclerviewwiki.main.Constant;

import java.util.ArrayList;

/**
 * 创建者     CJR
 * 创建时间   2017-08-28 15:52
 * 描述	      蜂巢效果的 Activity, 目前对 RecyclerView 的缓存机制还不是太熟悉, 暂时没做缓存
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HiveActivity extends AppCompatActivity {

    private RecyclerView      mRecyclerView;
    private ArrayList<Object> mImageList;
    private RVAdapter         mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hive);

        initView();
        initData();
        initListener();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new HiveLayoutManager(HiveLayoutManager.VERTICAL));
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mImageList = new ArrayList<>();

        for (int i = 0; i < Constant.IMAGE_ARRAY.length; i++) {
            mImageList.add(Constant.IMAGE_ARRAY[i]);
           /* if (i >= 6)
                break;*/
        }

        mAdapter = new RVAdapter(mImageList);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 初始化监听
     */
    private void initListener() {

    }
}
