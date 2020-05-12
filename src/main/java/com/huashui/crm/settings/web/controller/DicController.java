package com.huashui.crm.settings.web.controller;

import com.huashui.crm.settings.domain.DicType;
import com.huashui.crm.settings.domain.DicValue;
import com.huashui.crm.settings.service.DicService;
import com.huashui.crm.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/1/12
 */
@Controller
@RequestMapping("/settings/dictionary")
public class DicController {

    @Autowired
    private DicService dicService;

    //跳转到数据字典表页面
    @RequestMapping("/toDicIndex.do")
    public String toDicIndex(){
        return "/settings/dictionary/index";
    }


    //查询字典类型，展示到页面中，并且跳转到字典类型页面
    @RequestMapping("/type/toTypeIndex.do")
    public ModelAndView toTypeIndex(){

        List<DicType> dtList = dicService.getTypeList();
        ModelAndView mv = new ModelAndView();
        mv.addObject("dtList",dtList);
        mv.setViewName("/settings/dictionary/type/index");

        return mv;
    }

    @RequestMapping("/type/toTypeSave.do")
    public String toTypeSave(){

        return "/settings/dictionary/type/save";

    }


    //验证code字段是否重复
    @RequestMapping("/type/checkCode.do")
    @ResponseBody
    public Map<String,Object> checkCode(String code){
        boolean flag = dicService.checkCode(code);

        Map<String,Object> map = new HashMap<>();

        map.put("success", flag);

        return map;
    }

    //根据提交的表单添加字典类型
    @RequestMapping("/type/saveType.do")
    public String saveType(DicType dt){
        dicService.saveType(dt);
        //执行完添加，修改，删除操作后，统一回到列表页index.jsp

        //回到列表页的方式是使用重定向

        return "redirect:/settings/dictionary/type/toTypeIndex.do";
    }

    //根据code查询某一条字典类型，然后跳转到字典类型修改页
    @RequestMapping("/type/toTypeUpdate.do")
    public ModelAndView toTypeUpdate(String code){
        DicType dt = dicService.getTypeByCode(code);
        ModelAndView mv = new ModelAndView();
        mv.addObject("dt", dt);
        mv.setViewName("/settings/dictionary/type/edit");
        return mv;

    }

    //根据提交的表单修改字典类型
    @RequestMapping("/type/updateType.do")
    public String updateType(DicType dt){
        dicService.updataType(dt);
        return "redirect:/settings/dictionary/type/toTypeIndex.do";
    }

    //根据code删除字典类型
    @RequestMapping("/type/deleteType.do")
    public String deleteType(String[] code){
        dicService.deleteType(code);
        return "redirect:/settings/dictionary/type/toTypeIndex.do";
    }

    //查询并展示字典值，然后跳转到字典值主页面
    @RequestMapping("/value/toValueIndex.do")
    public ModelAndView toValueIndex(){
        List<DicValue> dvlist = dicService.getValueList();
        ModelAndView mv = new ModelAndView();
        mv.addObject("dvList", dvlist);
        mv.setViewName("/settings/dictionary/value/index");
        return mv;
    }

    //获取字典类型，然后跳转到创建字典值页面
    @RequestMapping("/value/toValueSave.do")
    public ModelAndView toValueSave(){
        List<DicType> dtList = dicService.getTypeList();
        ModelAndView mv = new ModelAndView();
        mv.addObject("dtList", dtList);
        mv.setViewName("/settings/dictionary/value/save");
        return mv;
    }

    //根据提交的表单创建字典值，然后返回字典值主页面
    @RequestMapping("/value/saveValue.do")
    public String saveValue(DicValue dv){

        dv.setId(UUIDUtil.getUUID());

        dicService.saveValue(dv);

        return "redirect:/settings/dictionary/value/toValueIndex.do";
    }

    //根据id查询到某条字典值，然后跳转到修改页
    @RequestMapping("/value/toValueUpdate.do")
    public ModelAndView toValueUpdate(String id){
        DicValue dv = dicService.getValueById(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("dv", dv);
        mv.setViewName("/settings/dictionary/value/edit");
        return mv;
    }

    //修改字典值
    @RequestMapping("/value/updateValue.do")
    public String updateValue(DicValue dv){
        dicService.updateValue(dv);
        return "redirect:/settings/dictionary/value/toValueIndex.do";
    }

    //删除字典值
    @RequestMapping("/value/deleteValue.do")
    public String deleteValue(String[] id){
        dicService.deleteValue(id);
        return "redirect:/settings/dictionary/value/toValueIndex.do";
    }
}

