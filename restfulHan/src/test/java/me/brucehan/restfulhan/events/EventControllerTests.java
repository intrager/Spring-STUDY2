package me.brucehan.restfulhan.events;

import me.brucehan.restfulhan.accounts.Account;
import me.brucehan.restfulhan.accounts.AccountRepository;
import me.brucehan.restfulhan.accounts.AccountRole;
import me.brucehan.restfulhan.accounts.AccountService;
import me.brucehan.restfulhan.common.AppProperties;
import me.brucehan.restfulhan.common.BaseControllerTest;
import me.brucehan.restfulhan.common.TestDescription;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Set;
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

public class EventControllerTests extends BaseControllerTest {

    @Autowired EventRepository eventRepository;
    @Autowired AccountService accountService;
    @Autowired AccountRepository accountRepository;
    @Autowired AppProperties appProperties;

    @Before
    public void setUp() {
        this.eventRepository.deleteAll();
        this.accountRepository.deleteAll();
    }

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 11, 4, 11, 47))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 11, 5, 11, 50))
                .beginEventDateTime(LocalDateTime.of(2022, 11, 6, 9, 18))
                .endEventDateTime(LocalDateTime.of(2022, 11, 7, 4, 23))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("hong-dae")
                .build();
//        // eventRepository.save(event)가 호출되면 event를 리턴하라
//        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                    .contentType(MediaType.APPLICATION_JSON_VALUE) // APPLICATION_JSON_UTF8
                    .accept(MediaTypes.HAL_JSON) // 이 스펙이 준하는 응답을 보내겠다
                    .content(objectMapper.writeValueAsString(event))) // bean으로 등록 안 했지만 있는 것처럼 쓸 수 있음
                .andDo(print()) // 실제 콘솔에서 어떤 요청과 응답을 받았는지 확인 가능
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists()) // "id"가 있는지 테스트
                .andExpect(header().exists(HttpHeaders.LOCATION)) // type safe 하게 작성됨
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false)) // type safe 하게 작성됨
                .andExpect(jsonPath("offline").value(true)) // type safe 하게 작성됨
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-events",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing event"),
                                linkWithRel("profile").description("link to update an existing event")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"), // 새로 생성된 이벤트를 조회할 수 있는 URL
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")    // 어떤 컨텐트 타입이다
                        ),
                        relaxedResponseFields( // relaxedResponseFields : 문서의 일부분, 즉 _links에 해당하는 거는
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                                fieldWithPath("free").description("it tells if this event is free or not"),
                                fieldWithPath("offline").description("it tells if this event is offline event or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                /* _link관련된 것들은 없앨 방법을 찾는 것도 추천? */
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query event list"),
                                fieldWithPath("_links.update-event.href").description("link to update existing event"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
   }

    private String getBearerToken() throws Exception {
        return "Bearer " + getAccessToken();
    }

    private String getAccessToken() throws Exception {
        // given
        Account bruce = Account.builder()
                .email(appProperties.getUserUsername())
                .password(appProperties.getUserPassword())
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountService.saveAccount(bruce);

        // 인증 서버가 등록되면 /oauth/token이라는 요청을 처리할 수 있는 핸들러가 적용됨
        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                        .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                        .param("username", appProperties.getUserUsername())
                        .param("password", appProperties.getUserPassword())
                        .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
        var responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        return parser.parseMap(responseBody).get("access_token").toString();
    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 11, 5, 11, 47))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 11, 5, 11, 50))
                .beginEventDateTime(LocalDateTime.of(2022, 11, 6, 9, 18))
                .endEventDateTime(LocalDateTime.of(2022, 11, 7, 4, 23))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("hong-dae")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();
//        // eventRepository.save(event)가 호출되면 event를 리턴하라
//        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                    .contentType(MediaType.APPLICATION_JSON_VALUE) // APPLICATION_JSON_UTF8
                    .accept(MediaTypes.HAL_JSON) // 이 스펙이 준하는 응답을 보내겠다
                    .content(objectMapper.writeValueAsString(event))) // bean으로 등록 안 했지만 있는 것처럼 쓸 수 있음
                .andDo(print()) // 실제 콘솔에서 어떤 요청과 응답을 받았는지 확인 가능
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }
    @Test
    @TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 11, 3, 11, 47))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 11, 4, 11, 50))
                .beginEventDateTime(LocalDateTime.of(2022, 11, 5, 9, 18))
                .endEventDateTime(LocalDateTime.of(2022, 11, 6, 4, 23))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("hong-dae")
                .build();

            this.mockMvc.perform(post("/api/events/")
                            .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                    .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].objectName").exists())
//                .andExpect(jsonPath("content[0].field").exists()) // 어떤 필드에서 발생한 에러인지
                .andExpect(jsonPath("errors[0].defaultMessage").exists()) // 기본 메시지는 무엇이고
                .andExpect(jsonPath("errors[0].code").exists()) // 에러 코드는 무엇이었는지
//                .andExpect(jsonPath("content[0].rejectedValue").exists()) // 입력을 거절당한 그 값이 무엇인지
                .andExpect(jsonPath("_links.index").exists());
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception {
        // given
        IntStream.range(0, 30).forEach(this::generateEvent);

        // when & then
        this.mockMvc.perform(get("/api/events")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "name,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"));
    }

    @Test
    @TestDescription("기존의 이벤트를 하나 조회하기")
    public void getEvent() throws Exception {
        // given
        Event event = this.generateEvent(100);

        // when & then
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"));
    }

    @Test
    @TestDescription("없는 이벤트를 조회했을 때 404 응답받기")
    public void getEvent404() throws Exception {
        // when & then
        this.mockMvc.perform(get("/api/events/11883"))
                .andExpect(status().isNotFound());
    }

    @Test
    @TestDescription("이벤트를 정상적으로 수정하기")
    public void updateEvent() throws Exception {
        // given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        String eventName = "Updated Event";
        eventDto.setName(eventName);

        // when & then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event"));
    }

    @Test
    @TestDescription("입력값이 잘못된 경우에 이벤트 수정 실패")
    public void updateEvent_Bad_Request() throws Exception {
        // given
        Event event = this.generateEvent(200);
        event.setBasePrice(30000);
        event.setMaxPrice(150);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        // when & then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("존재하지 않는 이벤트 이벤트 수정 실패")
    public void updateEvent_Not_Found() throws Exception {
        // given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        // when & then
        this.mockMvc.perform(put("/api/events/112343")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @TestDescription("입력값이 비어있는 경우에 이벤트 수정 실패")
    public void updateEvent_Bad_Request_Empty_Input() throws Exception {
        // given
        Event event = this.generateEvent(200);
        EventDto eventDto = new EventDto();
        // when & then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event " + index)
                .description("test event")
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 11, 4, 11, 47))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 11, 5, 11, 50))
                .beginEventDateTime(LocalDateTime.of(2022, 11, 6, 9, 18))
                .endEventDateTime(LocalDateTime.of(2022, 11, 7, 4, 23))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("hong-dae")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();
        return this.eventRepository.save(event);
    }
}
