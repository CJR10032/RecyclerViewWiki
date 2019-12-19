package com.ttyun.recyclerviewwiki.hive.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ttyun.recyclerviewwiki.R;
import com.ttyun.recyclerviewwiki.hive.widget.HiveView;
import com.ttyun.recyclerviewwiki.main.utils.ToastUtil;

import java.util.ArrayList;

/**
 * 创建者     CJR
 * 创建时间   2017-08-30 10:06
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private ArrayList<Object> mImageList;

    public RVAdapter() {
    }

    public RVAdapter(ArrayList<Object> imageList) {
        this.mImageList = imageList;
    }

    public void setImageList(ArrayList<Object> imageList) {
        this.mImageList = imageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_hive, parent, false);//  这里传了parent, 所以会根据布局要求的宽高来绘制
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mImageList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mImageList == null ? 0 : mImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements HiveView.OnHiveClickListener {

        public final HiveView hiveView;
        public final TextView tvNum;
        private int curPos = -1;

        public ViewHolder(View itemView) {
            super(itemView);
            hiveView = (HiveView) itemView.findViewById(R.id.item_view_hive_hv);
            tvNum = (TextView) itemView.findViewById(R.id.item_view_hiv_num_tv);

            hiveView.setOnHiveClickListener(this);
        }

        public void setData(Object img, int pos) {
            curPos = pos;
            Glide.with(itemView.getContext())
                    .load(img)
                    //                    .placeholder(R.drawable.avatar_middle)
                    .into(hiveView);
            tvNum.setText(pos + "");

        }

        @Override
        public void onHiveClick(View v) {
            Log.e("vivi", String.format("点击了编号 %s 的蜂窝", curPos));
            ToastUtil.doToast(itemView.getContext(), String.format("点击了编号 %s 的蜂窝", curPos > 9 ? curPos : "0" + curPos));
        }
    }
}
