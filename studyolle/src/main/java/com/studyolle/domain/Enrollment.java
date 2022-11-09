package com.studyolle.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * N이 되는 쪽에서 1이 되는 쪽을 fk로 참조하는 방법이 가장 많이 쓰임
 * fk는 Enrollment라는 테이블에 생기게 될 거임. 조인 테이블은 생성되지 않음
 */
@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class Enrollment {

    @Id
    @GeneratedValue
    private Long id;

    // enrollment에다가 event값을 어떻게 넣느냐에 따라 db에 반영됨
    @ManyToOne
    private Event event; // 관계의 주인

    @ManyToOne
    private Account account;

    /* 만약 풀방에서 인원 더 신청했는데,
       모임에 들어간 한 명이 나가면 자동으로 다음 순서가 들어오도록 하기 위함 */
    private LocalDateTime enrolledAt;

    private boolean accepted; // 확정인가 아닌가

    private boolean attended; // 실제로 참석 했는지 안 했는지
}
