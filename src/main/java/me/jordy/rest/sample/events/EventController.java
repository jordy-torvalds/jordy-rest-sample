package me.jordy.rest.sample.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;
import java.net.URL;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/events", produces= MediaTypes.HAL_JSON_VALUE+";charset=UTF-8")
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event) {
        Event newEvent = eventRepository.save(event);

        /* 메소드 온 없이 URI를 만드는 것이 가능하기 때문에 쓰지 않고 간결하게 만듬.*/
        URI createEventURL = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        // URI createEventURL = linkTo(methodOn(EventController.class).createEvent(event)).slash("{id}").toUri();

        event.setId(10);
        return ResponseEntity.created(createEventURL).body(event);

    }
}
