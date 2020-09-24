package me.jordy.rest.sample.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {
    /**
     * rejectValue는 필드 에러,
     * 그리고 reject는 글로벌 에러.
     */
    public void validate(EventDto eventDto, Errors errors) {
        if(eventDto.getBasePrice() > eventDto.getMaxPrice()
                && eventDto.getMaxPrice() != 0) {
            errors.reject("wrongPrices", "values for price are wrong");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if(endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
                endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())  ||
                endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()) ) {
            errors.rejectValue("endEventDateTime","wrongValue", "endEventDateTime is Wrong");
        }

        LocalDateTime beginEventDateTime = eventDto.getBeginEventDateTime();
        if(beginEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())  ||
                beginEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()) ) {
            errors.rejectValue("beginEventDateTime","wrongValue", "beginEventDateTime is Wrong");
        }

        LocalDateTime closeEnrollmentDateTime = eventDto.getCloseEnrollmentDateTime();
        if(closeEnrollmentDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()) ) {
            errors.rejectValue("closeEnrollmentDateTime","wrongValue", "closeEnrollmentDateTime is Wrong");
        }

        //TODO
    }
}
