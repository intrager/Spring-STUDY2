package com.studyolle.modules.study.event;

import com.studyolle.modules.study.Study;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Async // 각각 들어오는 이벤트 (사람)
@Component
@Transactional(readOnly = true)
public class StudyEventListener {

    // 수영장 튜브
    @EventListener
    public void handleStudyCreatedEvent(StudyCreatedEvent studyCreatedEvent) { // 전달하려는 값을 이 안에서 써야하니까 매서드 파라미터로 받음
        Study study = studyCreatedEvent.getStudy();
        log.info(study.getTitle() + "is created.");
        // TODO 이메일 보내거나 DB에 Notification 정보를 저장하면 됨 -> 해당 스터디의 주제와 활동지역에 관심있는 사용자에게
        throw new RuntimeException();
    }
}
