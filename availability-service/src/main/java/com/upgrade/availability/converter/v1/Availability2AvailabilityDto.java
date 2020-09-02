package com.upgrade.availability.converter.v1;

import com.upgrade.availability.api.dto.v1.AvailabilityDto;
import com.upgrade.availability.model.Availability;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class Availability2AvailabilityDto implements Converter<Availability, AvailabilityDto> {
    @Override
    public AvailabilityDto convert(Availability availability) {
        if (availability == null) {
            return null;
        }

        return AvailabilityDto.builder()
                .day(availability.getDay())
                .id(availability.getId())
                .reservationId(availability.getReservationId())
                .build();
    }
}
