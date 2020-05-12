package com.huashui.crm.workbench.service.impl;

import com.huashui.crm.workbench.dao.TranHistoryDao;
import com.huashui.crm.workbench.service.TranHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 华水吴彦祖
 * 2020/2/19
 */
@Service
public class TranHistoryServiceImpl implements TranHistoryService {

    @Autowired
    private TranHistoryDao tranHistoryDao;


}
