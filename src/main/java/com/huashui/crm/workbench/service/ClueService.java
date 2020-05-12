package com.huashui.crm.workbench.service;

import com.huashui.crm.vo.PaginationVo;
import com.huashui.crm.workbench.domain.Clue;
import com.huashui.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/2/12
 */
public interface ClueService {

    void saveClue(Clue c);

    Clue getClueByIdForOwner(String id);

    void deleteRelationById(String id);

    void insertActivityBind(String clueId, String[] activityIds);

    void convert(String clueId, Tran t, String createBy, String flag);

    PaginationVo pageList(Map<String, Object> paramMap);
}
