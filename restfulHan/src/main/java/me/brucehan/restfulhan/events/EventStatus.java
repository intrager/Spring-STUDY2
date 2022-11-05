package me.brucehan.restfulhan.events;

public enum EventStatus {
    DRAFT, PUBLISHED, BEGAN_ENROLLMENT;
} // ORDINARY? -> 0, 1, 2 중간에 추가/삭제 시 인덱싱 작업을 다시해야될 수도 있음(유지보수성이 낮음)
