package me.jordy.rest.sample.events;

import lombok.*;

import java.time.LocalDateTime;

@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter @EqualsAndHashCode(of="id")
public class Event {

    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    private EventStatus eventStatus;

    public String getEventPrice() {
        if(basePrice == 0  && maxPrice == 100) {
            return "FCFS"; // first come, first served
        } else if (basePrice == 0 && maxPrice == 0) {
            return "free";
        } else if (basePrice == 100 && maxPrice == 0) {
            return "unlimitedAuction";
        } else if (basePrice == 100 && maxPrice == 200) {
            return "unlimitedAuctionWithFCFS";
        }

    }
}
