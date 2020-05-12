package com.huashui.crm.vo;

import java.util.List;

/**
 * 华水吴彦祖
 * 2020/2/5
 */
public class PaginationVo<T> {

    private List<T> dataList;

    private int total;

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
