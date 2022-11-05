package me.brucehan.restfulhan.events;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest // 웹과 관련된 빈들이 모두 등록되고, MockMVC를 통해서 쉽게 빈을 주입받을 수 있음
public class EventControllerTests {
    @Autowired MockMvc mockMvc; // 가짜 요청을 만들어서 DispatcherServlet에 보내고, 그 응답을 확인할 수 있음
    // MockMvc를 사용하게 되면 Slicing test가 됨

    @Test
    public void createEvent() throws Exception {
        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE) // APPLICATION_JSON_UTF8
                        .accept(MediaTypes.HAL_JSON)) // 이 스펙이 준하는 응답을 보내겠다
                .andExpect(status().isCreated());
    }
}
