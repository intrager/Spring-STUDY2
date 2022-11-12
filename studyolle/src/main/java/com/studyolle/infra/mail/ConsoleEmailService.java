package com.studyolle.infra.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile({"local", "test"}) // 콘솔에서 개발할 때, test 안 넣으면 테스트 돌릴 때 빈이 없음
@Component
public class ConsoleEmailService implements EmailService {

    @Override
    public void sendEmail(EmailMessage emailMessage) {
        log.info("sent email : {}", emailMessage.getMessage());
    }
}
