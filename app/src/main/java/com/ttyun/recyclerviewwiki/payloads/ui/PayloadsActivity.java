package com.ttyun.recyclerviewwiki.payloads.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ttyun.recyclerviewwiki.R;
import com.ttyun.recyclerviewwiki.main.Constant;
import com.ttyun.recyclerviewwiki.payloads.adapter.PayloadAdapter;
import com.ttyun.recyclerviewwiki.payloads.bean.TestBean;
import com.ttyun.recyclerviewwiki.payloads.callback.PayloadCallback;
import com.ttyun.recyclerviewwiki.sticky.widget.RecycleViewDivider;

import java.util.ArrayList;

/**
 * 创建者     CJR
 * 创建时间   2017-08-10 16:13
 * 描述	      学习使用DiffUtil做增量更新, 注意Adapter中onBindViewHolder的重载方法
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class PayloadsActivity extends AppCompatActivity {

    private RecyclerView       mRv;
    private SwipeRefreshLayout mRefreshLayout;
    private PayloadAdapter     mAdapter;

    private ArrayList<TestBean> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payloads);

        initView();
        initData();
        initListener();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mRv = (RecyclerView) findViewById(R.id.recycler_view);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mRefreshLayout.setColorSchemeColors(0xffff4081);

        mRv.setLayoutManager(new LinearLayoutManager(this));

        mRv.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));

        mAdapter = new PayloadAdapter();
        createData();
        mAdapter.setDataList(mDataList);
        mRv.setAdapter(mAdapter);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRv.postDelayed(refreshRunnable, 1500);
            }
        });
    }

    private void createData() {
        mDataList = new ArrayList<>();
        mDataList.add(new TestBean("圣子降临", "顶级CG动画", Constant.IMAGE_0));
        mDataList.add(new TestBean("桐乃", "这本轻小说真厉害", Constant.IMAGE_1));
        mDataList.add(new TestBean("ましろ", "纯白", Constant.IMAGE_2));

    }

    private void refreshDate() {
        mRefreshLayout.setRefreshing(false);

        ArrayList<TestBean> newDatas = new ArrayList<>();
        for (TestBean bean : mDataList) {
            newDatas.add(bean.clone());
        }

        if (mDataList.size() <= 3) {
            newDatas.add(new TestBean("纳尼", "不知道什么鬼", Constant.IMAGE_3));
            newDatas.add(new TestBean("亚丝娜", "本子王", Constant.IMAGE_4));
            newDatas.add(new TestBean("脑内1", "奇奇怪怪的作品", Constant.IMAGE_5));
            newDatas.add(new TestBean("脑内2", "卖人物模型的作品", Constant.IMAGE_6));
            newDatas.get(0).desc = "经费燃烧,厂商倒闭了";     //  修改第一个条目
            //        newDatas.get(2).pic = Constant.IMAGE_7;      //  将第3个条目的
            newDatas.add(newDatas.remove(1));     //  将第二个条目移动到末端
            newDatas.add(1, new TestBean("小埋", "干物妹", Constant.IMAGE_7));
        }


        //  这是老的更新方法
        /*mDataList = newDatas;
        mAdapter.setDataList(mDataList);
        mAdapter.notifyDataSetChanged();*/

        //  增量更新   将数据设置给Adapter
        mAdapter.setDataList(newDatas);

        //  DiffUtil传入规则 Callback对象 和 是否检测移动item 的boolean变量, 得到 DiffUtil.DiffResult 的对象  true表示检测位移, false表示不检测,检测位移的话时间消耗会久一点
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new PayloadCallback(mDataList, newDatas), true);
        //  利用DiffUtil.Result 对象的 dispatchUpdatesTo() 方法传入RecyclerView的adapter完成增量更新
        diffResult.dispatchUpdatesTo(mAdapter);

        //  改变mDataList的指向
        mDataList = newDatas;
    }

    Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            refreshDate();
        }
    };
}
