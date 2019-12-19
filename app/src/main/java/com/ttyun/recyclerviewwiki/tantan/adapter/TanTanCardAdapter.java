package com.ttyun.recyclerviewwiki.tantan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ttyun.recyclerviewwiki.R;
import com.ttyun.recyclerviewwiki.tantan.bean.TanTanBean;

import java.util.ArrayList;

/**
 * 创建者     CJR
 * 创建时间   2017-08-16 08:59
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TanTanCardAdapter extends RecyclerView.Adapter<TanTanCardAdapter.ViewHolder> {


    private ArrayList<TanTanBean> mDataList;
    private OnItemSwipeListener   mListener;

    public TanTanCardAdapter(ArrayList<TanTanBean> dataList) {
        mDataList = dataList;
    }

    public TanTanCardAdapter(ArrayList<TanTanBean> dataList, OnItemSwipeListener listener) {
        mDataList = dataList;
        mListener = listener;
    }

    public void setDataList(ArrayList<TanTanBean> dataList) {
        mDataList = dataList;
    }

    public void setListener(OnItemSwipeListener listener) {
        mListener = listener;
    }

    public ArrayList<TanTanBean> getDataList() {
        return mDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //      用CardView 在5.0以上机子跑的时候, 底部的分界线不明显, 都连在一起成为一体了
        //        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tantan_card_view, parent, false);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tantan_view, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

        public final ImageView avatarIv;
        public final ImageView deleteIv;
        public final ImageView favourIv;
        public final TextView  nameTv;
        public final TextView  ageTv;
        public final TextView  constellationTv;     //  星座
        public final TextView  occupationTv;     //  星座
        private int curPos = -1;

        //        private final RoundedCornersTransformation connerTransformation;        //  Glide 处理圆角的
        //        private final CropSquareTransformation     squareTransformation;        //  Glide 裁剪的, centerCrop

        public ViewHolder(View itemView) {
            super(itemView);

            avatarIv = (ImageView) itemView.findViewById(R.id.tantan_card_avatar_iv);
            deleteIv = (ImageView) itemView.findViewById(R.id.tantan_card_dislike_iv);
            favourIv = (ImageView) itemView.findViewById(R.id.tantan_card_like_iv);
            nameTv = (TextView) itemView.findViewById(R.id.tantan_card_name_tv);
            ageTv = (TextView) itemView.findViewById(R.id.tantan_card_age_tv);
            constellationTv = (TextView) itemView.findViewById(R.id.tantan_card_constellation_tv);  //  星座
            occupationTv = (TextView) itemView.findViewById(R.id.tantan_card_occupation_tv);  //  职业

            //            connerTransformation = new RoundedCornersTransformation(itemView.getContext(), 4, 0, RoundedCornersTransformation.CornerType.TOP);
            //            squareTransformation = new CropSquareTransformation(itemView.getContext());
            itemView.setOnTouchListener(this);
        }

        public void setData(TanTanBean bean, int pos) {
            curPos = pos;
            Glide.with(itemView.getContext())
                    .load(bean.pic)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(avatarIv);      //  RoundImageView 设置图片
           /* Glide.with(itemView
                    .getContext())
                    .load(bean.pic)
                    .bitmapTransform(squareTransformation, connerTransformation)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(avatarIv);*/      //  设置图片    这里要消除 顶上2个角 , 这样做有空隙
            nameTv.setText(bean.name);
            ageTv.setText(String.format("♀ %s", bean.age));     //  这里的性别没有做设置, 全部都是♀
            constellationTv.setText(bean.constellation);
            occupationTv.setText(bean.occupation);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mListener != null) {
                mListener.onItemSwipe(this, curPos);
            }
            return false;
        }
    }

    public interface OnItemSwipeListener {
        void onItemSwipe(ViewHolder holder, int pos);
    }
}
