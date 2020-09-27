package me.jordy.rest.sample.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.ResourceSupport;

public class EventResource extends ResourceSupport {

    /**
     * - 어노태이션 적용 전
     *   - {"event":{"id":1,"name":"죠르디 스프링 특강","description":"죠르디가 진행하는 스프링 특강","beginEnrollmentDateTime":"2020-09-23T12:23:45","closeEnrollmentDateTime":"2020-09-23T14:23:45","beginEventDateTime":"2020-10-23T12:23:45","endEventDateTime":"2020-10-23T14:23:45","location":"서울 스타듀밸리","basePrice":100,"maxPrice":200,"limitOfEnrollment":20,"offline":false,"free":false,"eventStatus":"DRAFT"},"_links":{"query-events":{"href":"http://localhost/api/events"},"self":{"href":"http://localhost/api/events/1"},"update-event":{"href":"http://localhost/api/events/1"}}}
     * - 어노태이션 적용 후
     *   - {"id":1,"name":"죠르디 스프링 특강","description":"죠르디가 진행하는 스프링 특강","beginEnrollmentDateTime":"2020-09-23T12:23:45","closeEnrollmentDateTime":"2020-09-23T14:23:45","beginEventDateTime":"2020-10-23T12:23:45","endEventDateTime":"2020-10-23T14:23:45","location":"서울 스타듀밸리","basePrice":100,"maxPrice":200,"limitOfEnrollment":20,"offline":false,"free":false,"eventStatus":"DRAFT","_links":{"query-events":{"href":"http://localhost/api/events"},"self":{"href":"http://localhost/api/events/1"},"update-event":{"href":"http://localhost/api/events/1"}}}
     */
    @JsonUnwrapped
    private Event event;

    public EventResource(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
