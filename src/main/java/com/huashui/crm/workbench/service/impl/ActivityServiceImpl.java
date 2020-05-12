package com.huashui.crm.workbench.service.impl;

import com.huashui.crm.settings.dao.UserDao;
import com.huashui.crm.settings.domain.User;
import com.huashui.crm.vo.PaginationVo;
import com.huashui.crm.workbench.dao.ActivityDao;
import com.huashui.crm.workbench.dao.ActivityRemarkDao;
import com.huashui.crm.workbench.domain.Activity;
import com.huashui.crm.workbench.domain.ActivityRemark;
import com.huashui.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/1/21
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private ActivityRemarkDao activityRemarkDao;

    @Autowired
    private UserDao userDao;

    @Override
    public void saveActivity(Activity a) {
        activityDao.saveActivity(a);
    }

    @Override
    public Map<String, Object> pageList(Map<String, Object> paramMap) {

        //取得dataList
        List<Activity> dataList = activityDao.getActivityListByCondition1(paramMap);

        //取得total
        int total = activityDao.getTotalByCondition1(paramMap);


        //创建一个map，将dataList和total放到map中返回
        Map<String,Object> map = new HashMap<>();
        map.put("dataList", dataList);
        map.put("total", total);

        return map;
    }

    @Override
    public Map<String, Object> pageList1(Map<String, Object> paramMap) {
        //取得dataList
        List<Activity> dataList = activityDao.getActivityListByCondition2(paramMap);

        //取得total
        int total = activityDao.getTotalByCondition2(paramMap);


        //创建一个map，将dataList和total放到map中返回
        Map<String,Object> map = new HashMap<>();
        map.put("dataList", dataList);
        map.put("total", total);

        return map;

    }

    @Override
    public PaginationVo pageList3(Map<String, Object> paramMap) {
        //取得dataList
        List<Activity> dataList = activityDao.getActivityListByCondition2(paramMap);

        //取得total
        int total = activityDao.getTotalByCondition2(paramMap);

        //创建一个vo对象，将dataList和total封装起来
        PaginationVo<Activity> vo = new PaginationVo<>();
        vo.setDataList(dataList);
        vo.setTotal(total);
        return vo;
    }

    @Override
    public void deleteActivity(String[] ids) {

        //删除市场相关活动关联的备注信息
        activityRemarkDao.deleteRemarkByAids(ids);

        //删除市场活动
        activityDao.deleteActivityByIds(ids);
    }

    @Override
    public Map<String, Object> getUserListAndActivityById(String id) {

        //取得uList
        List<User> uList = userDao.getUserList();

        //取得Activity a
        Activity a = activityDao.getActivityById(id);

        //创建一个map，将uList和a放到map中返回
        Map<String,Object> map = new HashMap<>();
        map.put("uList", uList);
        map.put("a", a);
        return map;

    }

    @Override
    public void updateActivity(Activity a) {
        activityDao.updateActivity(a);
    }

    @Override
    public List<Activity> getActivityAll() {

        List<Activity> aList = activityDao.getActivityAll();

        return aList;
    }

    @Override
    public List<Activity> getActivityListByIds(String[] ids) {

        List<Activity> aList = activityDao.getActivityListByIds(ids);

        return aList;
    }

    @Override
    public int saveActivityList(List<Activity> aList) {
        int count = activityDao.saveActivityList(aList);

        return count;
    }

    @Override
    public Activity getActivityByIdForOwner(String id) {

        Activity a = activityDao.getActivityByIdForOwner(id);


        return a;
    }

    @Override
    public List<Activity> getRemarkListById(String activityId) {

        List<Activity> acList = activityRemarkDao.getRemarkListById(activityId);

        return acList;

    }

    @Override
    public void deleteRemarkById(String id) {

        activityRemarkDao.deleteRemarkById(id);
    }

    @Override
    public void saveActivityRemark(ActivityRemark ac) {

        activityRemarkDao.saveActivityRemark(ac);

    }

    @Override
    public void updateActivityRemark(ActivityRemark ac) {
        activityRemarkDao.updateActivityRemark(ac);
    }

    @Override
    public boolean saveActivity1(Activity a) {
        boolean flag = true;

        int count = activityDao.saveActivity1(a);

        if(count != 1){

            flag = false;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {

        List<Activity> aList = activityDao.getActivityListByClueId(clueId);


        return aList;
    }

    @Override
    public List<Activity> getActivityNameAndNotByClueId(String searchName, String clueId) {

        Map<String,String> map = new HashMap<>();
        map.put("searchName", searchName);
        map.put("clueId", clueId);

        List<Activity> aList = activityDao.getActivityNameAndNotByClueId(map);



        return aList;
    }

    @Override
    public List<Activity> getActivityListByActivityName(String searchName) {

        List<Activity> aList = activityDao.getActivityListByActivityName(searchName);

        return aList;
    }
}
