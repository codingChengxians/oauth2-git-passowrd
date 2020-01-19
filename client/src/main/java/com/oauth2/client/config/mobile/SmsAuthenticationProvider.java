package com.oauth2.client.config.mobile;

import com.oauth2.client.config.User;
import com.oauth2.client.config.mobile.service.SmsDetailServiceImpl;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

public class SmsAuthenticationProvider implements AuthenticationProvider {


    private SmsDetailServiceImpl smsDetailService;


    public void setSmsDetailService(SmsDetailServiceImpl smsDetailService) {
        this.smsDetailService = smsDetailService;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        User user = smsDetailService.loadUserByUsername(authenticationToken.getPrincipal().toString());
        if (user == null) {
            throw new InternalAuthenticationServiceException("无效认证！");
        }
        SmsAuthenticationToken smsAuthenticationToken = new SmsAuthenticationToken(user, user.getAuthorities());
        smsAuthenticationToken.setDetails(authenticationToken.getDetails());

        return smsAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
