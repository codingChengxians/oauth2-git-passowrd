package com.oauth2.client.config.sms;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.Map;

public class SmsTokenGranter extends AbstractTokenGranter {

    private CustomUserDetailsService userDetailsService;

    public SmsTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, CustomUserDetailsService userDetailsService) {
        super(tokenServices, clientDetailsService, requestFactory, "sms_mobile");
        this.userDetailsService = userDetailsService;
    }

    protected UserDetails getUserDetails(Map<String, String> parameters) {
        String mobile = parameters.get("mobile");
        String code = parameters.get("code");
        return userDetailsService.loadUserByPhoneAndSmsCode(mobile, code);
    }
}
