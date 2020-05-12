package com.huashui.crm.workbench.service.impl;

import com.huashui.crm.utils.DateTimeUtil;
import com.huashui.crm.utils.UUIDUtil;
import com.huashui.crm.workbench.dao.CustomerDao;
import com.huashui.crm.workbench.dao.TranDao;
import com.huashui.crm.workbench.dao.TranHistoryDao;
import com.huashui.crm.workbench.domain.Customer;
import com.huashui.crm.workbench.domain.Tran;
import com.huashui.crm.workbench.domain.TranHistory;
import com.huashui.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/2/19
 */
@Service
public class TranServiceImpl implements TranService {

    @Autowired
    private TranDao tranDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private TranHistoryDao tranHistoryDao;


    @Override
    public void saveTran(Tran t, String customerName) {

        /*
           1.处理客户信息
              根据用户在表单中填写的客户信息，到客户表中进行查询
              如果有此客户，则将该客户取出，取得其id，将该id值封装到t对象的customerId属性中
              如果没有此客户，则需要新建一个客户，取得id，将该id值封装到t对象的customerId属性中

         */

        Customer cus = customerDao.getCustomerByName(customerName);

        if(cus==null){

            cus = new Customer();

            cus.setId(UUIDUtil.getUUID());
            cus.setCreateBy(t.getCreateBy());
            cus.setCreateTime(DateTimeUtil.getSysTime());
            cus.setName(customerName);
            cus.setOwner(t.getOwner());
            cus.setNextContactTime(t.getNextContactTime());
            cus.setDescription(t.getDescription());
            cus.setContactSummary(t.getContactSummary());

            //添加客户
            customerDao.saveCustomer(cus);

        }

        t.setCustomerId(cus.getId());

        //2.添加交易
        tranDao.saveTran(t);

        //3.添加交易历史
        TranHistory th = new TranHistory();

        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setCreateBy(t.getCreateBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setStage(t.getStage());

        tranHistoryDao.saveTranHistory(th);


    }

    @Override
    public Tran getTranByIdForOwner(String id) {

        Tran t = tranDao.getTranByIdForOwner(id);

        return t;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> thList = tranHistoryDao.getHistoryListByTranId(tranId);


        return thList;
    }

    @Override
    public void updateStage(Tran t) {

        //更新交易阶段
        tranDao.updateStage(t);

        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setTranId(t.getId());
        th.setMoney(t.getMoney());
        th.setStage(t.getStage());
        th.setExpectedDate(t.getExpectedDate());
        tranHistoryDao.saveTranHistory(th);

    }

    @Override
    public Map<String, Object> getChart() {

        //取得nameList
        List<String> nameList = tranDao.getNameList();

        //取得min
        int min = tranDao.getMinStageCount();

        //取得max
        int max = tranDao.getMaxStageCount();

        //取得dataList
        List<Map<String,Object>> dataList = tranDao.getDataList();

        //将以上业务信息保存到map中返回
        Map<String,Object> map = new HashMap<>();

        map.put("nameList", nameList);
        map.put("min", min);
        map.put("max", max);
        map.put("dataList", dataList);

        return map;
    }
}
