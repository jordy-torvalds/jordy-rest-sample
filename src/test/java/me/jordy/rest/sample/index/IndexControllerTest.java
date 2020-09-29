package me.jordy.rest.sample.index;

import me.jordy.rest.sample.common.RestDocsConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class IndexControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void index() throws Exception {
        mockMvc.perform(get("/api"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("_links.events").exists())
        ;

    }
}
