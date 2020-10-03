package me.jordy.rest.sample.configs;

import me.jordy.rest.sample.accounts.Account;
import me.jordy.rest.sample.accounts.AccountRole;
import me.jordy.rest.sample.accounts.AccountService;
import me.jordy.rest.sample.common.BaseControllerTest;
import me.jordy.rest.sample.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 일종의 컨트롤러 테스트 이므로 BaseControllerTest를 상속 받음.
 */
public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception{

        String email = "scappy-cook@kakao.com";
        String password = "1234";
        String grant_type = "password";
        Account account = Account.builder()
                .email(email)
                .password(password)
                .roles(new HashSet<AccountRole>(Arrays.asList(AccountRole.USER)))
                .build()
        ;
        accountService.saveAccount(account);


        String clientId = "myApp";
        String clientSecret = "pass";

        mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId,clientSecret))
                .param("grant_type", grant_type)
                .param("username", email)
                .param("password", password)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("access_token").exists())
        ;
    }

}