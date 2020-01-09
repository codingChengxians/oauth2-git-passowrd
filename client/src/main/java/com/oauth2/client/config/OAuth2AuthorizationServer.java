package com.oauth2.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    /**
     * 注入AuthenticationManager ，密码模式用到
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()
                .checkTokenAccess("isAuthenticated()") //url:/oauth/check_token allow check token 获取check token
                .tokenKeyAccess("isAuthenticated()");// 获取密钥需要身份认证，使用单点登录时必须配置  判断token 账户登录
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
//                .withClient("auth_code")
//                // client secret
//                .secret(new BCryptPasswordEncoder().encode("secret"))
//                /**
//                 ----支持的认证授权类型----
//                 示例（授权码）：http://localhost:9001/oauth/authorize?client_id=auth_code&response_type=code&redirect_uri=http://localhost:9001/auth_user/get_auth_code
//                 示例（access_token）：http://localhost:9001/oauth/token?grant_type=authorization_code&code=kb04Ur&client_id=auth_code&client_secret=secret&redirect_uri=http://localhost:9001/auth_user/get_auth_code
//                 refresh_token示例：http://localhost:9001/oauth/token?client_id=auth_code&client_secret=secret&grant_type=refresh_token&refresh_token=xxxx
//                 授权码模式（authorization_code）
//                 --client_id：客户端ID，必选
//                 --response_type：必须为code，必选
//                 --redirect_uri：回掉url,必选
//                 */
//                .authorizedGrantTypes("authorization_code","refresh_token")
//                //回调uri，在authorization_code与implicit授权方式时，用以接收服务器的返回信息
//
//                .redirectUris("http://localhost:8081/client/account/redirect")
//                .autoApprove(false)
//
//                // 允许的授权范围
//                .scopes("insert","update","del", "select", "replace")
//
//                .withClient("app")
//                .secret(new BCryptPasswordEncoder().encode("secret"))
//                .accessTokenValiditySeconds(3600)
//                .refreshTokenValiditySeconds(864000)
////                .autoApprove(true) //自动授权配置
////                .redirectUris("http://localhost:8080/login") //单点登录时配置
//                //设置支持[密码模式、授权码模式、token刷新]
//                    .authorizedGrantTypes("authorization_code","client_credentials", "refresh_token") //密码，授权，刷新token
//        clients.inMemory()
                .withClient("3d21b21b1c4498b465df")
                // client secret
//                .secret(new BCryptPasswordEncoder().encode("secret"))
                .secret("78f4d9bfeb1f75b8d7e7dc882c2176cd65945a45")
                /**
                 ----支持的认证授权类型----
                 授权码模式（authorization_code）
                 --client_id：客户端ID，必选
                 --response_type：必须为code，必选
                 --redirect_uri：回掉url,必选
                 */
                .authorizedGrantTypes("authorization_code", "password", "refresh_token")
                //回调uri，在authorization_code与implicit授权方式时，用以接收服务器的返回信息
                .redirectUris("http://localhost:8080/oauth/redirect")
                // 允许的授权范围
                .scopes("all")
                .and()

                //--------------------------------------------------------------------两个模式分界线--------------------------------------------------------------------------------------------

                .withClient("client_password")
                //client secret
                .secret(new BCryptPasswordEncoder().encode("secret"))
                /**
                 ----密码模式---
                 自己有一套账号权限体系在认证服务器中对应,客户端认证的时候需要带上自己的用户名和密码
                 示例：http://localhost:9001/oauth/token?username=user&password=123&grant_type=password&client_id=client_password&client_secret=secret
                 refresh_token示例：http://localhost:9001/oauth/token?grant_type=refresh_token&refresh_token=xxx&client_id=client_password&client_secret=secret
                 --client_id：客户端ID，必选
                 --client_secret：客户端密码，必选
                 --grant_type：必须为password，必选
                 --username:用户名，必选
                 --password:密码，必选
                 */
                .authorizedGrantTypes("password", "refresh_token")
                //资源ID
                .resourceIds("resource_password_id")
                // 允许的授权范围
                .scopes("all")
                // 自动授权，无需人工手动点击 approve
//                .autoApprove(true)
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(864000);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //开启密码管理需要注入这个manager
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenStore(jwtTokenStore())//
                .accessTokenConverter(accessTokenConverter()) //token转换
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }

    /**
     * 设置token 由Jwt产生，不使用默认的透明令牌
     */
    @Bean
    public JwtTokenStore jwtTokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * 对Jwt签名时，增加一个密钥
     * JwtAccessTokenConverter：对Jwt来进行编码以及解码的类
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("oauth2-key"); //对称加密
        return converter;
    }


}
