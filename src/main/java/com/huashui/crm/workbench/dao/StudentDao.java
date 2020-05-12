package com.huashui.crm.workbench.dao;

import com.huashui.crm.workbench.domain.Student;

import java.util.List;
import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/2/28
 */
public interface StudentDao {
    void saveStudent(Student s);

    List<Student> getStudent(Map<String, Object> pMap);

    int getTotalStudent(Map<String, Object> pMap);
}
