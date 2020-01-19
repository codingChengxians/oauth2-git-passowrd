package com.oauth2.client.config.mobile;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter {


    @Autowired
    StringRedisTemplate redisTemplate;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (StrUtil.equals("/sms/login",request.getRequestURI()) && request.getMethod().equals("POST")) {
            validation(request);
        }
        filterChain.doFilter(request, response);
    }

    private boolean validation(HttpServletRequest request) {
        String code = request.getParameter("code");
        String mobile = request.getParameter("mobile");
        if (mobile.trim().length() < 1) {
            System.out.println("手机不正确！");
            return true;
        }
        String smsCode = redisTemplate.opsForValue().get(mobile);
        if(smsCode ==null){
            //请获取验证码
        }
        if (!code.equals(smsCode)) {
            System.out.println("验证码错误！");
            return true;
        }
        System.out.println("验证成功！，拦截器链继续往下走！");
        return false;
    }
}
