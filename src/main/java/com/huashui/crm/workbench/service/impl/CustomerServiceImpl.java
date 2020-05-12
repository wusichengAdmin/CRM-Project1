package com.huashui.crm.workbench.service.impl;

import com.huashui.crm.workbench.dao.CustomerDao;
import com.huashui.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 华水吴彦祖
 * 2020/2/18
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public List<String> getCustomerNameByName(String name) {

        List<String> sList = customerDao.getCustomerNamesByName(name);

        return sList;
    }
}
