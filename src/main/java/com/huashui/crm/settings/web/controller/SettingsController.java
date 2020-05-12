package com.huashui.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 华水吴彦祖
 * 2020/1/12
 */

@Controller
public class SettingsController {

    @RequestMapping("settings/toSettingsIndex.do")
    public String toSettingsIndex(){
        return "/settings/index";
    }
}
