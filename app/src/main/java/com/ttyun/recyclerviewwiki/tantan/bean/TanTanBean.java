package com.ttyun.recyclerviewwiki.tantan.bean;

/**
 * 创建者     CJR
 * 创建时间   2017-08-15 17:33
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TanTanBean {
    public Object pic;
    public String name;
    public int    age;
    public String constellation;        //  星座
    public String occupation;           //  职业

    public TanTanBean(Object pic, String name, int age, String constellation, String occupation) {
        this.pic = pic;
        this.name = name;
        this.age = age;
        this.constellation = constellation;
        this.occupation = occupation;
    }
}
