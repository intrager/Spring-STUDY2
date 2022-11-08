package me.brucehan.restfulhan.events;

import lombok.RequiredArgsConstructor;
import me.brucehan.restfulhan.accounts.Account;
import me.brucehan.restfulhan.accounts.AccountAdapter;
import me.brucehan.restfulhan.accounts.CurrentUser;
import me.brucehan.restfulhan.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

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
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto,
                                      Errors errors, @CurrentUser Account currentUser) {
        if(errors.hasErrors()) { // jsr 303
            return badRequest(errors);
        }

        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        event.setManager(currentUser);
        Event newEvent = this.eventRepository.save(event);
        Integer eventId = newEvent.getId();

        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri = selfLinkBuilder.toUri();// id에 해당하는 링크 생성

        EntityModel eventResource = EntityModel.of(newEvent);
        eventResource.add(linkTo(EventController.class).slash(eventId).withSelfRel());
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        eventResource.add(Link.of("/docs/index.html#resources-events-create").withRel("profile"));
        return ResponseEntity.created(createdUri).body(eventResource);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable,
                                      PagedResourcesAssembler<Event> assembler,
                                      @CurrentUser Account account) {

        Page<Event> page = this.eventRepository.findAll(pageable);
        var pagedResources = assembler.toModel(page, (RepresentationModelAssembler<Event, RepresentationModel<?>>) entity -> new EventResource(entity));
        pagedResources.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));
        if(account != null) {
            pagedResources.add(linkTo(EventController.class).withRel("create-event"));
        }
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id,
                                   @CurrentUser Account currentUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if(optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource(event);
        eventResource.add(Link.of("/docs/index.html#resources-events-get").withRel("profile"));
        if(event.getManager().equals(currentUser)) { // update-event를 매니저인 경우에만 노출
            eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
        }
        return ResponseEntity.ok(eventResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id,
                                      @RequestBody @Valid EventDto eventDto,
                                      Errors errors,
                                      @CurrentUser Account currentUser) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if(optionalEvent.isEmpty()) { // 옵셔널 풀었는데 비어있으면
            // 이거 리턴 안 해줬더니 optionalEvent.get()을 해도 비었는 거 확인 안 했니? 라는 오류 발견
            return ResponseEntity.notFound().build();
        }
        if(errors.hasErrors()) { // 데이터 binding이 잘못됐으면
            return badRequest(errors);
        }
        this.eventValidator.validate(eventDto, errors); // 실제 값이 비즈니스 로직에 맞는지 확인
        if(errors.hasErrors()) { // 비즈니스 로직상 문제가 있는 것
            return badRequest(errors);
        }

        Event existingEvent = optionalEvent.get();
        if(existingEvent.getManager().equals(currentUser)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        // eventDto데이터를 existingEvent로 옮김
        this.modelMapper.map(eventDto, existingEvent);
        Event savedEvent = this.eventRepository.save(existingEvent);

        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(Link.of("/docs/index.html#resources-events-update").withRel("profile"));

        return ResponseEntity.ok(eventResource);
    }

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(ErrorsResource.modelOf(errors));
    }
}
