package com.huashui.crm.workbench.dao;

import com.huashui.crm.workbench.domain.Tran;
import com.huashui.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranDao {

    void saveTran(Tran t);

    Tran getTranByIdForOwner(String id);

    List<TranHistory> getHistoryListByTranId(String tranId);

    void updateStage(Tran t);

    List<String> getNameList();

    int getMinStageCount();

    int getMaxStageCount();

    List<Map<String, Object>> getDataList();
}
