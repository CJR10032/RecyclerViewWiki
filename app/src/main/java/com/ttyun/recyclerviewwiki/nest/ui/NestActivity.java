package com.ttyun.recyclerviewwiki.nest.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ttyun.recyclerviewwiki.R;
import com.ttyun.recyclerviewwiki.nest.adapter.NestAdapter;
import com.ttyun.recyclerviewwiki.nest.bean.NestTestBean;

import java.util.ArrayList;

/**
 * 创建者     CJR
 * 创建时间   2017-06-30 10:58
 * 描述	      RecyclerView嵌套RecyclerView
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class NestActivity extends AppCompatActivity {

    private RecyclerView            mRecyclerView;
    private ArrayList<NestTestBean> mDataList;
    private NestAdapter             mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nest);

        initView();
        initData();
        initListener();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.nest_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mDataList = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            NestTestBean bean = new NestTestBean();
            bean.top = "组 -- " + i + " 的top";
            bean.bottom = "组 -- " + i + " 的bottom";
            bean.dataList = new ArrayList<>();
            for (int j = i * 10; j <= (i + 1) * 10; j++) {
                bean.dataList.add("组 " + i + " 的子数据 -- " + j);
            }
            mDataList.add(bean);
        }
        mAdapter = new NestAdapter(mDataList);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 初始化监听
     */
    private void initListener() {

    }
}
