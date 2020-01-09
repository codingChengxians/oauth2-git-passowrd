package com.oauth2.client.config.sms;

import com.oauth2.client.config.User;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CustomUserDetailsService {

    public UserDetails loadUserByPhoneAndPassword(String phone, String password) {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password)) {
            throw new InvalidGrantException("无效的手机号或短信验证码");
        }
        // 判断成功后返回用户细节
        return new User(phone);
    }

    public UserDetails loadUserByPhoneAndSmsCode(String phone, String smsCode) {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(smsCode)) {
            throw new InvalidGrantException("无效的手机号或短信验证码");
        }
        // 判断成功后返回用户细节
        return new User(phone);
    }

}