package me.jordy.rest.sample.configs;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 리소스 서버란?
 * OAuth 서버와 연동이 외서 운영 되는 서버.
 * 외부 요청이 리소스에 접근으 할 떄 인증이 필요하다면 OAuth 쪽에 요청을 보내서 토큰을 받고,
 * 토큰이 유효한지 확인하느 역할.
 *
 * 인증 정보에 토큰이 있는지 확인을 하고 없으면 리소스 접근을 제한.
 *
 * 그래서 서버 아키테처에서 리소스 서버는 이벤트 도메인과 함께 있는 것이 맞고
 * OAuth 서버는 별도로 구성되는 것이 맞음.
 *
 * 작은 서비스에서는 같이 있어도 노상관.
 */

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * 리소스 ID와 같은 리소스 서버 관련 속성을 추가하십시오.
     * 기본값은 많은 응용 프로그램에 대해 작동해야 하지만 최소한 리소스 ID를 변경해야 할 수 있다.
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("event");
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .anonymous()
                .and()
            .authorizeRequests()
                .mvcMatchers(HttpMethod.GET,"/api/**")
                    .permitAll()
                .anyRequest()
                    .authenticated()
                .and()
            .exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler())
        /**
         * OAuth2AccessDeniedHandler.class 란?
         * 인증이 실패하고 발신자가 특정 콘텐츠 유형 응답을 요청한 경우,
         * 이 진입점은 표준 403 상태와 함께 해당 콘텐츠 유형 응답을 전송할 수 있다.
         *
         * 일반적인 방법으로 스프링 보안 구성에 {@link AccessDeniedHandler }(으)로 추가하십시오.
         */



        ;
    }
}
