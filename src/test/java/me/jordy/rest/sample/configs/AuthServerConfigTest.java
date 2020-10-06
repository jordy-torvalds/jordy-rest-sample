package me.jordy.rest.sample.configs;

import me.jordy.rest.sample.accounts.AccountService;
import me.jordy.rest.sample.common.AppProperties;
import me.jordy.rest.sample.common.BaseTest;
import me.jordy.rest.sample.common.TestDescription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 일종의 컨트롤러 테스트 이므로 BaseControllerTest를 상속 받음.
 */
public class AuthServerConfigTest extends BaseTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AppProperties appProperties;

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception{

        String grant_type = "password";

        mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(),appProperties.getClientSecret()))
                .param("grant_type", grant_type)
                .param("username", appProperties.getUserUsername())
                .param("password", appProperties.getUserPassword())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("access_token").exists())
        ;
    }

}