package me.brucehan.restfulhan.events;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE) // 클래스 안에 있는 모든 핸들러는 HAL+JSON이라는 응답을 보내게 됨
@RequiredArgsConstructor
public class EventController {

    /* 생성자가 하나만 있고, 이 생성자로 받아올 파라미터가 이미 빈으로 등록되어있다면,
    생성자에 붙일 Autowired라는 애노테이션은 생략해도 됨 (after spring 4.3~)
    */
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        if(errors.hasErrors()) { // jsr 303
            return ResponseEntity.badRequest().body(errors); // body(errors) -> errors라는 객체를 json으로 그냥 변환할 수 없기 때문에 에러남
        }

        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        Event newEvent = this.eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();// id에 해당하는 링크 생성
        return ResponseEntity.created(createdUri).body(event);
    }
}
