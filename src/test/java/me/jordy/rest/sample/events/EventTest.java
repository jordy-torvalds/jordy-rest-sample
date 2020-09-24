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

    @Test
    public void javaBean() {

        //Given
        String name = "죠르디 자바 코딩 강의";
        String description = "죠르디의 자바 노하우가 깃든 코딩 강의 입니다.";

        //When
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        //Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @Test
    public void testFree() {
        Event event;
        //Given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isFree()).isTrue();

        //Given
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isFree()).isFalse();

        //Given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isFree()).isFalse();

    }

    @Test
    public void testOffline() {
        Event event;

        // Given
        event = Event.builder()
                .location("삼성 건물")
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isOffline()).isFalse();


        // Given
        event = Event.builder()
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isOffline()).isTrue();
    }
}