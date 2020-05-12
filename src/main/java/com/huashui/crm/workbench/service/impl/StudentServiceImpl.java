package com.huashui.crm.workbench.service.impl;

import com.huashui.crm.workbench.dao.StudentDao;
import com.huashui.crm.workbench.domain.Student;
import com.huashui.crm.workbench.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/2/28
 */

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentDao studentDao;

    @Override
    public void saveStudent(Student s) {

        studentDao.saveStudent(s);

    }

    @Override
    public Map<String, Object> getStudent(Map<String, Object> pMap) {

        //取得学生信息dataList
        List<Student> dataList = studentDao.getStudent(pMap);

        //取得总数total
        int total = studentDao.getTotalStudent(pMap);

        Map<String,Object> map = new HashMap<>();
        map.put("dataList", dataList);
        map.put("total",total);

        return map;
    }
}
