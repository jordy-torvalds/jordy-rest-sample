package me.jordy.rest.sample.configs;

import jdk.nashorn.internal.parser.Token;
import me.jordy.rest.sample.accounts.Account;
import me.jordy.rest.sample.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
/**
 * 아래 코드를 클래스에 부여하는 순간 스프링부트가 설정해주는 스프링 시큐리티 설정은 적용되지 않음.
 * @EnableWebSecurity 부여
 * extends WebSecurityConfigureAdapter
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public TokenStore tokenStore (){
        return new InMemoryTokenStore();
    }

    /**
     * 다른 AuthorizationServer나 ResourceServer가 참조할 수 있도록
     * Bean 으로 등록!
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * AuthenticationManager 를 어떻게 만들지에 대해 정의한 메소드
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder);
    }

    /**
     * SecurityFilter의 적용 유무를 설정
     * HttpSecurity를 적용하기 전에 WebSecurity에서 적용할지 말지를 정의
     * 필터 역할.
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/docs/event.html");

        /* 스프링부트에서 제공해주는 기본 리소스 디렉토리를 모두 가져와서 Spring Security 가 적용되지 않도록 함.*/
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * WebSecurity 다음이 HttpSecurity .
     * HttpSecurity 로 왔다는 것은 스프링 시큐리티 안으로 온 것.
     * WebSecurity의 필터에 걸러지지 않았기 때문에 여기서 필터체인을 타게 되어 있음.
     *
     * 필터(웹) 자체에서 걸러주는 편이, 불 필요한 필터 체인 통과를 최소화 함으로써
     * 서버의 불필요한 부화를 최소화 할 수 있음.
     * 결론적으로, 정적인 리소스는 WebSecurity 로 처리하는 것이 나음
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /* 정적인 리소스를 HttpSecurity로 처리 했을 때의 코드. 쓰지 않으므로 주석 처리.*/
//        http.authorizeRequests()
//                .mvcMatchers("/docs/event.html").anonymous()
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).anonymous()
//        ;
    }
}
