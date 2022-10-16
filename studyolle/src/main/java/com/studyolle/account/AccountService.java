package com.studyolle.account;

import com.studyolle.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm); //위에 @Transactional을 안 붙이면 save 후로 detached 상태가 됨
        newAccount.generateEmailCheckToken(); // 토큰 만듦 //위에 @Transactional을 붙임으로써 Persist 상태 유지
        sendSignUpConfirmEmail(newAccount); // 메세지 보내기
        return newAccount;
    } // Persist상태의 객체는 트랜잭션이 종료될 때 상태를 DB에 Sink함

    private Account saveNewAccount(SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword())) // TODO encoding 해야함
                .studyCreatedByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .studyUpdatedByWeb(true)
                .build();
        // 회원 가입 및 저장
        return accountRepository.save(account);
    }

    public void sendSignUpConfirmEmail(Account newAccount) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("스터디올래 회원 가입 인증"); // 이메일 제목
        mailMessage.setText("/check-email-token?token="+ newAccount.getEmailCheckToken() +
                "&email=" + newAccount.getEmail()); // 이메일 본문
        javaMailSender.send(mailMessage);
    }

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account), // 로그인 했으면 이 생성된 객체가 인증된 principal 로 간주됨
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))); // 권한 목록을 받아줌
        SecurityContextHolder.getContext().setAuthentication(token);
    } // 인코딩한 password로밖에 접근을 못하기 때문에 정석적이지 않은, AuthenticationManager 방법으로 작성

    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname);
        if(account == null) {
            account = accountRepository.findByNickname(emailOrNickname);
        }
        if(account == null) {
            // 여기에 해당하는 유저가 없다 -> 이메일 혹은 패스워드가 잘못됐다고 리턴하면 됨 -> 메세지를 보여줄 것
            throw new UsernameNotFoundException(emailOrNickname);
        }
        // principal에 해당하는 객체를 넣어주면 됨
        return new UserAccount(account);
    }
}
