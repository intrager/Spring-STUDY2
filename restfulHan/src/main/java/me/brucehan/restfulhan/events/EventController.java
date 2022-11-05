package me.brucehan.restfulhan.events;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE) // 클래스 안에 있는 모든 핸들러는 HAL+JSON이라는 응답을 보내게 됨
@RequiredArgsConstructor
public class EventController {

    /* 생성자가 하나만 있고, 이 생성자로 받아올 파라미터가 이미 빈으로 등록되어있다면,
    생성자에 붙일 Autowired라는 애노테이션은 생략해도 됨 (after spring 4.3~)
    */
    private final EventRepository eventRepository;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event) {
        Event newEvent = this.eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();// id에 해당하는 링크 생성
        return ResponseEntity.created(createdUri).body(event);
    }
}
