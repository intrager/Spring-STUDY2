package me.brucehan.restfulhan.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static me.brucehan.restfulhan.events.EventStatus.DRAFT;

@Entity
// lombok 어노테이션은 메타 어노테이션으로 못 만듦
@Builder
@Getter @Setter
/** Equals와 HashCode를 구현할 때 모든 필드를 기본으로 사용함.
 * 그러나 나중에 엔티티(객체)간 연관관계가 있을 때 연관관계 간에 상호참조하는 관계가 되어버리면,
 * Equals와 HashCode를 구현한 코드 안에서 Stack Overflow가 발생할 수 있음
 */
@EqualsAndHashCode(of = "id") // 연관관계도 of 안에 넣어버리면, 즉 다른 엔티티와의 묶음은 마찬가지로 상호참조 때문에 지양 -> Stack Overflow 가능성 있음
@NoArgsConstructor @AllArgsConstructor
public class Event {
    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = DRAFT;

    public void update() {
        // update free
        this.free = this.basePrice == 0 && this.maxPrice == 0 ? true : false;

        // update offline
        this.offline = this.location == null || this.location.isBlank() ? false : true;
    }
    /** Java 11 에서 생긴 isBlank() : 공백 문자열 확인
     * isBlank()이 원래는 String.trim() -> compaction -> isEmpty()
     */
}
