package com.ttyun.recyclerviewwiki.sticky.bean;

/**
 * 创建者     CJR
 * 创建时间   2017-08-02 14:33
 * 描述	      记录人的信息的bean
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class Person implements Comparable<Person> {
    public String name;
    public String firstChar;

    public String firstPinyin;      //  名字首字的大写全拼

    @Override
    public int compareTo(Person o) {
        return this.firstPinyin.compareTo(o.firstPinyin);
    }
}
