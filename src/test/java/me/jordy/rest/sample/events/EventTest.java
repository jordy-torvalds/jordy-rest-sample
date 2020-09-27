package me.jordy.rest.sample.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class EventTest {

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
    /**
     *  파라미터 적용 방법
     *  1. 명시적으로 할당
     *  @Parameters({
     *         "0, 0, true",
     *         "100, 0, false",
     *         "0, 100, false"
     *  })
     *
     *  2. 컨벤션을 준수한 이름을 가진 Object 배열 반환 메서드를 파라미터로 사용
     *  - 메서드에 @Parameters 어노태이션 붙이기
     *  - 컨벤션을 준수한 이름을 가진 객체 선언
     *
     *      private Object[] parametersForTestFree() {
     *         return new Object[] {
     *                 new Object [] {0, 0, true},
     *                 new Object [] {100, 0, false},
     *                 new Object [] {0, 100, false},
     *                 new Object [] {100, 200, false}
     *         };
     *     }
     *
     *  3. 원하는 이름을 가진 Object 배열 반환 메서드를 파라미터로 사용
     *  - @Parameters(method="paramsForTestFree")
     */
    @Parameters(method="paramsForTestFree")
    public void testFree(int basePrice, int maxPrice, boolean isFree) throws Exception {
        Event event;
        //Given
        event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private Object[] paramsForTestFree() {
        return new Object[] {
                new Object [] {0, 0, true},
                new Object [] {100, 0, false},
                new Object [] {0, 100, false},
                new Object [] {100, 200, false}
        };
    }

    @Test
    @Parameters
    public void testOffline(String location, boolean isOffline) {
        Event event;

        // Given
        event = Event.builder()
                .location(location)
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isOffline()).isEqualTo(isOffline);

    }

    private Object[] parametersForTestOffline() {
        return new Object[] {
                new Object[] {"삼성 건물", false},
                new Object[] {null, true}
        };
    }
}