package me.jordy.rest.sample.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapeer () {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // prefix 로 인코딩 방식을 표시해주고, 이에 따라 적절한 패스워드 인코더를 사용!
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
