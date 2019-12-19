package com.ttyun.recyclerviewwiki.tantan.widget;

/**
 * 创建者     CJR
 * 创建时间   2017-08-11 10:39
 * 描述	      皇帝翻牌效果的Card的配置信息
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TanTanCardConfig {
    //  屏幕上最多同时显示几张卡片
    public int MAX_SHOW_COUNT;

    //  每一级Scale相差0.05f
    public float SCALE_GAP;

    //  translationY = itemHeight / TRANS_Y_RATIO, 这里默认取 14等分
    public float TRANS_Y_RATIO;

    //  第一张卡片最大旋转角度
    public float MAX_ROTATION;

    public TanTanCardConfig(int MAX_SHOW_COUNT, float SCALE_GAP,
                            float TRANS_Y_RATIO, float MAX_ROTATION) {
        this.MAX_SHOW_COUNT = MAX_SHOW_COUNT;
        this.SCALE_GAP = SCALE_GAP;
        this.TRANS_Y_RATIO = TRANS_Y_RATIO;
        this.MAX_ROTATION = MAX_ROTATION;
    }
}
