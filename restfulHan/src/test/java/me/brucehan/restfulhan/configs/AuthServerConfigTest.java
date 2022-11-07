package me.brucehan.restfulhan.configs;

import me.brucehan.restfulhan.accounts.Account;
import me.brucehan.restfulhan.accounts.AccountRole;
import me.brucehan.restfulhan.accounts.AccountService;
import me.brucehan.restfulhan.common.AppProperties;
import me.brucehan.restfulhan.common.BaseControllerTest;
import me.brucehan.restfulhan.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {
    @Autowired AccountService accountService;
    @Autowired AppProperties appProperties;

    @Test
    @TestDescription("인증 토근을 발급 받는 테스트")
    public void getAuthToken() throws Exception {
        // 인증 서버가 등록되면 /oauth/token이라는 요청을 처리할 수 있는 핸들러가 적용됨
        this.mockMvc.perform(post("/oauth/token")
                        .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                        .param("username", appProperties.getUserUsername())
                        .param("password", appProperties.getUserPassword())
                        .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }
}