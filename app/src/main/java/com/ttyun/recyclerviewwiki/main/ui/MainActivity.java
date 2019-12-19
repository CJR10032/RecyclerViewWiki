package com.ttyun.recyclerviewwiki.main.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ttyun.recyclerviewwiki.R;
import com.ttyun.recyclerviewwiki.hive.ui.HiveActivity;
import com.ttyun.recyclerviewwiki.main.adapter.RVAdapter;
import com.ttyun.recyclerviewwiki.main.utils.ToastUtil;
import com.ttyun.recyclerviewwiki.nest.ui.NestActivity;
import com.ttyun.recyclerviewwiki.payloads.ui.PayloadsActivity;
import com.ttyun.recyclerviewwiki.renren.ui.RenRenActivity;
import com.ttyun.recyclerviewwiki.sticky.ui.StickyActivity;
import com.ttyun.recyclerviewwiki.tantan.ui.TanTanActivity;

import java.util.ArrayList;

/**
 * 创建者     CJR
 * 创建时间   2017-06-29 18:00
 * 描述	     写这个app主要是为了 练习最近更新的RecyclerView的一些 新功能, 比如探探的皇帝翻牌, DiffUtil功能类的使用等
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MainActivity extends AppCompatActivity {


    private RecyclerView      mRecyclerView;
    private ArrayList<String> mDataList;
    private RVAdapter         mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initListener();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void testShowDialog() {
        final String items[] = {"两", "斤", "公斤"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);  //先得到构造器
        //        builder.setTitle("提示"); //设置标题
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ToastUtil.doToast(MainActivity.this, items[which]);
            }
        });
        /*builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ToastUtil.doToast(MainActivity.this, "确定");
            }
        });*/
        builder.create().show();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mDataList = new ArrayList<>();
        mDataList.add("探探翻牌效果");
        mDataList.add("人人影视翻牌效果");
        mDataList.add("RecyclerView嵌套RecyclerView");
        mDataList.add("头部悬浮功能");
        mDataList.add("DiffUtil完成Payloads");
        mDataList.add("蜂巢(正六边形)效果");
        mDataList.add("列表选择弹窗的测试");

        mAdapter = new RVAdapter(mDataList, new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RVAdapter.ViewHolder holder, int pos) {
                Log.e("vivi", "点击了 -- " + pos);
                ToastUtil.doToast(MainActivity.this, "点击了 -- " + pos);
                switch (pos) {
                    case 0:     //  探探翻牌效果
                        turnActivity(TanTanActivity.class);
                        break;
                    case 1:     //  人人影视翻牌效果        (这里有个Bug, 没对触摸事件进行处理, 翻的不是第一张牌时会有bug)
                        turnActivity(RenRenActivity.class);
                        break;
                    case 2:     //  RecyclerView嵌套RecyclerView
                        turnActivity(NestActivity.class);
                        break;
                    case 3:     //  ItemDecoration实现头部悬浮功能
                        turnActivity(StickyActivity.class);
                        break;
                    case 4:
                        turnActivity(PayloadsActivity.class);
                        break;
                    case 5:
                        turnActivity(HiveActivity.class);
                        break;
                    case 6:
                        //                        testShowDialog();
                        turnActivity(DemoActivity.class);
                        break;
                    default:
                        break;
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 初始化监听
     */
    private void initListener() {

    }

    public void turnActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }
}
