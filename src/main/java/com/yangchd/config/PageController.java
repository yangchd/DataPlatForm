package com.yangchd.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yangchd
 * @date 2017/7/21
 * spring boot 在这里配置所有的页面请求路径
 */

@Controller
public class PageController {
    @RequestMapping("/index")
    public String index(){
        return "/index";
    }
    @RequestMapping("/")
    public String index1(){
        return "/index";
    }
    @RequestMapping("/login")
    public String index2(){
        return "/index";
    }
}