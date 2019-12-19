package com.ttyun.recyclerviewwiki.payloads.callback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.ttyun.recyclerviewwiki.payloads.bean.TestBean;

import java.util.List;

/**
 * 创建者     CJR
 * 创建时间   2017-08-10 16:13
 * 描述	      DiffUtil是support-v7:24.2.0中的新工具类,它用来比较两个数据集,寻找出旧数据集 -> 新数据集的最小变化量, 主要是做增量更新的
 * *          Callback是核心类, 用来判断 新旧item是否相等,
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class PayloadCallback extends DiffUtil.Callback {

    private List<TestBean> mOldDatas, mNewDatas;

    public PayloadCallback(List<TestBean> oldDatas, List<TestBean> newDatas) {
        mOldDatas = oldDatas;
        mNewDatas = newDatas;
    }

    /**
     * @return 老数据的size
     */
    @Override
    public int getOldListSize() {
        return mOldDatas == null ? 0 : mOldDatas.size();
    }

    /**
     * @return 新数据的size
     */
    @Override
    public int getNewListSize() {
        return mNewDatas == null ? 0 : mNewDatas.size();
    }

    /**
     * 英文版注释看父类代码
     * <p/>
     * 根据一些字段判断两个条目是否相同, 如id唯一的时候可以通过id判断, 名字唯一的时候可以通过名字判断之类的
     *
     * @param oldItemPosition 旧的pos
     * @param newItemPosition 新的pos
     * @return 两个条目的名字是否相同 (这里用名字判断,名字相同看为相同的条目)
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        String oldName = mOldDatas.get(oldItemPosition).name;
        String newName = mNewDatas.get(newItemPosition).name;
        return oldName.equals(newName);
    }

    /**
     * 英文版注释看父类代码
     * <p/>
     * 当两个item是相同的时候, 也就是areItemTheSame返回true时, 会调用该方法来检查这个item的数据是否有更新
     *
     * @param oldItemPosition 旧的pos
     * @param newItemPosition 新的pos
     * @return 这个条目是否有更新
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        TestBean oldBean = mOldDatas.get(oldItemPosition);
        TestBean newBean = mNewDatas.get(newItemPosition);
        //  名字在areItemTheSame判断过了, 相同才会走这个方法, 所以这里不判断名字
        return oldBean.desc.equals(newBean.desc) && oldBean.pic.equals(newBean.pic);
    }

    //  =================== 以上四个方法是DiffUtil.Callback的基本用法, 接下来是搞基用法 ====================

    /**
     * areItemsTheSame返回true表明是同一条目, areContentsTheSame返回false表明数据有更新的时候才会调用该方法
     * 该方法实现定向刷新中的部分更新, 能提高效率 (实现了该方法之后就没有系统的自带动画了)
     *
     * @param oldItemPosition 旧的pos
     * @param newItemPosition 新的pos
     * @return List payloads   对应到Adapter的onBindViewHolder第三个参数
     */
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        TestBean oldBean = mOldDatas.get(oldItemPosition);
        TestBean newBean = mNewDatas.get(newItemPosition);

        //  这里就不用比较 名字 了areItemsTheSame比较过了,一定相等
        Bundle payload = new Bundle();
        if (!oldBean.desc.equals(newBean.desc)) {
            payload.putString("KEY_DESC", newBean.desc);
        }
        if (!oldBean.pic.equals(newBean.pic)) {
            payload.putString("KEY_AVATER", newBean.pic);
        }
        if (payload.isEmpty()) {        //  如果没有变化就传空, 这样就不会回调 onBindViewHolder方法? 因为onBindViewHolder的payload 是non-null的
            return null;
        }
        return payload;
    }
}
