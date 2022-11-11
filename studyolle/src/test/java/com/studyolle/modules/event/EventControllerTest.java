package com.studyolle.modules.event;

import com.studyolle.modules.account.WithAccount;
import com.studyolle.modules.account.Account;
import com.studyolle.modules.study.Study;
import com.studyolle.modules.study.StudyControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EventControllerTest extends StudyControllerTest {
    @Autowired
    EventService eventService;
    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Test
    @DisplayName("선착순 모임에 참가 신청 - 자동 수락")
    @WithAccount("bruce")
    void newEnrollment_to_FCFS_event_accepted() throws Exception {
        Account brucehan = createAccount("brucehan");
        Study study = createStudy("test-study", brucehan);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, brucehan);

        mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/enroll")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));

        Account bruce = accountRepository.findByNickname("bruce");
        isAccepted(bruce, event);
    }

    @Test
    @DisplayName("선착순 모임에 참가 신청 - 이미 인원이 꽉차서 대기중")
    @WithAccount("bruce")
    void newEnrollment_to_FCFS_event_not_accepted() throws Exception {
        Account brucehan = createAccount("brucehan");
        Study study = createStudy("test-study", brucehan);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, brucehan);

        Account jason = createAccount("jason");
        Account jeongsoo = createAccount("jeongsoo");
        eventService.newEnrollment(event, jason);
        eventService.newEnrollment(event, jeongsoo);

        mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/enroll")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));

        Account bruce = accountRepository.findByNickname("bruce");
        isNotAccepted(bruce, event);
    }

    @Test
    @DisplayName("참가신청이 확정된 자가 선착순 모임에서 신청을 취소하는 경우, 바로 그 다음 대기자를 자동으로 신청 확인한다")
    @WithAccount("bruce")
    void accepted_account_cancelEnrollment_to_FCFS_event_not_accepted() throws Exception {
        Account bruce = accountRepository.findByNickname("bruce");
        Account brucehan = createAccount("brucehan");
        Account jason = createAccount("jason");
        Study study = createStudy("test-study", brucehan);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, brucehan);

        eventService.newEnrollment(event, jason);
        eventService.newEnrollment(event, bruce);
        eventService.newEnrollment(event, brucehan);

        isAccepted(jason, event);
        isAccepted(bruce, event);
        isNotAccepted(brucehan, event);

        mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/disenroll")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() +"/events/" + event.getId()));

        isAccepted(jason, event);
        isAccepted(brucehan, event);
        assertNull(enrollmentRepository.findByEventAndAccount(event, bruce));
    }

    @Test
    @DisplayName("참가신청이 확정되지 않은 자가 선착순 모임에 참가 신청을 취소하는 경우, 기존 확장자를 그대로 유지하되 새로 확정되는 이는 없다.")
    @WithAccount("bruce")
    void not_accepted_account_cancelEnrollment_to_FCFS_event_not_accepted() throws Exception {
        Account bruce = accountRepository.findByNickname("bruce");
        Account brucehan = createAccount("brucehan");
        Account jason = createAccount("jason");
        Study study = createStudy("test-study", brucehan);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, brucehan);

        eventService.newEnrollment(event, jason);
        eventService.newEnrollment(event, brucehan);
        eventService.newEnrollment(event, bruce);

        isAccepted(jason, event);
        isAccepted(brucehan, event);
        isNotAccepted(bruce, event);

        mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/disenroll")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));

        isAccepted(jason, event);
        isAccepted(brucehan, event);
        assertNull(enrollmentRepository.findByEventAndAccount(event, bruce));
    }

    @Test
    @DisplayName("관리자 확인 모임에 참가 신청 - 대기중")
    @WithAccount("bruce")
    void newEnrollment_to_CONFIRMATIVE_event_not_accepted() throws Exception {
        Account brucehan = createAccount("brucehan");
        Study study = createStudy("test-study", brucehan);
        Event event = createEvent("test-event", EventType.CONFIRMATIVE, 2, study, brucehan);

        mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/enroll")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));

        Account bruce = accountRepository.findByNickname("bruce");
        isNotAccepted(bruce, event);
    }

    private void isNotAccepted(Account account, Event event) {
        assertFalse(enrollmentRepository.findByEventAndAccount(event, account).isAccepted());
    }

    private void isAccepted(Account account, Event event) {
        assertTrue(enrollmentRepository.findByEventAndAccount(event, account).isAccepted());
    }

    private Event createEvent(String eventTitle, EventType eventType, int limit, Study study, Account account) {
        Event event = new Event();
        event.setEventType(eventType);
        event.setLimitOfEnrollments(limit);
        event.setTitle(eventTitle);
        event.setCreatedDateTime(LocalDateTime.now());
        event.setEndEnrollmentDateTime(LocalDateTime.now().plusDays(1));
        event.setStartDateTime(LocalDateTime.now().plusDays(1).plusHours(5));
        event.setEndDateTime(LocalDateTime.now().plusDays(1).plusHours(7));
        return eventService.createEvent(event, study, account);
    }
}
