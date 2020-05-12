package com.huashui.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 华水吴彦祖
 * 2020/1/11
 */
@Controller
public class workbenchController {

    @RequestMapping("/workbench/toWorkbenchIndex.do")
    public String toWorkbenchIndex(){

        return "/workbench/index";
    }

    @RequestMapping("/workbench/main/toMainIndex.do")
    public String toMainIndex(){
        return "/workbench/main/index";
    }



}
