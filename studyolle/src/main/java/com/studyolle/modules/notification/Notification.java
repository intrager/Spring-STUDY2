package com.studyolle.modules.notification;

import com.studyolle.modules.account.Account;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class Notification {

    @Id @GeneratedValue
    private Long id;

    private String title;

    private String link;

    private String message; // 짧은 메세지(1줄 분량)

    private boolean checked;

    @ManyToOne
    private Account account; //account 정보가 있어야지 notification을 가지고 알림을 조회

    private LocalDateTime createdDateTime;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
}