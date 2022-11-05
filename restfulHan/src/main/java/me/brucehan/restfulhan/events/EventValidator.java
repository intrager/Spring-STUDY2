package me.brucehan.restfulhan.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {
    public void validate(EventDto eventDto, Errors errors) {
        // 기본 금액이 최대치보다 큰데, 최대치도 양수일 때
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
            errors.rejectValue("basePrice", "wrongValue", "basePrice is wrong.");
            errors.rejectValue("maxPrice", "wrongValue", "maxPrice is wrong.");
            // global로 들어가는 error
            errors.reject("wrongPrices", "Value of Prices are wrong"); // 얘만 있으면 안 되던데 왜인지는 찾아봐야됨
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if(endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
        endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
        endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            // field로 들어가는 error
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong.");
        }
    }
}
