package com.huashui.crm.workbench.dao;

import com.huashui.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/1/21
 */
public interface ActivityDao {
    void saveActivity(Activity a);

    List<Activity> getActivityListByCondition1(Map<String, Object> paramMap);

    int getTotalByCondition1(Map<String, Object> paramMap);

    List<Activity> getActivityListByCondition2(Map<String, Object> paramMap);

    int getTotalByCondition2(Map<String, Object> paramMap);

    void deleteActivityByIds(String[] ids);

    Activity getActivityById(String id);

    void    updateActivity(Activity a);

    List<Activity> getActivityAll();

    List<Activity> getActivityListByIds(String[] ids);

    int saveActivityList(List<Activity> aList);

    Activity getActivityByIdForOwner(String id);

    int saveActivity1(Activity a);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityNameAndNotByClueId(Map<String,String> map);

    List<Activity> getActivityListByActivityName(String searchName);
}
