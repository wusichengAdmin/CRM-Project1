package com.huashui.crm.workbench.service.impl;

import com.huashui.crm.utils.DateTimeUtil;
import com.huashui.crm.utils.UUIDUtil;
import com.huashui.crm.vo.PaginationVo;
import com.huashui.crm.workbench.dao.*;
import com.huashui.crm.workbench.domain.*;
import com.huashui.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 华水吴彦祖
 * 2020/2/12
 */

@Service
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueDao clueDao;

    @Autowired
    private ClueActivityRelationDao clueActivityRelationDao;

    @Autowired
    private ClueRemarkDao clueRemarkDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerRemarkDao customerRemarkDao;

    @Autowired
    private ContactsDao contactsDao;

    @Autowired
    private ContactsRemarkDao contactsRemarkDao;

    @Autowired
    private ContactsActivityRelationDao contactsActivityRelationDao;

    @Autowired
    private TranDao tranDao;

    @Autowired
    private TranHistoryDao tranHistoryDao;


    @Override
    public void saveClue(Clue c) {
        clueDao.saveClue(c);
    }

    @Override
    public Clue getClueByIdForOwner(String id) {

        Clue c = clueDao.getClueByIdForOwner(id);

        return c;
    }

    @Override
    public void deleteRelationById(String id) {

        clueActivityRelationDao.deleteRelationById(id);
    }

    @Override
    public void insertActivityBind(String clueId, String[] activityIds) {

        List<ClueActivityRelation> carList = new ArrayList<>();

        //遍历市场活动数组
        for (String activityId : activityIds) {

            //取得每一个市场活动的id
            //根据每一个遍历出来的市场活动的id，跟clueId做关联
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();

            clueActivityRelation.setId(UUIDUtil.getUUID());
            clueActivityRelation.setActivityId(activityId);
            clueActivityRelation.setClueId(clueId);

            carList.add(clueActivityRelation);


        }


        clueActivityRelationDao.insertActivityBind(carList);
    }

    @Override
    public void convert(String clueId, Tran t, String createBy, String flag) {

        //从这里开始进行线索转换业务

        /*
           1.根据线索的id，查询到线索的对象(马云这条记录的对象)
             将来我们会根据该对象，从中抽取相应的信息，生成客户以及联系人

         */

        Clue c = clueDao.getClueById(clueId);

        /*
          2.从以上的c对象中，取得相应公司名称
            根据该公司名称，到客户表中进行查询，看看到底有没有这个客户

            如果有这个客户，则根据该客户对象查询得到，将来我们要使用这个客户的信息

            如果没有这个客户，则新建一个新客户，将来我们要用这个客户的信息

         */

        String company = c.getCompany();

        //根据名称查询客户的时候，需要按照名称进行精准匹配，而不是模糊查询
        Customer cus = customerDao.getCustomerByName(company);

        if (cus == null) {

            //若进入到这个if语句块，则说明没有查询到此客户
            //需要新建一个客户
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setCreateBy(createBy);
            cus.setCreateTime(DateTimeUtil.getSysTime());
            cus.setWebsite(c.getWebsite());
            cus.setPhone(c.getPhone());
            cus.setAddress(c.getAddress());
            cus.setName(company);
            cus.setContactSummary(c.getContactSummary());
            cus.setDescription(c.getDescription());
            cus.setOwner(c.getOwner());
            cus.setNextContactTime(c.getNextContactTime());

            //添加新用户
            customerDao.saveCustomer(cus);
        }

        //完成第二步过后，接下来的步骤，需要使用到客户的id，我们就可以直接使用cus.getId();

        /*
          3.从c对象中抽取出相关的人的信息，生成一个联系人
            联系人不需要进行关于名字重复的判断，因为名字本来就会重复

         */
        Contacts con = new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setAddress(c.getAddress());
        con.setAppellation(c.getAppellation());
        con.setContactSummary(c.getContactSummary());
        con.setCreateBy(createBy);
        con.setCreateTime(DateTimeUtil.getSysTime());
        con.setCustomerId(cus.getId());
        con.setDescription(c.getDescription());
        con.setEmail(c.getEmail());
        con.setSource(c.getSource());
        con.setOwner(c.getOwner());
        con.setNextContactTime(c.getNextContactTime());
        con.setMphone(c.getMphone());
        con.setJob(c.getJob());
        con.setFullname(c.getFullname());

        //添加联系人
        contactsDao.saveContacts(con);

        /*
          完成了第三步之后
          接下来需要是要联系人的id
          可以直接使用con.getId();
         */

        /*
          4.查询得到线索相关联的备注信息列表，将所有的“备注信息”，转换到客户备注以及联系人的备注中去

         */
        List<ClueRemark> clueRemarksList = clueRemarkDao.getRemarkListByClueId(clueId);

        //遍历取得的备注信息

        //ArrayList<CustomerRemark> customerRemarkList = new ArrayList<>();

        //ArrayList<ContactsRemark> contactsRemarksList = new ArrayList<>();

        /*if(clueRemarksList.size() > 0){

            for (ClueRemark clueRemark : clueRemarksList) {

                //取得每一个需要转换的备注信息
                String noteContent = clueRemark.getNoteContent();
                //添加客户备注
                CustomerRemark customerRemark = new CustomerRemark();
                customerRemark.setCreateBy(createBy);
                customerRemark.setCreateTime(DateTimeUtil.getSysTime());
                customerRemark.setCustomerId(cus.getId());
                customerRemark.setEditFlag("0");
                customerRemark.setId(UUIDUtil.getUUID());
                customerRemark.setNoteContent(noteContent);

                customerRemarkList.add(customerRemark);

                //添加联系人备注
                ContactsRemark contactsRemark = new ContactsRemark();
                contactsRemark.setContactsId(con.getId());
                contactsRemark.setCreateBy(createBy);
                contactsRemark.setCreateTime(DateTimeUtil.getSysTime());
                contactsRemark.setEditFlag("0");
                contactsRemark.setId(UUIDUtil.getUUID());
                contactsRemark.setNoteContent(noteContent);

                contactsRemarksList.add(contactsRemark);
            }
            //处理添加客户备注业务
            customerRemarkDao.saveCustomerRemark(customerRemarkList);
            //处理添加联系人备注业务
            contactsRemarkDao.saveContactsRemarkDao(contactsRemarksList);

        }*/

        CustomerRemark customerRemark = new CustomerRemark();
        ArrayList<CustomerRemark> customerRemarkArrayList = new ArrayList<>();

        ContactsRemark contactsRemark = new ContactsRemark();
        ArrayList<ContactsRemark> contactsRemarkArrayList = new ArrayList<>();

        if (clueRemarksList.size() != 0) {//有remark才转化  行了 ok 刚刚什么问题
            for (ClueRemark clueRemark : clueRemarksList) {
                //备注信息中只有noteContent是有效的
                String noteContent = clueRemark.getNoteContent();


                customerRemark.setId(UUIDUtil.getUUID());
                customerRemark.setEditFlag("0");
                customerRemark.setNoteContent(noteContent);
                customerRemark.setCreateTime(DateTimeUtil.getSysTime());
                customerRemark.setEditBy(createBy);
                customerRemark.setCustomerId(cus.getId());
                customerRemarkArrayList.add(customerRemark);

                contactsRemark.setId(UUIDUtil.getUUID());
                contactsRemark.setContactsId(con.getId());
                contactsRemark.setCreateBy(createBy);
                contactsRemark.setCreateTime(DateTimeUtil.getSysTime());
                contactsRemark.setEditFlag("0");
                contactsRemark.setNoteContent(noteContent);
                contactsRemarkArrayList.add(contactsRemark);
            }

            contactsRemarkDao.saveContactsRemarkDao(contactsRemarkArrayList);
            customerRemarkDao.saveCustomerRemark(customerRemarkArrayList);


        }




        /*
          5.查询线索和市场活动的关联关系列表，将原来的线索和市场活动的关联关系，转换成为联系人和市场活动之间的关联关系

         */

        //查询得到线索和市场活动的关联关系列表
        List<ClueActivityRelation> clueActivityRelationsList = clueActivityRelationDao.getRelationListByClueId(clueId);

        //遍历得到的关系列表
        if (clueActivityRelationsList.size() != 0) {
            List<ContactsActivityRelation> contactsActivityRelationList = new ArrayList<>();

            for (ClueActivityRelation clueActivityRelation : clueActivityRelationsList) {

                //取得该线索关联的每一个市场活动id
                String activityId = clueActivityRelation.getActivityId();

                //让以上取得的原来与线索关联的市场活动id，与联系人id做新的关联
                ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
                contactsActivityRelation.setContactsId(con.getId());
                contactsActivityRelation.setActivityId(activityId);
                contactsActivityRelation.setId(UUIDUtil.getUUID());
                contactsActivityRelationList.add(contactsActivityRelation);
            }

            //添加联系人与市场活动的关联关系
            contactsActivityRelationDao.saveRelation(contactsActivityRelationList);
        }
        /*
          6.如果有创建交易的需求，则需要创建一条交易

         */

        if ("a".equals(flag)) {
            /*

                如果需要创建交易
                    t对象中已经封装了一些属性值
                    money,name,expectedDate,stage,activityId
                    id,createBy,createTime

                    其他的属性值，可以从c对象中转换过来

             */
            t.setSource(c.getSource());
            t.setOwner(c.getOwner());
            t.setNextContactTime(c.getNextContactTime());
            t.setDescription(c.getDescription());
            t.setCustomerId(cus.getId());
            t.setContactSummary(c.getContactSummary());
            t.setContactsId(con.getId());

            //添加交易
            tranDao.saveTran(t);

            /*
              7.交易的添加操作，需要搭配添加一条交易历史

             */

            TranHistory tranHistory = new TranHistory();
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(DateTimeUtil.getSysTime());
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setStage(t.getStage());
            tranHistory.setTranId(t.getId());

            //添加交易历史
            tranHistoryDao.saveTranHistory(tranHistory);


        }

            /*
              8.删除所有的线索关联的备注
             */

        clueRemarkDao.deleteRemarkByClueId(clueId);


            /*

            9.删除所有的线索与市场活动的关联关系

             */
        clueActivityRelationDao.deleteRelationByClueId(clueId);

           /*

            10.删除线索

            */
        clueDao.deleteById(clueId);
    }

    @Override
    public PaginationVo pageList(Map<String, Object> paramMap) {

        //取得dataList
        List<Activity> dataList = clueDao.getClueListByCondition(paramMap);

        //取得total
        int total = clueDao.getTotalByCondition(paramMap);

        //创建一个vo对象，将dataList和total封装起来
        PaginationVo<Activity> vo = new PaginationVo<>();
        vo.setDataList(dataList);
        vo.setTotal(total);
        return vo;
    }

}
