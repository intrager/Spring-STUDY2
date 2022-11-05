package me.brucehan.restfulhan.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
/* 웹과 관련된 빈들이 모두 등록되고, MockMVC를 통해서 쉽게 웹용 빈을 주입받을 수 있음
단, Repository는 등록 안 해줌
*/
@WebMvcTest
public class EventControllerTests {
    /** 가짜 요청을 만들어서 DispatcherServlet에 보내고, 그 응답을 확인할 수 있음
     *  MockMvc를 사용하게 되면 Slicing test가 됨
     */
    @Autowired MockMvc mockMvc;

    // Mapping jackson json이 의존성으로 들어가 있으면 Object Mapper를 자동으로 빈으로 등록해준다
    @Autowired ObjectMapper objectMapper;

    @MockBean EventRepository eventRepository; // mock이기 때문에 save를 해도 null을 리턴함함

   @Test
    public void createEvent() throws Exception {
        Event event = Event.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 11, 5, 11, 47))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 11, 5, 11, 50))
                .beginEventDateTime(LocalDateTime.of(2022, 11, 6, 9, 18))
                .endEventDateTime(LocalDateTime.of(2022, 11, 7, 4, 23))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("홍대입구역")
                .build();
        event.setId(10);
        // eventRepository.save(event)가 호출되면 event를 리턴하라
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE) // APPLICATION_JSON_UTF8
                        .accept(MediaTypes.HAL_JSON) // 이 스펙이 준하는 응답을 보내겠다
                        .content(objectMapper.writeValueAsString(event))) // bean으로 등록 안 했지만 있는 것처럼 쓸 수 있음
                .andDo(print()) // 실제 콘솔에서 어떤 요청과 응답을 받았는지 확인 가능
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists()); // "id"가 있는지 테스트
    }
}
