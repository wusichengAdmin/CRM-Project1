package com.huashui.crm.workbench.web.controller;

import com.huashui.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/2/20
 */
@Controller
@RequestMapping("/workbench/chart")
public class ChartController {

    @Autowired
    private TranService tranService;

    @RequestMapping("transaction/toCharTranIndex.do")
    public String toCharTranIndex(){

        return "/workbench/chart/transaction/index";

    }

    @RequestMapping("/transaction/getChart.do")
    @ResponseBody
    public Map<String,Object> getChart(){

        Map<String,Object> map = tranService.getChart();

        return map;

    }

}
