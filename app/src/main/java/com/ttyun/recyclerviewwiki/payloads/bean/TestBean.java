package com.ttyun.recyclerviewwiki.payloads.bean;

/**
 * 创建者     CJR
 * 创建时间   2017-08-10 10:35
 * 描述	      测试RecyclerView增量更新,DiffUtil工具类 的bean
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TestBean implements Cloneable {
    public String name;     //  姓名
    public String desc;     //  简介
    public String pic;      //  图片

    public TestBean(String name, String desc, String pic) {
        this.name = name;
        this.desc = desc;
        this.pic = pic;
    }

    @Override
    public TestBean clone() {
        TestBean bean = null;
        try {
            bean = (TestBean) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return bean;
    }
}
