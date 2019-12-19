package com.ttyun.recyclerviewwiki.renren.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ttyun.recyclerviewwiki.R;
import com.ttyun.recyclerviewwiki.renren.bean.RenRenCardBean;

import java.util.ArrayList;

/**
 * 创建者     CJR
 * 创建时间   2017-08-12 09:21
 * 描述
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述
 */
public class RenRenCardAdapter extends RecyclerView.Adapter<RenRenCardAdapter.ViewHolder> {

    private ArrayList<RenRenCardBean> mDataList;

    public RenRenCardAdapter(ArrayList<RenRenCardBean> dataList) {
        mDataList = dataList;
    }

    public void setDataList(ArrayList<RenRenCardBean> dataList) {
        mDataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_renren_card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mDataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView picIv, favourIv, delIv;
        public final TextView indexTv, nameTv;

        public ViewHolder(View itemView) {
            super(itemView);

            picIv = (ImageView) itemView.findViewById(R.id.renren_card_pic_iv);
            favourIv = (ImageView) itemView.findViewById(R.id.renren_card_iv_love);
            delIv = (ImageView) itemView.findViewById(R.id.renren_card_iv_del);
            indexTv = (TextView) itemView.findViewById(R.id.renren_card_index_tv);
            nameTv = (TextView) itemView.findViewById(R.id.renren_card_name_tv);
        }

        public void setData(RenRenCardBean bean, int pos) {
            Glide.with(itemView.getContext()).load(bean.img).placeholder(R.mipmap.ic_launcher).into(picIv);
            indexTv.setText(String.format("%s/%s", bean.pos + 1, mDataList.size()));
            nameTv.setText(bean.name);
        }
    }
}
