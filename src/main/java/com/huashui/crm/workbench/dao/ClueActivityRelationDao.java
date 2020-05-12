package com.huashui.crm.workbench.dao;

import com.huashui.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    void deleteRelationById(String id);

    void insertActivityBind(List<ClueActivityRelation> carList);

    List<ClueActivityRelation> getRelationListByClueId(String clueId);

    void deleteRelationByClueId(String clueId);
}
