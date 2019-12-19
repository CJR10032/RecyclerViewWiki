package com.ttyun.recyclerviewwiki.sticky.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ttyun.recyclerviewwiki.R;
import com.ttyun.recyclerviewwiki.sticky.bean.Person;

import java.util.ArrayList;

/**
 * 创建者     CJR
 * 创建时间   2017-08-02 10:48
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RVStickyAdapter extends RecyclerView.Adapter<RVStickyAdapter.ViewHolder> {

    private ArrayList<Person>   dataList;
    private OnItemClickListener listener;

    public RVStickyAdapter(ArrayList<Person> datas) {
        this.dataList = datas;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_sticky, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public ArrayList<Person> getDataList() {
        return dataList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv;
        private int curPos = -1;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
            itemView.setOnClickListener(this);
        }

        public void setData(Person data, int pos) {
            curPos = pos;
            tv.setText(data.name);
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
