package com.huashui.crm.settings.service.impl;

import com.huashui.crm.exception.LoginException;
import com.huashui.crm.settings.dao.UserDao;
import com.huashui.crm.settings.domain.User;
import com.huashui.crm.settings.service.UserService;
import com.huashui.crm.utils.Const;
import com.huashui.crm.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/1/9
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws  LoginException {


        //1.验证账号密码
        Map<String,String> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        User user = userDao.login(map);

        if(user == null){
            throw new LoginException("账号密码错误");
        }
        /*
        * 如果程序能够走到下面这行。说明上面的if语句块没有执行，说明账号密码正确
        * 接下来我们继续验证其他项
        *
        * */

        //2.验证失效时间
        String expireTime = user.getExpireTime();
        if(expireTime.compareTo(DateTimeUtil.getSysTime())<0){
            throw new LoginException("账号已失效");
        }

        //验证锁定状态
        String lockState = user.getLockState();
        if(Const.LOCK_STATE_CLOSE.equals(lockState)){
            throw new LoginException("账号已锁定");
        }

        //验证ip地址
        String allowIps = user.getAllowIps();
        if(!allowIps.contains(ip)){
            throw new LoginException("ip地址受限");

        }

        /*如果程序能够走到这行，说明以上四处的抛异常都没有和执行
         * 所有的验证都是正确的
         * 则登陆成功
         * */
        //返回user对象
        return user;
    }

    @Override
    public List<User> getUserList() {
        List<User> uList = userDao.getUserList();
        return uList;
    }
}
