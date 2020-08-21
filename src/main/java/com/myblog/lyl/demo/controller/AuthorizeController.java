package com.myblog.lyl.demo.controller;

import com.myblog.lyl.demo.dto.AccessTokenDTO;
import com.myblog.lyl.demo.dto.GithubUser;
import com.myblog.lyl.demo.mapper.UserMapper;
import com.myblog.lyl.demo.model.User;
import com.myblog.lyl.demo.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @program: demo
 * @Date: 2020/8/20 14:05
 * @Author: Yuling Li
 * @Description:
 */
@Controller
public class AuthorizeController {
    final GithubProvider githubProvider;
    @Autowired
    public AuthorizeController(GithubProvider githubProvider) {
        this.githubProvider = githubProvider;
    }
    @Autowired
    private UserMapper userMapper;
    @Value("${github.client.id}")
    private String githubId;
    @Value("${github.client.secret}")
    private String githubSecret;
    @Value("${github.redirect.uri}")
    private String githubRedirectUri;
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request, HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(githubId);
        accessTokenDTO.setClient_secret(githubSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(githubRedirectUri);
        accessTokenDTO.setState(state);



        String access_token = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(access_token);
        if (githubUser != null) {
            //登录成功，写cookies和session
            request.getSession().setAttribute("user",githubUser);
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            response.addCookie(new Cookie("token", token));
            return "redirect:/";
        } else {
            //登录失败，重新登录
            return "redirect:/";
        }

    }
}
