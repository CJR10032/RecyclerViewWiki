package com.ttyun.recyclerviewwiki.payloads.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ttyun.recyclerviewwiki.R;
import com.ttyun.recyclerviewwiki.payloads.bean.TestBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     CJR
 * 创建时间   2017-08-10 10:52
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class PayloadAdapter extends RecyclerView.Adapter<PayloadAdapter.ViewHolder> {

    private ArrayList<TestBean> mDataList;

    public void setDataList(ArrayList<TestBean> dataList) {
        mDataList = dataList;
    }

    @Override
    public PayloadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_payloads, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public void onBindViewHolder(PayloadAdapter.ViewHolder holder, int position) {
        holder.setData(mDataList.get(position), position);
    }

    /**
     * 增量更新需要重载的方法
     *
     * @param holder   ViewHolder
     * @param position 当前的pos
     * @param payloads 有效载荷
     */
    @Override
    public void onBindViewHolder(PayloadAdapter.ViewHolder holder, int position, List payloads) {
        if (payloads.isEmpty()) {       //  payloads有效载荷是non-null的, 所以只需要判断是长度否为0就可以了
            onBindViewHolder(holder, position);
            Log.e("vivi", position + " == 位置的数据需要整个条目刷新");
        } else {
            //  获取增量更新的数据
            Bundle payload = (Bundle) payloads.get(0);
            TestBean bean = mDataList.get(position);
            for (String key : payload.keySet()) {
                switch (key) {
                    case "KEY_DESC":        //  desc 有更新
                        Log.e("vivi", position + " == 位置的数据 desc 需要刷新");
                        holder.descTv.setText(bean.desc);       //  bean的数据也是新的可以用, 也可以用Bundle里的数据
                        break;
                    case "KEY_AVATER":      //  pic 有更新
                        Log.e("vivi", position + " == 位置的数据 pic 需要刷新");
                        Glide.with(holder.itemView.getContext()).load(bean.pic).placeholder(R.mipmap.ic_launcher).into(holder.picIv);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView picIv;
        public final TextView  nameTv;
        public final TextView  descTv;

        public ViewHolder(View itemView) {
            super(itemView);

            picIv = (ImageView) itemView.findViewById(R.id.item_payloads_pic);
            nameTv = (TextView) itemView.findViewById(R.id.item_payloads_name);
            descTv = (TextView) itemView.findViewById(R.id.item_payloads_desc);
        }

        public void setData(TestBean data, int pos) {
            nameTv.setText(data.name);
            descTv.setText(data.desc);
            Glide.with(itemView.getContext()).load(data.pic).placeholder(R.mipmap.ic_launcher).into(picIv);
        }
    }
}
