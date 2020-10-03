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

/**
 * 현 Auth Server에서 중요한 부분 중 하나가 인증을 하는 방법으로,
 * 총 6가지의 인증 방법 중
 * 이 프로젝트에서는 Password와 RefreshToken 으로 발급 받음
 *
 * 최초에 AuthToken을 발급 받을 때는 Password를 사용.
 * 다른 인증 방법과 차이는 Hop이 1이라는 점.
 * 카카오톡, 페이스북 같은 3rd Party 서비스를 통해 인증하는 것이 아닌 인증 정보를 가지고 있는
 * 서비스 제공자 측에서 인증을 할 때 사용.
 * 필요한 정보는 다음과 같음
 *   - Grant Type: password
 *   - Username: 사용자 명
 *   - Password: 비밀번호
 *   - ClientId : 애플리케이션에서 공식적이고 고유한 식별자로 사용되는 값. 16진수의 32자 이상의 값을 권고
 *   - ClientSecret(Optional):  client_secret은 응용 프로그램과 인증 서버에 알려진 비밀번호이다.
 *                              추측할 수 없을 정도로 임의로 생성되어야 한다.
 */

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
