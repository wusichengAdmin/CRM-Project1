package com.huashui.crm.settings.service;

import com.huashui.crm.exception.LoginException;
import com.huashui.crm.settings.domain.User;

import java.util.List;

/**
 * 华水吴彦祖
 * 2020/1/9
 */

public interface UserService {

    User login(String loginAct,String loginPwd,String ip) throws LoginException;

    List<User> getUserList();
}
