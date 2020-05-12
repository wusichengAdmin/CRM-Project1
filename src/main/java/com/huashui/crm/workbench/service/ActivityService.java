package com.huashui.crm.workbench.service;

import com.huashui.crm.vo.PaginationVo;
import com.huashui.crm.workbench.domain.Activity;
import com.huashui.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/1/21
 */
public interface ActivityService {
    void saveActivity(Activity a);

    Map<String, Object> pageList(Map<String, Object> paramMap);

    Map<String, Object> pageList1(Map<String, Object> paramMap);


    PaginationVo pageList3(Map<String, Object> paramMap);

    void deleteActivity(String[] ids);

    Map<String, Object> getUserListAndActivityById(String id);

    void updateActivity(Activity a);

    List<Activity> getActivityAll();

    List<Activity> getActivityListByIds(String[] ids);

    int saveActivityList(List<Activity> aList);

    Activity getActivityByIdForOwner(String id);

    List<Activity> getRemarkListById(String activityId);

    void deleteRemarkById(String id);

    void saveActivityRemark(ActivityRemark ac);

    void updateActivityRemark(ActivityRemark ac);

    boolean saveActivity1(Activity a);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityNameAndNotByClueId(String searchName, String clueId);

    List<Activity> getActivityListByActivityName(String searchName);
}
