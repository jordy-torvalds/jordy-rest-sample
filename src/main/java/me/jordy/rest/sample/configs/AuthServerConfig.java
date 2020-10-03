package me.jordy.rest.sample.configs;

import jdk.nashorn.internal.parser.Token;
import me.jordy.rest.sample.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccountService accountService;

    @Autowired
    TokenStore tokenStore;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("myApp") // 지원하는 클라이언트 아이디
                .authorizedGrantTypes("password", "refresh_token") // 지원하는 그랜트 타입
                .scopes("read","write","a","b","c") // 임의의 값
                .secret(passwordEncoder.encode("pass")) // 클라이언트 시크릿
                .accessTokenValiditySeconds(10 * 60)  // 액세스 토큰 유효시간
                .refreshTokenValiditySeconds(10 * 60 * 6); // 리프레쉬 토근 유효시간

        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(accountService)
                .tokenStore(tokenStore)
        ;
    }
}
