package me.jordy.rest.sample.events;


import me.jordy.rest.sample.accounts.Account;
import me.jordy.rest.sample.accounts.AccountAdapter;
import me.jordy.rest.sample.accounts.AccountRepository;
import me.jordy.rest.sample.accounts.AccountService;
import me.jordy.rest.sample.common.AppProperties;
import me.jordy.rest.sample.common.BaseControllerTest;
import me.jordy.rest.sample.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class  EventsControllerTests extends BaseControllerTest {

    // TODO 권한 여부에 따른 추가적인 링크 정보 처리 추가
    // TODO SNIPPET 처리

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AppProperties appProperties;

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
    @TestDescription("완전한 EventDto 데이터를 삽입했을 때 정상적으로 처리 되는지 확인하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
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

        /**
         *  EventRepository가 빈에 등록되어 있지 않으므로 모킹하여 save 호출시 이벤트가 반환 되도록 함
         *  통합 테스트 설정에 따라 해당 코드를 주석 처리
         */
        //Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
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
                /* .andDo(document("create-event") 만 있어도 본문(리소스) 코드의 스니펫을 생성해 줌. 헤더와 링크 정보 문서화를 위해 아래와 같은 처리 필요*/
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update a existing event"),
                                linkWithRel("profile").description("link to event`s profile docs")

                        ), requestHeaders (
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")

                        ), requestFields (
                                fieldWithPath("name").description("Name of new Event"),
                                fieldWithPath("description").description("description of new Event"),
                                fieldWithPath("beginEnrollmentDateTime").description("begin enrollment date time of new Event"),
                                fieldWithPath("closeEnrollmentDateTime").description("close enrollment date time of new Event"),
                                fieldWithPath("beginEventDateTime").description("begin event date time of new Event"),
                                fieldWithPath("endEventDateTime").description("end event date time of new Event"),
                                fieldWithPath("location").description("location of new Event"),
                                fieldWithPath("basePrice").description("base price of new Event"),
                                fieldWithPath("maxPrice").description("max price of new Event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment of new Event")

                        ), responseHeaders (
                                headerWithName(HttpHeaders.LOCATION).description("Address to read newly created events"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("response data`s data type ")

                        ), responseFields (

                                fieldWithPath("id").description("Identification of Event"),
                                fieldWithPath("name").description("Name of Event"),
                                fieldWithPath("description").description("description of Event"),
                                fieldWithPath("beginEnrollmentDateTime").description("begin enrollment date time of Event"),
                                fieldWithPath("closeEnrollmentDateTime").description("close enrollment date time of Event"),
                                fieldWithPath("beginEventDateTime").description("begin event date time of Event"),
                                fieldWithPath("endEventDateTime").description("end event date time of Event"),
                                fieldWithPath("location").description("location of Event"),
                                fieldWithPath("basePrice").description("base price of Event"),
                                fieldWithPath("maxPrice").description("max price of Event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment of Event"),
                                fieldWithPath("free").description("it tells if this event is free or not"),
                                fieldWithPath("offline").description("it tells if this event is offline meeting or not"),
                                fieldWithPath("eventStatus").description("status of new Event"),
                                fieldWithPath("owner.id").description("owner id of new Event"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query events"),
                                fieldWithPath("_links.update-event.href").description("link to update a existing event"),
                                fieldWithPath("_links.profile.href").description("link to event`s profile docs")
                                /**
                                 *  _links 정보는 위에서 선언해주긴 했으나, 이 또한 리스폰스에 포함되므로
                                 *  위와 같이 처리 해주거나 responseFields 에서 relaxedResponseFields 로 바꿔야 한다.
                                 *
                                 *  다만, relaxedResponseFields 로 할 경우 전체 응답 필드 중 일부가 비어 있어도 알 수가 없어
                                 *  문서화 데이터에 빠진 데이터가 생길 수가 있을 뿐만 아니라 테스트도 응답 필드에 한해
                                 *  불안정 해진다는 단점이 있다.
                                 */
                        )
                ));
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
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken())
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
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken())
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
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))

                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("content[0].objectName").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception{
        // Given
        IntStream.range(0,30).forEach( i -> {
            this.generateEvents(i);
        });

        // When
        this.mockMvc.perform(get("/api/events")
                    .param("page","1")
                    .param("size", "10")
                    .param("sort","name,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"))
        ;

    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEventsWithAuthentication() throws Exception{
        // Given
        IntStream.range(0,30).forEach( i -> {
            this.generateEvents(i);
        });

        // When
        this.mockMvc.perform(get("/api/events")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .param("page","1")
                .param("size", "10")
                .param("sort","name,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.create-event").exists())
                .andDo(document("query-events"))
        ;

    }

    @Test
    @TestDescription("기존 이벤트 하나 조회하기")
    public void getEvent() throws Exception {
        // Given
        Event event = this.generateEvents(100);

        // When & Then
        mockMvc.perform(get("/api/events/{id}",event.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-event"))
        ;
    }

    @Test
    @TestDescription("없는 이벤트 데이터 조회하기")
    public void getEvent404() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/events/{id}",0))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @TestDescription("정상적인 요청으로 이벤트 수정")
    public void updateEvent() throws Exception {
        //Given
        Event event = generateEvents(200);

        EventDto eventDto = modelMapper.map(event, EventDto.class);
        String eventName = "updated event";
        eventDto.setName(eventName);

        // When & Then
        mockMvc.perform(put("/api/events/{id}",event.getId())
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event"))
        ;
    }

    @Test
    @TestDescription("이벤트 수정 중 비어있는 데이터로 인해 바인딩 에러 발생")
    public void updateEventWithEmptyContent() throws Exception {
        //Given
        Event event = generateEvents(202);

        EventDto eventDto = new EventDto();

        // When & Then
        mockMvc.perform(put("/api/events/{id}", event.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("이벤트 수정 중 도메인 로직 에러 발생")
    public void updateEventWithDomainLoginError() throws Exception {
        //Given
        Event event = generateEvents(203);

        EventDto eventDto = modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(100000);
        eventDto.setMaxPrice(20000);

        // When & Then
        mockMvc.perform(put("/api/events/{id}",event.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("이벤트 수정을 충분하지 않은 권한으로 진행")
    public void updateEventWithNotEnoughAuth() throws Exception {
        //Given
        Event event = generateEvents(204);

        EventDto eventDto = modelMapper.map(event, EventDto.class);

        // When & Then
        mockMvc.perform(put("/api/events/{id}",0)
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }
    private Event generateEvents(int index) {
        Optional<Account> account = accountRepository.findByEmail(appProperties.getAdminUsername());
        Event event = Event.builder()
                .id(index)
                .name("죠르디 스프링 특강 #"+index)
                .description("죠르디가 진행하는 스프링 특강")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,9,25,12,23,45))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,9,26,14,23,45))
                .beginEventDateTime(LocalDateTime.of(2020,10,27,12,23,45))
                .endEventDateTime(LocalDateTime.of(2020,10,28,14,23,45))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(20)
                .location("서울 스타듀밸리")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .owner(account.get())
                .build()
                ;
        event = eventRepository.save(event);
        System.out.println(event+"############");
        return event;
    }

    private String getBearerToken() throws Exception {
        return "Bearer" + getAuthToken();
    }

    private String getAuthToken() throws Exception {

        String grant_type = "password";

        ResultActions result = mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(appProperties.getClientId(),appProperties.getClientSecret()))
                    .param("grant_type", grant_type)
                    .param("username", appProperties.getAdminUsername())
                    .param("password", appProperties.getAdminPassword())
                )
            .andDo(print())
        ;
        String resultString = result.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        return parser.parseMap(resultString).get("access_token").toString();
    }
}