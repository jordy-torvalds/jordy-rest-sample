package me.jordy.rest.sample.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("인프런 스프링 레스트 에이피아이")
                .description("인프런에서 스프링 레스트에 대하여 공부해봐요!")


                .build();
        assertThat(event).isNotNull();
    }
}