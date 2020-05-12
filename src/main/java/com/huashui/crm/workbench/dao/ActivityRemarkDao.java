package com.huashui.crm.workbench.dao;

import com.huashui.crm.workbench.domain.Activity;
import com.huashui.crm.workbench.domain.ActivityRemark;

import java.util.List;

/**
 * 华水吴彦祖
 * 2020/1/21
 */
public interface ActivityRemarkDao {
    void deleteRemarkByAids(String[] ids);

    List<Activity> getRemarkListById(String activityId);

    void deleteRemarkById(String id);

    void saveActivityRemark(ActivityRemark ac);

    void updateActivityRemark(ActivityRemark ac);
}
