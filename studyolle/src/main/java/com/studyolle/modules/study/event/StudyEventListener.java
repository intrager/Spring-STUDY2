package com.studyolle.modules.study.event;

import com.studyolle.infra.config.AppProperties;
import com.studyolle.infra.mail.EmailMessage;
import com.studyolle.infra.mail.EmailService;
import com.studyolle.modules.account.Account;
import com.studyolle.modules.account.AccountPredicates;
import com.studyolle.modules.account.AccountRepository;
import com.studyolle.modules.notification.Notification;
import com.studyolle.modules.notification.NotificationRepository;
import com.studyolle.modules.notification.NotificationType;
import com.studyolle.modules.study.Study;
import com.studyolle.modules.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Slf4j
@Async // 각각 들어오는 이벤트 (사람)
@Component
@Transactional
@RequiredArgsConstructor
public class StudyEventListener {

    private final StudyRepository studyRepository;
    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;
    private final NotificationRepository notificationRepository;

    // 수영장 튜브
    @EventListener
    public void handleStudyCreatedEvent(StudyCreatedEvent studyCreatedEvent) { // 전달하려는 값을 이 안에서 써야하니까 매서드 파라미터로 받음
        Study study = studyRepository.findStudyWithTagsAndZonesById(studyCreatedEvent.getStudy().getId()); //detached
        Iterable<Account> accounts = accountRepository.findAll(AccountPredicates.findByTagsAndZones(study.getTags(), study.getZones()));
        // accounts를 순회하면서
        accounts.forEach(account -> {
            if(account.isStudyCreatedByEmail()) { // email로 알림 받겠다고 했으면
                sendStudyCreatedEmail(study, account);
            }
            if(account.isStudyCreatedByWeb()) { // Web으로 알림 받겠다고 했으면
                saveStudyCreatedNotification(study, account);
            }
        });
    }

    private void saveStudyCreatedNotification(Study study, Account account) {
        Notification notification = new Notification();
        notification.setTitle(study.getTitle());
        notification.setLink("/study/" + study.getEncodedPath());
        notification.setChecked(false);
        notification.setCreatedLocalDateTime(LocalDateTime.now());
        notification.setMessage(study.getShortDescription());
        notification.setAccount(account);
        notification.setNotificationType(NotificationType.STUDY_CREATED);
        notificationRepository.save(notification);
    }

    private void sendStudyCreatedEmail(Study study, Account account) {
        Context context = new Context();
        context.setVariable("nickname", account.getNickname());
        context.setVariable("link", "/study/" + study.getEncodedPath());
        context.setVariable("linkName", study.getTitle());
        context.setVariable("message", "새로운 스터디가 생겼습니다");
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/simple-link", context);

        templateEngine.process("mail/simple-link", context);
        EmailMessage emailMessage = EmailMessage.builder()
                .subject("모터디, '" + study.getTitle() + "'스터디가 생겼습니다.")
                .to(account.getEmail())
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
    }
}
