package com.huashui.crm.settings.domain;

/**
 * 华水吴彦祖
 * 2020/1/13
 */
public class DicType {

    private String code; //字典类型编码  主键
    private String name; //类型名称
    private String description;//类型描述

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
