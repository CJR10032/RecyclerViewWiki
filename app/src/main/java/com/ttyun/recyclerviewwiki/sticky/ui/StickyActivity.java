package com.ttyun.recyclerviewwiki.sticky.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;
import android.widget.Toast;

import com.github.promeg.pinyinhelper.Pinyin;
import com.ttyun.recyclerviewwiki.R;
import com.ttyun.recyclerviewwiki.main.Cheeses;
import com.ttyun.recyclerviewwiki.sticky.adapter.RVStickyAdapter;
import com.ttyun.recyclerviewwiki.sticky.bean.Person;
import com.ttyun.recyclerviewwiki.sticky.widget.AnimatorTitleDecoration;
import com.ttyun.recyclerviewwiki.sticky.widget.ClickDecoration;
import com.ttyun.recyclerviewwiki.sticky.widget.IndexDecoration;
import com.ttyun.recyclerviewwiki.sticky.widget.RVDividerDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

/**
 * 创建者     CJR
 * 创建时间   2017-08-02 10:37
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class StickyActivity extends AppCompatActivity {

    private RecyclerView      mRecyclerView;
    private RVStickyAdapter   mAdapter;
    private ArrayList<Person> mDataList;
    private ClickDecoration   mClickDecoration;
    private IndexDecoration   mIndexDecoration;
    private TreeSet<String>   mIndexLetters;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky);

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

    /**
     * 初始化数据
     */
    private void initData() {
        createData();
        mAdapter = new RVStickyAdapter(mDataList);
        mAdapter.setOnItemClickListener(new RVStickyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RVStickyAdapter.ViewHolder holder, int pos) {
                //                Log.e("vivi", mDataList.get(pos).firstPinyin);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mClickDecoration = new ClickDecoration(this);
        //        mRecyclerView.addItemDecoration(mClickDecoration);
        mRecyclerView.addItemDecoration(new AnimatorTitleDecoration(this));
        //        mRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new RVDividerDecoration(this));
        mIndexDecoration = new IndexDecoration(this);
        mIndexDecoration.setLetters(mIndexLetters);
        mRecyclerView.addItemDecoration(mIndexDecoration);

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            private int lastTouchX, lastTouchY;
            private int touchSlop = ViewConfiguration.get(StickyActivity.this).getScaledTouchSlop();     //  滑动的临界值
            private boolean isMove = false;

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (mIndexDecoration.performTouchEvent(rv, e) != -1) {
                    return true;    //  是indexBar的触摸事件, 由IndexDecoration处理
                }

                int x = (int) (e.getX() + 0.5f);
                int y = (int) (e.getY() + 0.5f);

                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchX = x;
                        lastTouchY = y;
                        isMove = false;

                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(x - lastTouchX) > touchSlop || Math.abs(y - lastTouchY) > touchSlop) {
                            isMove = true;
                        }


                        break;
                    case MotionEvent.ACTION_UP:
                        if (!isMove) {
                            //  点击事件, 判断是否decoration处理
                            //                            mClickDecoration.performClick(lastTouchX, lastTouchY);
                            int pos = mClickDecoration.findHeaderPositionUnder(x, y);
                            if (pos != -1) {
                                //                                Log.e("vivi", "点击了头部 --- " + mDataList.get(pos).firstChar);
                            }
                        }

                        break;
                    default:
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                //  是indexBar的触摸事件, 由IndexDecoration处理
                int pos = mIndexDecoration.performTouchEvent(rv, e);
                if (pos != -1) {
                    doToast(mDataList.get(pos).firstChar);
                }
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
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //  这里的firstVisiblePos是第一个可见的位置,这个位置是在mDataList里的位置,
                //  所以用getChildAt(firstVisiblePos)肯定不对的
                final View child = recyclerView.getChildAt(0);
                //  这里和getChildAdapterPosition是一模一样的,ViewHolder的那两个方法才有区别
                final int firstVisiblePos = recyclerView.getChildLayoutPosition(child);
                //   Log.e("vivi", "firstVisiblePos = " + firstVisiblePos);
            }
        });
    }

    private void createData() {
        mDataList = new ArrayList<>();
        mIndexLetters = new TreeSet<>();
        for (String str : Cheeses.NAMES) {
            Person p = new Person();
            p.name = str;
            p.firstPinyin = Pinyin.toPinyin(str.charAt(0));
            p.firstChar = p.firstPinyin.charAt(0) + "";
            mDataList.add(p);
            mIndexLetters.add(p.firstChar);
        }

        Collections.sort(mDataList);
    }

    public void doToast(String content) {
        if (mToast == null) {
            mToast = Toast.makeText(this, content, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            TextView tv = (TextView) mToast.getView().findViewById(android.R.id.message);
            if (tv != null) {
                DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
                float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 22, displayMetrics);
                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, displayMetrics);
                tv.setTextSize(textSize);
                tv.setWidth(width);
                tv.setGravity(Gravity.CENTER);
            }
        } else {
            mToast.setText(content);
        }
        mToast.show();
    }
}
