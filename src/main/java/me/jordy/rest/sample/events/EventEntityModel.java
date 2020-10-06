package me.jordy.rest.sample.events;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Resource 생성 및 links 생성에 사용되는 클래스
 * JsonUnwrapped를 사용해서 객체 명으로 내부 필드가 감싸지는 것을 방지함
 * 안 쓰고도 감싸지는 것을 방지하기 위한 방법은 아래 클래스를 참조
 */
public class EventEntityModel extends EntityModel<Event> {

    public EventEntityModel(Event event, Link... links) {
        super(event, links);
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}
