package me.jordy.rest.sample.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.jordy.rest.sample.common.CustomMediaTypes;
import me.jordy.rest.sample.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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


    /**
     *  @WebMvcTest 가 웹용 빈만 등록을 해주고, 리포지토리를 빈으로 등록 해주지 않음
     * 그래서 리포지토리의 모킹이 필요함.
     *
     *  EventRepository가 빈에 등록되어 있지 않으므로 모킹하여 save 호출시 이벤트가 반환 되도록 함
     *  통합 테스트 설정에 따라 해당 코드를 주석 처리
     */
//    @MockBean
//    EventRepository eventRepository;

    @Test
    @TestDescription("완전한 Event 데이터를 삽입했을 때 정상적으로 처리 되는지 확인하는 테스트")
    public void createEvent() throws Exception {
        Event event = Event.builder()
                .id(100)
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
                .free(true)
                .offline(true)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        /**
         *  EventRepository가 빈에 등록되어 있지 않으므로 모킹하여 save 호출시 이벤트가 반환 되도록 함
         *  통합 테스트 설정에 따라 해당 코드를 주석 처리
         */
        //Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))

                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE+";charset=UTF-8"))
                .andExpect(jsonPath("id").value(Matchers.not(100)))  /* 값 일치 확인 Matcher 사용*/
                .andExpect(jsonPath("free").value(Matchers.not(true)))  /* 값 일치 확인 Matcher 사용*/
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))  /* 값 일치 확인 Matcher 사용*/
        ;
    }

    /**
     * 알 수 없는 필드가 같이 들어왔을 때
     * Bad Request를 떨어트리는 테스트.
     * 핵심 소스는 프로퍼티즈의 spring.jackson.deserialization.fail-on-unknown-properties=true !
     * @throws Exception
     */
    @Test
    @TestDescription("알 수 없는 필드가 같이 들어왔을 때 Bad Request가 떨어지는 지 확인하는 테스트")
    public void createEvent_BadRequest() throws Exception {
        Event event = Event.builder()
                .id(100)
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
                .free(true)
                .offline(true)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))

                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }


    /**
     * 알 수 없는 필드가 같이 들어왔을 때
     * Bad Request를 떨어트리는 테스트.
     * 핵심 소스는 프로퍼티즈의 spring.jackson.deserialization.fail-on-unknown-properties=true !
     * @throws Exception
     */
    @Test
    @TestDescription("들어오는 Input Parameter에 유효하지 않는 데이터를 넣어 주었을 때 Bad Request가 나는지 확인하는 테스트")
    public void createEvent_BadRequest_EmptyInput() throws Exception {
        EventDto event = EventDto.builder()
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))

                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }


    /**
     * 알 수 없는 필드가 같이 들어왔을 때
     * Bad Request를 떨어트리는 테스트.
     * 핵심 소스는 프로퍼티즈의 spring.jackson.deserialization.fail-on-unknown-properties=true !
     * @throws Exception
     */
    @Test
    @TestDescription("들어오는 Input Parameter에 유효하지 않는 데이터를 넣어 주었을 때 Bad Request가 나는지 확인하는 테스트")
    public void createEvent_BadRequest_WrongInput() throws Exception {
        EventDto event = EventDto.builder()
                .name("죠르디 스프링 특강")
                .description("죠르디가 진행하는 스프링 특강")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,9,26,12,23,45))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,9,25,14,23,45))
                .beginEventDateTime(LocalDateTime.of(2020,10,24,12,23,45))
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
        ;
    }
}

// "[수정] Bad Request 에러 내용 반환 처리 및 이벤트 도메인 비즈니스 로직 추가 & 테스트 로직 추가"