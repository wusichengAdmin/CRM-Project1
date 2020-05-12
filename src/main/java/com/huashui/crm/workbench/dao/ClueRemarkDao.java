package com.huashui.crm.workbench.dao;

import com.huashui.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getRemarkListByClueId(String clueId);

    void deleteRemarkByClueId(String clueId);
}
