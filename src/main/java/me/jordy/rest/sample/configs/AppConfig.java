package me.jordy.rest.sample.configs;

import me.jordy.rest.sample.accounts.Account;
import me.jordy.rest.sample.accounts.AccountRole;
import me.jordy.rest.sample.accounts.AccountService;
import net.bytebuddy.asm.Advice;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;

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

    //TODO 테스트용 계정 추가
    @Bean
    public ApplicationRunner applicationRunner () {
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account account = Account.builder()
                                    .email("jordy@admin.com")
                                    .password("1234")
                                    .roles(new HashSet<AccountRole>(Arrays.asList(AccountRole.ADMIN, AccountRole.USER)))
                                    .build()
                ;
                accountService.saveAccount(account);
            }
        };
    }

}
