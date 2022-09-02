package com.study.templateapi.domain.member.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/** getter는 있지만 setter는 없음.
 * member entity 클래스가 갖는 필드값들을 무분별하게 접근하지 않고
 * 필요한 필드만 업뎃할 수 있도록 메서드를 따로 둘 거임
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
}
