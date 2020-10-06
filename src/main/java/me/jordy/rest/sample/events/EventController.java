package me.jordy.rest.sample.events;

import me.jordy.rest.sample.accounts.Account;
import me.jordy.rest.sample.accounts.AccountAdapter;
import me.jordy.rest.sample.accounts.CurrentUser;
import me.jordy.rest.sample.common.ErrorResource;
import me.jordy.rest.sample.index.IndexController;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces= MediaTypes.HAL_JSON_VALUE+";charset=UTF-8")
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors
                                    , @CurrentUser Account currentUser) throws URISyntaxException {

        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        eventValidator.validate(eventDto, errors);

        /*
            - 이벤트라는 도메인은 자바 빈 스펙을 준수 하고 있어서 Bean을 Json으로 변환하는 것이 가능
            - body에 뭔가 값을 넣어서 JSON 으로 변환을 하면 Controller 처리 부 내부에 ObjectMapper가 있어서
              해당 데이터를 빈 시리얼라이즈를 해줌.
         */
        if(errors.hasErrors()) {
            return badRequest(errors);
         }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        event.setOwner(currentUser);
        Event newEvent = eventRepository.save(event);

        // 메소드 명을 써가며 URI를 만들기 보단 아래처럼 하는 것이 나아서 주석처리
        // URI createEventURL = linkTo(methodOn(EventController.class).createEvent(event)).slash("{id}").toUri();

        /* 메소드 온 없이 URI를 만드는 것이 가능하기 때문에 쓰지 않고 간결하게 만듬.*/
//        URI createEventURL = linkTo(EventController.class).slash(newEvent.getId()).toUri();



        /* 지속적인 셀프 링크 사용을 고려해, 위 코드에서 별도의 인스턴스 선언으로 변경.*/
        ControllerLinkBuilder controllerLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createEventURL = controllerLinkBuilder.toUri();

        EventResource eventResource = new EventResource(newEvent);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(controllerLinkBuilder.withRel("update-event"));
        eventResource.add(linkTo(IndexController.class).slash("/docs/event.html#resources-events-create").withRel("profile"));
        return ResponseEntity.created(createEventURL).body(eventResource);

    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable
                                    , PagedResourcesAssembler assembler/* 페이지 리턴 값에 링크 정보를 주기 위해 사용*/
                                    //, @AuthenticationPrincipal User user /* 이 어노태이션은 SecurityContextHolder.getContext().getAuthentication().getPrincipal() 와 동일한 결과 반화 */
                                    , @CurrentUser Account currentUser) {

        Page<Event> page = eventRepository.findAll(pageable);
        PagedResources pagedResource= assembler.toResource(page, e->new EventResource((Event) e));
        pagedResource.add(linkTo(IndexController.class).slash("docs/event.html#resources-events-list").withRel("profile"));

        if (currentUser != null) {
            pagedResource.add(linkTo(EventController.class).withRel("create-event"));
        }

        return ResponseEntity.ok(pagedResource);

    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id, @CurrentUser Account currentUser) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if(!optionalEvent.isPresent())
            return ResponseEntity.notFound().build();

        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource((event));
        eventResource.add(linkTo(IndexController.class).slash("/docs/event.html#resources-events-get").withRel("profile"));
        if(event.getOwner().equals(currentUser))
            eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
        return ResponseEntity.ok(eventResource);

    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id,
                                      @RequestBody @Valid EventDto eventDto,
                                      Errors errors,
                                      @CurrentUser Account currentUser) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if(!optionalEvent.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        eventValidator.validate(eventDto, errors);

        /*
            - 이벤트라는 도메인은 자바 빈 스펙을 준수 하고 있어서 Bean을 Json으로 변환하는 것이 가능
            - body에 뭔가 값을 넣어서 JSON 으로 변환을 하면 Controller 처리 부 내부에 ObjectMapper가 있어서
              해당 데이터를 빈 시리얼라이즈를 해줌.
         */

        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        Event existingEvent = optionalEvent.get();
        if(!existingEvent.getOwner().equals(currentUser))
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        modelMapper.map(eventDto, existingEvent);

        Event updatedEvent = eventRepository.save(existingEvent);

        EventResource eventResource = new EventResource((updatedEvent));
        eventResource.add(linkTo(IndexController.class).slash("/docs/event.html#resources-events-update").withRel("profile"));
        return ResponseEntity.ok(eventResource);

    }

    private ResponseEntity<ErrorResource> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorResource(errors));
    }


}
