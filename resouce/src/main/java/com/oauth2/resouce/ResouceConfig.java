package com.oauth2.resouce;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)//EnableGlobalMethodSecurity开户方法级别的保护
public class ResouceConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/order/*").permitAll().and().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll().anyRequest().authenticated();

//        http.authorizeRequests()//去掉获取token会被拦截
//                .antMatchers("/auth/**").permitAll()//开放的资源不用授权
//                .antMatchers("/api/**");
//        http
//                .authorizeRequests()
//                .anyRequest()
//                .permitAll();
        http.csrf().disable();

    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("resource_password_id")
                .tokenServices(tokenServices());
        super.configure(resources);

    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(jwtTokenStore());
        return defaultTokenServices;
    }

    /**
     * @Description OAuth2 token持久化接口，jwt不会做持久化处理
     * @Date 2019/7/15 18:12
     * @Version 1.0
     */
    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(accessTokenConverter());//toke加入key转换就是增强
    }

    /**
     * @Description 自定义token令牌增强器
     * @Date 2019/7/11 16:22
     * @Version 1.0
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey("oauth2-key");
        return accessTokenConverter;
    }

}
