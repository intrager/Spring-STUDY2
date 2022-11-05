package me.brucehan.restfulhan.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.brucehan.restfulhan.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
/* 웹과 관련된 빈들이 모두 등록되고, MockMVC를 통해서 쉽게 웹용 빈을 주입받을 수 있음
단, Repository는 등록 안 해줌
*/
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {
    /** 가짜 요청을 만들어서 DispatcherServlet에 보내고, 그 응답을 확인할 수 있음
     *  MockMvc를 사용하게 되면 Slicing test가 됨
     */
    @Autowired MockMvc mockMvc;

    // Mapping jackson json이 의존성으로 들어가 있으면 Object Mapper를 자동으로 빈으로 등록해준다
    @Autowired ObjectMapper objectMapper;
//    @MockBean EventRepository eventRepository; // mock이기 때문에 save를 해도 null을 리턴함

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
                    .contentType(MediaType.APPLICATION_JSON_VALUE) // APPLICATION_JSON_UTF8
                    .accept(MediaTypes.HAL_JSON) // 이 스펙이 준하는 응답을 보내겠다
                    .content(objectMapper.writeValueAsString(event))) // bean으로 등록 안 했지만 있는 것처럼 쓸 수 있음
                .andDo(print()) // 실제 콘솔에서 어떤 요청과 응답을 받았는지 확인 가능
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists()) // "id"가 있는지 테스트
                .andExpect(header().exists(HttpHeaders.LOCATION)) // type safe 하게 작성됨
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false)) // type safe 하게 작성됨
                .andExpect(jsonPath("offline").value(true)) // type safe 하게 작성됨
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists());
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

            this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                    .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].field").exists()) // 어떤 필드에서 발생한 에러인지
                .andExpect(jsonPath("$[0].defaultMessage").exists()) // 기본 메시지는 무엇이고
                .andExpect(jsonPath("$[0].code").exists()) // 에러 코드는 무엇이었는지
                .andExpect(jsonPath("$[0].rejectedValue").exists()); // 입력을 거절당한 그 값이 무엇인지
    }
}
