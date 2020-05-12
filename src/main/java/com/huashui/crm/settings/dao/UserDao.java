package com.huashui.crm.settings.dao;

import com.huashui.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/1/9
 */
public interface UserDao {
    User login(Map<String, String> map);

    List<User> getUserList();
}
