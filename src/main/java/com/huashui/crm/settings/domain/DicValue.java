package com.huashui.crm.settings.domain;

/**
 * 华水吴彦祖
 * 2020/1/13
 */
public class DicValue {

    private String id; //主键
    private String value; //字典值
    private String text; //字典值对应的文本
    private String orderNo; //按序号，排序
    private String typeCode; //字典类型编码  外键  指向tbl_dic_type表

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
}
