package com.ttyun.recyclerviewwiki.renren.widget;

/**
 * 创建者     CJR
 * 创建时间   2017-08-11 10:39
 * 描述	      皇帝翻牌效果的Card的配置信息
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RenRenCardConfig {
    //  屏幕上最多同时显示几张卡片
    public int MAX_SHOW_COUNT;

    //  每一级Scale相差0.05f
    public float SCALE_GAP;

    //  translationY相差7dp左右
    public int TRANS_Y_GAP;

    public RenRenCardConfig(int MAX_SHOW_COUNT, float SCALE_GAP, int TRANS_Y_GAP) {
        this.MAX_SHOW_COUNT = MAX_SHOW_COUNT;
        this.SCALE_GAP = SCALE_GAP;
        this.TRANS_Y_GAP = TRANS_Y_GAP;
    }
}
