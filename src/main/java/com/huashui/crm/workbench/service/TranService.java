package com.huashui.crm.workbench.service;

import com.huashui.crm.workbench.domain.Tran;
import com.huashui.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/2/19
 */
public interface TranService {

    void saveTran(Tran t, String customerName);

    Tran getTranByIdForOwner(String id);

    List<TranHistory> getHistoryListByTranId(String tranId);

    void updateStage(Tran t);

    Map<String, Object> getChart();
}
