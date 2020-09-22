package me.jordy.rest.sample.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.jordy.rest.sample.common.CustomMediaTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/* Junit 4 를 기준으로 작성된 테스트 */
@RunWith(SpringRunner.class)
@WebMvcTest
public class EventsControllerTests {

    /**
     * - 웹 서버를 띄우지 않고도 스프링 MVC (DispatcherServlet) 가 요청을 처리하는 과정을 확인할 수 있어
     *   컨트롤러 테스트용으로 자주 쓰임.
     * - 단위테스트라고 보기는 힘듬.(너무 많은 라이브러리가 사용됨
     */
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createEvent() throws Exception {
        Event event = Event.builder()
                .name("죠르디 스프링 특강")
                .description("죠르디가 진행하는 스프링 특강")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,9,23,12,23,45))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,9,23,14,23,45))
                .beginEventDateTime(LocalDateTime.of(2020,10,23,12,23,45))
                .endEventDateTime(LocalDateTime.of(2020,10,23,14,23,45))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(20)
                .location("서울 스타듀밸리")
                .build();

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))

                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists());
    }
}
