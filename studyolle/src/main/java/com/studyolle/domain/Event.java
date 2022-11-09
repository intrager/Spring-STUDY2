package com.studyolle.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class Event {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne // Study를 가리키는 fk가 생성될 거임
    private Study study; // 어느 스터디에 속한 이벤트인지

    @ManyToOne // Account를 가리키는 fk가 생성될 거임
    private Account createdBy;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdDateTime;

    @Column(nullable = false)
    private LocalDateTime endEnrollmentDateTime;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Column
    private Integer limitOfEnrollments; // null이 들어갈 수 있게끔

    @OneToMany(mappedBy = "event") // 이 연관관계는 event에서 하고 있다
    private List<Enrollment> enrollments;

    @Enumerated(EnumType.STRING)
    private EventType eventType;
}
