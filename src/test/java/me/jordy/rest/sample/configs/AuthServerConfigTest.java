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