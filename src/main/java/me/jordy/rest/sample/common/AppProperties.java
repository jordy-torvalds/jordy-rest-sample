package me.jordy.rest.sample.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;


/**
 * 설정 정보를 외부로 뺴고 싶을 때 사용.
 * pom.xml 에 관련 의존성의 추가가 필요
 * 빌드 후에는 properties 에서 자동완성도 지원.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix="my-app")
public class AppProperties {

    @NotEmpty
    private String adminUsername;

    @NotEmpty
    private String adminPassword;

    @NotEmpty
    private String userUsername;

    @NotEmpty
    private String userPassword;

    @NotEmpty
    private String clientId;

    @NotEmpty
    private String clientSecret;
}
