package com.huashui.crm.workbench.web.controller;


import com.huashui.crm.exception.AjaxRequestException;
import com.huashui.crm.settings.domain.User;
import com.huashui.crm.settings.service.UserService;
import com.huashui.crm.utils.DateTimeUtil;
import com.huashui.crm.utils.HandleFlag;
import com.huashui.crm.utils.UUIDUtil;
import com.huashui.crm.vo.PaginationVo;
import com.huashui.crm.workbench.domain.Activity;
import com.huashui.crm.workbench.domain.ActivityRemark;
import com.huashui.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/1/21
 */
@Controller
@RequestMapping("workbench/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    //跳转到市场活动页面
    @RequestMapping("/toActivityIndex.do")
    public String toActivityIndex(){
        return "/workbench/activity/index";
    }

    //查询用户列表，将用户名称添加到创建市场活动的下拉框中
    @RequestMapping("/getUserList.do")
    @ResponseBody
    public List<User> getUserList(){
        List<User> uList = userService.getUserList();
        return uList;
    }

    //添加市场活动
    @RequestMapping("/saveActivity.do")
    @ResponseBody
    public Map<String,Object> saveActivity(Activity a, HttpSession session) throws AjaxRequestException {
        //通过UUID主键的工具
        String id = UUIDUtil.getUUID();
        //取得创建人：当前登录的用户的名字
        String createBy = ((User)session.getAttribute("user")).getName();
        //获取创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();

        a.setId(id);
        a.setCreateBy(createBy);
        a.setCreateTime(createTime);

        try {
            activityService.saveActivity(a);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AjaxRequestException();
        }

        return HandleFlag.successTrue();
    }


    //分页查询操作
    //参数处理：部分参数使用实体类做封装
    @RequestMapping("/pageList.do")
    @ResponseBody
    public PaginationVo pageList(String pageNoStr,String pageSizeStr,Activity a){

        //将pageNoStr和pageSizeStr转为int类型

        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);

        //得到前面略过的记录数
        int skipCounut = (pageNo-1)*pageSize;


        Map<String,Object> paramMap = new HashMap<>();

        paramMap.put("activity", a);
        paramMap.put("skipCount", skipCounut);
        paramMap.put("pageSize", pageSize);


        PaginationVo vo = activityService.pageList3(paramMap);

        return vo;

    }

    //删除市场活动
    @RequestMapping("/deleteActivity.do")
    @ResponseBody
    public Map<String,Object> deleteActivity(String[] ids){

        activityService.deleteActivity(ids);

        return HandleFlag.successTrue();
    }

    //获取用户信息和活动信息，填充到修改表单中
    @RequestMapping("/getUserListAndActivityById.do")
    @ResponseBody
    public Map<String,Object> getUserListAndActivityById(String id){

        Map<String,Object> map = activityService.getUserListAndActivityById(id);

        return map;
    }

    //修改活动信息
    @RequestMapping("/updateActivity.do")
    @ResponseBody
    public Map<String,Object> updateActivity(Activity a,HttpSession session){

        String editBy = ((User)session.getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();

        a.setEditBy(editBy);
        a.setEditTime(editTime);

        activityService.updateActivity(a);

        return HandleFlag.successTrue();

    }

    //将活动信息全部导出到Excel表中
    @RequestMapping("/exportActivityAll.do")
    public void exportActivityAll(HttpServletResponse response){

        //查询得到全部市场活动信息列表
        List<Activity> aList = activityService.getActivityAll();

        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet sheet = workbook.createSheet();

        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("编号");

        cell = row.createCell(1);
        cell.setCellValue("所有者");

        cell = row.createCell(2);
        cell.setCellValue("市场活动名称");

        cell = row.createCell(3);
        cell.setCellValue("开始日期");

        cell = row.createCell(4);
        cell.setCellValue("结束日期");

        cell = row.createCell(5);
        cell.setCellValue("成本");

        cell = row.createCell(6);
        cell.setCellValue("描述");

        cell = row.createCell(7);
        cell.setCellValue("创建时间");

        cell = row.createCell(8);
        cell.setCellValue("创建人");

        cell = row.createCell(9);
        cell.setCellValue("修改时间");

        cell = row.createCell(10);
        cell.setCellValue("修改人");

        for (int i = 0; i < aList.size(); i++) {

            //取得每一个市场活动
            Activity a = aList.get(i);

            row = sheet.createRow(i+1);

            cell = row.createCell(0);
            cell.setCellValue(a.getId());

            cell = row.createCell(1);
            cell.setCellValue(a.getOwner());

            cell = row.createCell(2);
            cell.setCellValue(a.getName());

            cell = row.createCell(3);
            cell.setCellValue(a.getStartDate());

            cell = row.createCell(4);
            cell.setCellValue(a.getEndDate());

            cell = row.createCell(5);
            cell.setCellValue(a.getCost());

            cell = row.createCell(6);
            cell.setCellValue(a.getDescription());

            cell = row.createCell(7);
            cell.setCellValue(a.getCreateTime());

            cell = row.createCell(8);
            cell.setCellValue(a.getCreateBy());

            cell = row.createCell(9);
            cell.setCellValue(a.getEditTime());

            cell = row.createCell(10);
            cell.setCellValue(a.getEditBy());

        }

        //为客户浏览器提供下载框
        response.setContentType("octets/stream");
        response.setHeader("Content-Disposition","attachment;filename=Activity-"+DateTimeUtil.getSysTime()+".xls");
        OutputStream out = null;

        try {
            out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //选择导出市场活动
    @RequestMapping("/exportActivityXz.do")
    public void exportActivityXz(String[] ids,HttpServletResponse response){
        List<Activity> aList = activityService.getActivityListByIds(ids);

        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet sheet = workbook.createSheet();

        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("编号");

        cell = row.createCell(1);
        cell.setCellValue("所有者");

        cell = row.createCell(2);
        cell.setCellValue("市场活动名称");

        cell = row.createCell(3);
        cell.setCellValue("开始日期");

        cell = row.createCell(4);
        cell.setCellValue("结束日期");

        cell = row.createCell(5);
        cell.setCellValue("成本");

        cell = row.createCell(6);
        cell.setCellValue("描述");

        cell = row.createCell(7);
        cell.setCellValue("创建时间");

        cell = row.createCell(8);
        cell.setCellValue("创建人");

        cell = row.createCell(9);
        cell.setCellValue("修改时间");

        cell = row.createCell(10);
        cell.setCellValue("修改人");

        for (int i = 0; i < aList.size(); i++) {

            //取得每一个市场活动
            Activity a = aList.get(i);

            row = sheet.createRow(i+1);

            cell = row.createCell(0);
            cell.setCellValue(a.getId());

            cell = row.createCell(1);
            cell.setCellValue(a.getOwner());

            cell = row.createCell(2);
            cell.setCellValue(a.getName());

            cell = row.createCell(3);
            cell.setCellValue(a.getStartDate());

            cell = row.createCell(4);
            cell.setCellValue(a.getEndDate());

            cell = row.createCell(5);
            cell.setCellValue(a.getCost());

            cell = row.createCell(6);
            cell.setCellValue(a.getDescription());

            cell = row.createCell(7);
            cell.setCellValue(a.getCreateTime());

            cell = row.createCell(8);
            cell.setCellValue(a.getCreateBy());

            cell = row.createCell(9);
            cell.setCellValue(a.getEditTime());

            cell = row.createCell(10);
            cell.setCellValue(a.getEditBy());

        }

        //为客户浏览器提供下载框

        response.setContentType("octets/stream");
        response.setHeader("Content-Disposition","attachment;filename=Activity-"+DateTimeUtil.getSysTime()+".xls");

        OutputStream out = null;

        try {
            out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //导入市场活动
    @RequestMapping("/importActivity.do")
    @ResponseBody
    public Map<String,Object> importActivity(@RequestParam MultipartFile myFile, HttpServletRequest request) throws AjaxRequestException {

        //找到项目所在的路径，将文件上传到项目制定的路径下
        //上传文件，我们不找任何盘符，我们只找项目，这种路径也叫做虚拟路径
        ServletContext application = request.getServletContext();

        //找到指定的文件夹所在盘符的具体路径
        String path = application.getRealPath("/tmpDir");

        //为上传的文件取名，每一次都是不同的，使用日期工具来生成名字
        //xxx.xls
        String fileName = DateTimeUtil.getSysTimeForUpload()+".xls";

        String fileNamePath = path + "/" + fileName;

        //文件的上传操作
        /*

            如果不使用任何框架技术，我们仅仅只是使用web支持的上传的插件，上传操作是相当的麻烦的

            但是我们现在使用的是springmvc做的上传技术支持

            上传操作非常方便，一行代码搞定

         */

        try {

            //文件上传
            myFile.transferTo(new File(fileNamePath));

            //创建一个excel文件（之前的导出操作）
            //HSSFWorkbook workbook = new HSSFWorkbook();

            //读取一个excel文件
            /*
                所谓I/O（Input/Output）流，就是读/写流
                Input：做读操作
                Output：做写操作
                我们现在的目的，是为了读取刚刚上传的excel文件
                将文件中的信息解析出来
                所以做的是一次读操作
                使用Input
             */
            InputStream input = new FileInputStream(fileNamePath);

            /*

                以下的workbook对象，代表的就是我们刚刚上传的excel文件了

             */
            HSSFWorkbook workbook = new HSSFWorkbook(input);

            //将workbook中的信息全部解析出来，然后执行（批量）添加操作
            //通过指定的下标来取得需要解析的页
            //指定下标0，指的是取得第一页的信息
            HSSFSheet sheet = workbook.getSheetAt(0);

            List<Activity> aList = new ArrayList<>();

            /*

                sheet.getLastRowNum()
                    从方法的取名来看，表示的是取得的是最后一行的行号
                    但是实际上，取得的是最后一行的下标

                sheet.getLastRowNum()+1
                    所得到的才是真正的行号

             */

            //遍历所有行
            for (int i = 1; i < sheet.getLastRowNum()+1; i++) {

                //通过遍历，取得每一行
                HSSFRow row = sheet.getRow(i);

                //将解析出来的每一行中的信息保存到Activity对象中
                //一行一个对象

                Activity a = new Activity();

                for (int j = 0; j < row.getLastCellNum(); j++) {

                    /*

                    row.getLastCellNum()
                        从方法的名字来看，要取得的是最后一列的列号
                        实际上，取得的就是最后一列的列号

                 */

                    HSSFCell cell = row.getCell(j);

                    //获取每一个单元格中的值
                    String value = cell.getStringCellValue();

                    if(j==0){

                        a.setId(value);

                    }else if(j==1){

                        a.setOwner(value);

                    } else if (j == 2) {

                        a.setName(value);

                    } else if (j == 3) {

                        a.setStartDate(value);

                    } else if (j == 4) {

                        a.setEndDate(value);

                    } else if (j == 5) {

                        a.setCost(value);

                    } else if (j == 6) {

                        a.setDescription(value);

                    } else if (j == 7) {

                        a.setCreateTime(value);

                    } else if (j == 8) {

                        a.setCreateBy(value);

                    } else if (j == 9) {

                        a.setEditTime(value);

                    } else if (j == 10) {

                        a.setEditBy(value);

                    }

                }
                aList.add(a);
                //以上就是讲excel表中所有的信息解析出来，将信息封装成一个aList
                //接下来我们就要将aList中的信息批量添加到数据库表中
            }
            //调用业务层，执行批量添加的操作
            //同时需要返回信息
            int count = activityService.saveActivityList(aList);

            return HandleFlag.successObj("count", count);


        } catch (Exception e) {

            e.printStackTrace();

            /*Map<String,Object> map = new HashMap<>();
            map.put("success",false);
            return map;*/

            throw new AjaxRequestException();
        }
    }

    //查询市场活动详情并跳转到详情页面
    @RequestMapping("/toActivityDetail.do")
    public ModelAndView toActivityDetail(String id){

        Activity a = activityService.getActivityByIdForOwner(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("a", a);
        mv.setViewName("/workbench/activity/detail");

        return mv;

    }


    //根据id查询备注信息
    @RequestMapping("/getRemarkListById.do")
    @ResponseBody
    public List<Activity> getRemarkListById(String activityId){

        List<Activity> acList = activityService.getRemarkListById(activityId);

        return acList;

    }


    //删除活动备注
    @RequestMapping("/deleteRemarkById.do")
    @ResponseBody
    public Map<String,Object> deleteRemarkById(String id){

        activityService.deleteRemarkById(id);

        return HandleFlag.successTrue();

    }

    //添加备注信息
    @RequestMapping("/saveActivityRemark.do")
    @ResponseBody
    public Map<String,Object> saveActivityRemark(ActivityRemark ac,HttpSession session){

        ac.setId(UUIDUtil.getUUID());
        String createBy = ((User)session.getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();

        ac.setCreateBy(createBy);
        ac.setCreateTime(createTime);
        ac.setEditFlag("0");

        activityService.saveActivityRemark(ac);

        return HandleFlag.successObj("ar", ac);

    }

    //修改市场活动备注信息
    @RequestMapping("/updateActivityRemark.do")
    @ResponseBody
    public Map<String,Object> updateActivityRemark(ActivityRemark ac,HttpSession session){

        //获取当前登录人员，作为修改人
        String editBy = ((User)session.getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();

        ac.setEditFlag("1");
        ac.setEditBy(editBy);
        ac.setEditTime(editTime);

        activityService.updateActivityRemark(ac);

        return HandleFlag.successObj("ar", ac);
    }
}
