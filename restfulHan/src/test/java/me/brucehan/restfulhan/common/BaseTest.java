package me.brucehan.restfulhan.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/* 웹과 관련된 빈들이 모두 등록되고, MockMVC를 통해서 쉽게 웹용 빈을 주입받을 수 있음
단, Repository는 등록 안 해줌
*/
//@WebMvcTest

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class) // 다른 스프링 빈 설정 파일을 읽어와서 사용하는 방법 중 하나
@ActiveProfiles("test")
@Disabled // test를 가지고 있는 클래스가 아니므로
public class BaseTest {
    /** 가짜 요청을 만들어서 DispatcherServlet에 보내고, 그 응답을 확인할 수 있음
     *  MockMvc를 사용하게 되면 Slicing test가 됨
     */
    @Autowired
    protected MockMvc mockMvc;
    // Mapping jackson json이 의존성으로 들어가 있으면 Object Mapper를 자동으로 빈으로 등록해준다
    @Autowired
    protected ObjectMapper objectMapper;
    //    @MockBean EventRepository eventRepository; // mock이기 때문에 save를 해도 null을 리턴함
    @Autowired
    protected ModelMapper modelMapper;
}
