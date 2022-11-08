package me.brucehan.restfulhan.accounts;

import me.brucehan.restfulhan.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountServiceTest extends BaseTest {
//    @Rule public ExpectedException expectedException = ExpectedException.none(); -> Deprecated

    @Autowired AccountService accountService;
    @Autowired AccountRepository accountRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    public void findByUsername() {
        // given
        String password = "bruce";
        String username = "bruce@email.com";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountService.saveAccount(account);
        // when
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // then
        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
    }

    @Test // (expected = UsernameNotFoundException.class) -> try ~ catch 대신 쓸 수는 있긴 함
    public void findByUsernameFail() {
        assertThrows(UsernameNotFoundException.class, () -> accountService.loadUserByUsername("bruce@email.com"));
        /*
            // given
            String username = "asdf@email.com";
            // Expected
            expectedException.expect(UsernameNotFoundException.class);
            expectedException.expectMessage(Matchers.containsString(username));
            // when
            accountService.loadUserByUsername(username);
         */
    }
}