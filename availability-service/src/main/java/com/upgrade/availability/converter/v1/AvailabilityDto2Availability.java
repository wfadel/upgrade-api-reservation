package com.upgrade.availability.converter.v1;

import com.upgrade.availability.api.dto.v1.AvailabilityDto;
import com.upgrade.availability.model.Availability;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AvailabilityDto2Availability implements Converter<AvailabilityDto, Availability> {
    @Override
    public Availability convert(AvailabilityDto availabilityDto) {
        if (availabilityDto == null) {
            return null;
        }

        return Availability.builder()
                .day(availabilityDto.getDay())
                .id(availabilityDto.getId())
                .reservationId(availabilityDto.getReservationId())
                .build();
    }
}
