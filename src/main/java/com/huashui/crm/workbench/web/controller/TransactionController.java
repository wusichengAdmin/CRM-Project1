package com.huashui.crm.workbench.web.controller;

import com.huashui.crm.settings.domain.User;
import com.huashui.crm.settings.service.UserService;
import com.huashui.crm.utils.DateTimeUtil;
import com.huashui.crm.utils.HandleFlag;
import com.huashui.crm.utils.UUIDUtil;
import com.huashui.crm.workbench.domain.Activity;
import com.huashui.crm.workbench.domain.Contacts;
import com.huashui.crm.workbench.domain.Tran;
import com.huashui.crm.workbench.domain.TranHistory;
import com.huashui.crm.workbench.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/2/18
 */

@Controller
@RequestMapping("workbench/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ContactsService contactsService;

    @Autowired
    private TranService tranService;

    @Autowired
    private TranHistoryService tranHistoryService;


    //跳转到交易主页面
    @RequestMapping("/toTransactionIndex.do")
    public String toTransactionIndex(){

        return "/workbench/transaction/index";

    }
    //查询用户列表，交给创建交易的页面
    @RequestMapping("/toTranSave.do")
    public ModelAndView toTranSave(){

        List<User> uList = userService.getUserList();

        ModelAndView mv = new ModelAndView();

        mv.addObject("uList",uList);
        mv.setViewName("/workbench/transaction/save");

        return mv;
    }

    //
    @RequestMapping("/getActivityListByActivityName.do")
    @ResponseBody
    public List<Activity> getActivityListByActivityName(String searchName){

        List<Activity> aList = activityService.getActivityListByActivityName(searchName);

        return aList;

    }

    @RequestMapping("/getActivityListByContactsName.do")
    @ResponseBody
    public List<Contacts> getActivityListByContactsName(String searchContacts){

        List<Contacts> cList = contactsService.getActivityListByContactsName(searchContacts);

        return cList;


    }

    @RequestMapping("/getCustomerNamesByName.do")
    @ResponseBody
    public List<String> getCustomerNamesByName(String name){

        List<String> sList = customerService.getCustomerNameByName(name);

        return sList;
    }

    @RequestMapping("/getPossibilityByStage.do")
    @ResponseBody
    public Map<String,String> getPossibilityByStage(String stage , HttpServletRequest request){

        //取得阶段和可能性之间的对应关系
        ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");

        //取得可能性
        String possibility = pMap.get(stage);

        //{possibility:?}
        Map<String,String> map = new HashMap<>();

        map.put("possibility",possibility);

        return map;

    }

    @RequestMapping("/saveTran.do")
    public String saveTran(Tran t, String customerName, HttpSession session){

        String createBy = ((User)session.getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();

        t.setId(UUIDUtil.getUUID());
        t.setCreateTime(createTime);
        t.setCreateBy(createBy);


        tranService.saveTran(t,customerName);

        return "redirect:/workbench/transaction/toTransactionIndex.do";

    }


    @RequestMapping("/toTranDetail.do")
    public ModelAndView toTranDetail(String id,HttpServletRequest request){

        Tran t = tranService.getTranByIdForOwner(id);

        String stage = t.getStage();
        ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");

        String possibility = pMap.get(stage);

        t.setPossibility(possibility);

        ModelAndView mv = new ModelAndView();

        mv.addObject("t", t);
        mv.setViewName("/workbench/transaction/detail");

        return mv;

    }

    @RequestMapping("/getHistoryListByTranId.do")
    @ResponseBody
    public List<TranHistory> getHistoryListByTranId(String tranId,HttpServletRequest request){

        List<TranHistory> thList = tranService.getHistoryListByTranId(tranId);

        ServletContext application = request.getServletContext();

        Map<String,String> map = (Map<String, String>) application.getAttribute("pMap");

        for (TranHistory tranHistory : thList) {

            String stage = tranHistory.getStage();

            String possibility = map.get(stage);

            tranHistory.setPossibility(possibility);
        }


        return thList;
    }


    @RequestMapping("/updateStage.do")
    @ResponseBody
    public Map<String,Object> updateStage(Tran t,HttpServletRequest request){

        //对t对象封装修改人和修改时间
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();

        t.setEditBy(editBy);
        t.setEditTime(editTime);

        tranService.updateStage(t);

        ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");

        t.setPossibility(pMap.get(t.getStage()));

        return HandleFlag.successObj("t", t);
    }

}
