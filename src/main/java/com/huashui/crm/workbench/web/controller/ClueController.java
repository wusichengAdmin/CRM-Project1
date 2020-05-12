package com.huashui.crm.workbench.web.controller;

import com.huashui.crm.settings.domain.User;
import com.huashui.crm.settings.service.UserService;
import com.huashui.crm.utils.DateTimeUtil;
import com.huashui.crm.utils.HandleFlag;
import com.huashui.crm.utils.UUIDUtil;
import com.huashui.crm.vo.PaginationVo;
import com.huashui.crm.workbench.domain.Activity;
import com.huashui.crm.workbench.domain.Clue;
import com.huashui.crm.workbench.domain.Tran;
import com.huashui.crm.workbench.service.ActivityService;
import com.huashui.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/2/12
 */
@Controller
@RequestMapping("workbench/clue")
public class ClueController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClueService clueService;

    @Autowired
    private ActivityService activityService;

    //跳转到线索主页面
    @RequestMapping("/toClueIndex.do")
    public String toClueIndex() {

        return "/workbench/clue/index";
    }

    //获取用户列表，存入前端的下拉框中
    @RequestMapping("/getUserList.do")
    @ResponseBody
    public List<User> getUserList() {

        List<User> uList = userService.getUserList();

        return uList;
    }

    //创建线索
    @RequestMapping("/saveClue.do")
    @ResponseBody
    public Map<String, Object> saveClue(Clue c, HttpSession session) {

        String createBy = ((User) session.getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        c.setCreateBy(createBy);
        c.setCreateTime(createTime);
        c.setId(UUIDUtil.getUUID());
        clueService.saveClue(c);

        return HandleFlag.successTrue();

    }

   /* @RequestMapping("/pageList.do")
    public PaginationVo getClueList(String pageNoStr,String pageSizeStr,Clue clue){

        //将pageNoStr和pageSizeStr转为int类型
        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);

        //得到前面略过的记录数
        int skipCounut = (pageNo-1)*pageSize;
        Map<String,Object> paramMap = new HashMap<>();

        paramMap.put("clue", clue);
        paramMap.put("skipCount", skipCounut);
        paramMap.put("pageSize", pageSize);


        PaginationVo vo = clueService.pageList(paramMap);

        return vo;
    }*/


    //根据id查询线索，并且将线索信息传递到详情页面然后跳转到线索的详情页面
    @RequestMapping("/toClueDetail.do")
    @ResponseBody
    public ModelAndView toClueDetail(String id) {

        Clue c = clueService.getClueByIdForOwner(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("c", c);
        mv.setViewName("/workbench/clue/detail");

        return mv;
    }

    //根据线索id查询相关的活动列表
    @RequestMapping("/getActivityListByClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByClueId(String clueId) {

        List<Activity> aList = activityService.getActivityListByClueId(clueId);

        return aList;

    }

    //解除与线索关联的活动
    @RequestMapping("/unbund.do")
    @ResponseBody
    public Map<String, Object> deleteRelationById(String id) {

        clueService.deleteRelationById(id);

        return HandleFlag.successTrue();
    }

    //不根据线索id来获取活动的名字
    @RequestMapping("/getActivityNameAndNotByClueId.do")
    @ResponseBody
    public List<Activity> getActivityNameAndNotByClueId(String searchName, String clueId) {
        List<Activity> aList = activityService.getActivityNameAndNotByClueId(searchName, clueId);
        return aList;
    }

    //绑定活动
    @RequestMapping("/bind.do")
    @ResponseBody
    public Map<String, Object> bind(String clueId, String activityIds[]) {

        clueService.insertActivityBind(clueId, activityIds);

        return HandleFlag.successTrue();

    }

    //跳转到线索转换页面
    @RequestMapping("/toConvert.do")
    public ModelAndView toConvert(Clue c) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("c", c);
        mv.setViewName("/workbench/clue/convert");
        return mv;
    }
    //根据活动名字来查询活动列表
    @RequestMapping("/getActivityListByActivityName.do")
    @ResponseBody
    public List<Activity> getActivityListByActivityName(String searchName) {
        List<Activity> aList = activityService.getActivityListByActivityName(searchName);
        return aList;
    }
    //执行线索转换，并且在转换成功过后跳转到线索主页面
    @RequestMapping("/convert.do")
    public String convert(String clueId, Tran t, String flag, HttpSession session) {

        String createBy = ((User) session.getAttribute("user")).getName();

        if ("a".equals(flag)) {
            //进入if语句块说明要创建交易

            String createTime = DateTimeUtil.getSysTime();
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);
            t.setId(UUIDUtil.getUUID());

        }

        clueService.convert(clueId, t, createBy, flag);
        return "redirect:/workbench/clue/toClueIndex.do";

    }
}
