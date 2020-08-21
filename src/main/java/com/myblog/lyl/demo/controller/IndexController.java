package com.myblog.lyl.demo.controller;

import com.myblog.lyl.demo.mapper.UserMapper;
import com.myblog.lyl.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: demo
 * @Date: 2020/8/19 10:42
 * @Author: Yuling Li
 * @Description:
 */

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie:cookies) {
            if (cookie.getName().equals("token")) {
                String token = cookie.getName();
                User user = userMapper.findByToken(token);
                if(user!=null)
                    request.getSession().setAttribute("user", user);
                break;
            }
        }
        return "index";
    }

}
