package me.jordy.rest.sample.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jordy.rest.sample.accounts.Account;
import me.jordy.rest.sample.accounts.AccountRole;
import me.jordy.rest.sample.accounts.AccountService;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.HashSet;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/* Junit 4 를 기준으로 작성된 테스트 */
@RunWith(SpringRunner.class)
/**
 * @WebMvcTest는 컨트롤러 레이어에 슬라이싱 테스트를 적용하는 어노태이션.
 * 그래서  Repository는 빈 등록이 되지 않음.
 * 만약 테스트 중 실제 DB에 CRUD 하는 것이 필요할 경우 아래와 같이 어노태이션 교체가 필요
 * - @SpringBootTest           <-- 통합테스트 시 사용되는 어노태이션. 기본 웹 환경 값이 Mock으로 되어 있습니다.
 * - @AutoConfigureMockMvc     <-- MockMvc를 자동설정 해주는 어노태이션
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class) /* 설정 클래스에 설정된 정보를 적용하기 위해 임의로 임포트. 그냥 해서는 적용 안 됨*/
@ActiveProfiles("test")
@Ignore
public class BaseControllerTest {

    /**
     * - 웹 서버를 띄우지 않고도 스프링 MVC (DispatcherServlet) 가 요청을 처리하는 과정을 확인할 수 있어
     *   컨트롤러 테스트용으로 자주 쓰임.
     * - 단위테스트라고 보기는 힘듬.(너무 많은 라이브러리가 사용됨
     */
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    protected AccountService accountService;
}
