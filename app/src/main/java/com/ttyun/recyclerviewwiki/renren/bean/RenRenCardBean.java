package com.ttyun.recyclerviewwiki.renren.bean;

/**
 * 创建者     CJR
 * 创建时间   2017-08-12 16:13
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RenRenCardBean {
    public int    pos;      //  记录该bean的位置, 从0开始
    public Object img;
    public String name;

    public RenRenCardBean(int pos, Object img, String name) {
        this.pos = pos;
        this.img = img;
        this.name = name;
    }
}
