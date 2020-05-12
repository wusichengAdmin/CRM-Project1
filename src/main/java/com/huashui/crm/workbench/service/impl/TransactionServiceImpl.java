package com.huashui.crm.workbench.service.impl;

import com.huashui.crm.workbench.dao.TranDao;
import com.huashui.crm.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 华水吴彦祖
 * 2020/2/18
 */

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TranDao tranDao;

}
