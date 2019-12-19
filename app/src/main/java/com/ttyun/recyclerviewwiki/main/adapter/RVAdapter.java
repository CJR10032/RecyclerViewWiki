package com.ttyun.recyclerviewwiki.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ttyun.recyclerviewwiki.R;
import com.ttyun.recyclerviewwiki.main.utils.CountUtil;
import com.ttyun.recyclerviewwiki.utils.LogUtils;

import java.util.ArrayList;

/**
 * 创建者     CJR
 * 创建时间   2017-06-30 09:24
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private ArrayList<String>   dataList;
    private OnItemClickListener listener;

    public RVAdapter(ArrayList<String> dataList, OnItemClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    public void setDataList(ArrayList<String> dataList) {
        this.dataList = dataList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogUtils.myLog("RVAdapter的onCreateViewHolder方法调用了");
        return new ViewHolder(View.inflate(parent.getContext(), R.layout.item_view_main, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv;
        private int curPos = -1;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv = (TextView) itemView.findViewById(R.id.tv);
            CountUtil.countLog(1, "RVAdapter里的 ViewHolder创建");
        }

        public void setData(String data, int pos) {
            curPos = pos;
            tv.setText(data);
        }


        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onItemClick(this, curPos);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder, int pos);
    }
}
