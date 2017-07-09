package org.springboot.integration.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.annotations.ApiIgnore;

import  org.springframework.ui.Model;

/**
 * Created by thinkpad on 2017/5/13.
 * 此Controller 根据请求路径进行，跳转控制
 * 默认将会跳转到resource/templates/目录下
 * 而是寻找名字为hello的模板进行渲染，我们使用Thymeleaf模板引擎进行模板渲染，需要引入依赖：
 */

@ApiIgnore
@Controller
public class PageController {


    @RequestMapping(value="/hello/{name}",method = RequestMethod.GET)
    public String hello(@PathVariable("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }
    
    @RequestMapping(value={"/login","/"},method = RequestMethod.GET)
    public String login(){
    	return "login";
    }
    
    @RequestMapping(value="/user-list",method = RequestMethod.GET)
    public String userList(HttpServletRequest request) {
    	//request.getSession().invalidate();  模拟会话超时
        return "user/user-list";
    }

}
