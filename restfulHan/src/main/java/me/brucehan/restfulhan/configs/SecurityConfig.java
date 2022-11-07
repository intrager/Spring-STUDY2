package me.brucehan.restfulhan.configs;

import me.brucehan.restfulhan.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * WebSecurityConfigurerAdapter를 상속받는 순간
 * Boot에서 제공해주는 security설정은 더 이상 적용되지 않음
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    final AccountService accountService;
    final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(AccountService accountService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder);
    }

    /**
     * configure(HttpSecurity http)보다는 일을 덜 함
     * static 리소스들을 전부 다 허용할 거라면 필터 자체,
     * 즉 configure(WebSecurity web)에서 걸러주는 게 서버가 조금이라도 일을 덜 하게 되니까
     * configure(WebSecurity web)이 더 나을 수도 있음
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // /docs/index.html 경로로 들어오는 요청은 무시
        web.ignoring().mvcMatchers("/docs/index.html");
        // boot가 제공해주는 static 리소스들에 대한 기본 위치를 가져와서 security적용이 되지 않도록 설정
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .mvcMatchers("/doc/index.html").anonymous()
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .anonymous()
//                .and()
//                .formLogin()
//                .and()
//                .authorizeRequests()
//                .mvcMatchers(HttpMethod.GET, "/api/**").authenticated()
//                .anyRequest().authenticated(); // 위에서 설정한 것들을 제외한 나머지들은 인증이 필요하다
//    }
}
