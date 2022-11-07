package me.brucehan.restfulhan.configs;

import me.brucehan.restfulhan.accounts.Account;
import me.brucehan.restfulhan.accounts.AccountRole;
import me.brucehan.restfulhan.accounts.AccountService;
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

    @Test
    @TestDescription("인증 토근을 발급 받는 테스트")
    public void getAuthToken() throws Exception {
        // given
        String username = "bruce@email.com";
        String password = "bruce";
        Account bruce = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountService.saveAccount(bruce);

        String clientId = "qwer";
        String clientSecret = "pass";
        // 인증 서버가 등록되면 /oauth/token이라는 요청을 처리할 수 있는 핸들러가 적용됨
        this.mockMvc.perform(post("/oauth/token")
                        .with(httpBasic(clientId, clientSecret))
                        .param("username", username)
                        .param("password", password)
                        .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }
}