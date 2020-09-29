package me.jordy.rest.sample.events;

import me.jordy.rest.sample.common.ErrorResource;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) throws URISyntaxException {

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
        eventResource.add(new Link("/docs/event.html").withRel("profile"));
        return ResponseEntity.created(createEventURL).body(eventResource);

    }

    private ResponseEntity<ErrorResource> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorResource(errors));
    }
}
