package com.huashui.crm.workbench.dao;

import com.huashui.crm.workbench.domain.Activity;
import com.huashui.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {

    void saveClue(Clue c);

    Clue getClueByIdForOwner(String id);

    Clue getClueById(String clueId);

    void deleteById(String clueId);

    List<Activity> getClueListByCondition(Map<String, Object> paramMap);

    int getTotalByCondition(Map<String, Object> paramMap);
}
