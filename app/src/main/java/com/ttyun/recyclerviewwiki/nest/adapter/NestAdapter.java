package com.ttyun.recyclerviewwiki.nest.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ttyun.recyclerviewwiki.R;
import com.ttyun.recyclerviewwiki.main.adapter.RVAdapter;
import com.ttyun.recyclerviewwiki.main.utils.CountUtil;
import com.ttyun.recyclerviewwiki.nest.bean.NestTestBean;

import java.util.ArrayList;

/**
 * 创建者     CJR
 * 创建时间   2017-06-30 13:52
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class NestAdapter extends RecyclerView.Adapter<NestAdapter.ViewHolder> {

    private ArrayList<NestTestBean> dataList;

    public NestAdapter(ArrayList<NestTestBean> dataList) {
        this.dataList = dataList;
    }

    @Override
    public NestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //  不能填true  java.lang.IllegalStateException: The specified child already has a parent.
        // You must call removeView() on the child's parent first.
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_view_nest, parent, false));  //  效果可以的
        //        return new ViewHolder(View.inflate(parent.getContext(), R.layout
        // .item_view_nest, null));
    }

    @Override
    public void onBindViewHolder(NestAdapter.ViewHolder holder, int position) {
        holder.setData(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView topTv, bottomTv;
        private RecyclerView rv;
        private RVAdapter    adapter;

        public ViewHolder(View itemView) {
            super(itemView);
            CountUtil.countLog(0, "NestAdapter的 ViewHolder创建");
            topTv = (TextView) itemView.findViewById(R.id.nest_item_top);
            bottomTv = (TextView) itemView.findViewById(R.id.nest_item_bottom);
            rv = (RecyclerView) itemView.findViewById(R.id.nest_item_rv);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
            //            layoutManager.setSmoothScrollbarEnabled(true);
            //            layoutManager.setAutoMeasureEnabled(true);
            rv.setLayoutManager(layoutManager);
            //            rv.setHasFixedSize(true);
            //            rv.setNestedScrollingEnabled(false);
            adapter = new RVAdapter(null, null);
            rv.setAdapter(adapter);
        }

        public void setData(NestTestBean bean, int pos) {
            topTv.setText(bean.top);
            bottomTv.setText(bean.bottom);
            adapter.setDataList(bean.dataList);
            adapter.notifyDataSetChanged();
        }
    }
}
