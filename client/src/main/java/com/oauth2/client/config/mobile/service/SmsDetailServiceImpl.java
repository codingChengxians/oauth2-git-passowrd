package com.oauth2.client.config.mobile.service;

import com.oauth2.client.config.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SmsDetailServiceImpl {

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = new User(username);

        return user;
    }
}
