package com.oauth2.client.config;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class HelloController {
    @RequestMapping("/api/error")
    public String error() {
        return "error";
    }


    @RequestMapping("/oauth/github")
    public void jumpGithub(HttpServletResponse response) {
        String s = "https://github.com/login/oauth/authorize?client_id=3d21b21b1c4498b465df&scope=user:email&redirect_uri=http://localhost:8080/oauth/redirect&response_type=code";
        try {
            response.sendRedirect(s);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("/oauth/redirect")
    public String redirect(@RequestParam(value = "code", required = true) String code) {

        Map request = new HashMap();//封装
        request.put("code", code);
        request.put("client_id", "3d21b21b1c4498b465df");
        request.put("client_secret", "78f4d9bfeb1f75b8d7e7dc882c2176cd65945a45");
        request.put("redirect_uri", "http://localhost:8080/oauth/redirect");
        System.out.println("code:" + code);
        if (code != null && code != "") {
            String response = HttpUtil.get("https://github.com/login/oauth/access_token", request);
            System.out.println(response);
            String access_token = response.split("&")[0];
            access_token = access_token.replace("access_token=","");
            System.out.println("token:" + access_token);
            String userInfo = HttpUtil.get("https://api.github.com/user?access_token=" + access_token);
            System.out.println(userInfo);
            return userInfo;

        }
        return null;
    }
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/oauth/sms/send")
    public void smsSend(@RequestParam("mobile")String mobile){
        redisTemplate.opsForValue().set(mobile,String.valueOf(new Random().nextInt(9999)));
    }
    @RequestMapping("/sms/login")
    public void getSmsCode(@RequestParam("mobile")String mobile){
        System.out.println(mobile + "手机登陆，发放token 手动定义~");
    }
}
