package com.myblog.lyl.demo.controller;

import com.myblog.lyl.demo.dto.AccessTokenDTO;
import com.myblog.lyl.demo.dto.GithubUser;
import com.myblog.lyl.demo.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @Value("${github.client.id}")
    private String githubId;
    @Value("${github.client.secret}")
    private String githubSecret;
    @Value("${github.redirect.uri}")
    private String githubRedirectUri;
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                            @RequestParam(name="state") String state){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(githubId);
        accessTokenDTO.setClient_secret(githubSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(githubRedirectUri);
        accessTokenDTO.setState(state);



        String access_token = githubProvider.getAccessToken(accessTokenDTO);
        System.out.println(access_token);
        GithubUser githubUser = githubProvider.getUser(access_token);
        System.out.println(githubUser.getBio()+githubUser.getId()+githubUser.getName());
        return "index";
    }
}
