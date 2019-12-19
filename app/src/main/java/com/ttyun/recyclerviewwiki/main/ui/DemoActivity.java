package com.ttyun.recyclerviewwiki.main.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.ttyun.recyclerviewwiki.R;

/**
 * 创建者     CJR
 * 创建时间   2017-08-16 16:52
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class DemoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        initView();
        initData();
        initListener();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mIv = (ImageView) findViewById(R.id.iv);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        //        Glide.with(this).load(R.drawable.img_avatar_03).transform(new CornersTransform(this, 12)).placeholder(R.mipmap.ic_launcher).into(mIv);      //  设置图片    这里要消除 顶上2个角
       /* Glide.with(this)
                .load(R.drawable.a3)
                .bitmapTransform(new CropSquareTransformation(this),
                        //                        new CropTransformation(this),
                        new RoundedCornersTransformation(this, 30, 0, RoundedCornersTransformation.CornerType.ALL))
                .placeholder(R.mipmap.ic_launcher)
                .into(mIv);
       */
        //        mIv.setRotation(/*0.5f*/3);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        mIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv:

                break;

            default:
                break;
        }
    }
}
