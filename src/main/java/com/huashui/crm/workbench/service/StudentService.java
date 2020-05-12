package com.huashui.crm.workbench.service;

import com.huashui.crm.workbench.domain.Student;

import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/2/28
 */
public interface StudentService {


    void saveStudent(Student s);

    Map<String, Object> getStudent(Map<String, Object> pMap);
}
