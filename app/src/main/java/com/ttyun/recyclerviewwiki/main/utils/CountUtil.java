package com.ttyun.recyclerviewwiki.main.utils;

import android.util.Log;
import android.util.SparseArray;

/**
 * 创建者     CJR
 * 创建时间   2017-07-03 09:06
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class CountUtil {
    private static SparseArray<Integer> mSparseArray = new SparseArray<>();

    public static void countLog(int key, String msg) {
        Integer count = mSparseArray.get(key);
        if (count == null) {
            count = 0;
        }
        count++;
        mSparseArray.put(key, count);
        Log.e("vivi", String.format("%s, 第 %d 次", msg, count));
    }

    public static void setCount(int key, int count) {
        mSparseArray.put(key, count);
    }
}
