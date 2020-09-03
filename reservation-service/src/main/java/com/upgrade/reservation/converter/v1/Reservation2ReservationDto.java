package com.upgrade.reservation.converter.v1;

import com.upgrade.reservation.api.dto.v1.ReservationDto;
import com.upgrade.reservation.model.Reservation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class Reservation2ReservationDto implements Converter<Reservation, ReservationDto> {
    @Override
    public ReservationDto convert(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        return ReservationDto.builder()
                .checkInDate(reservation.getCheckInDate())
                .checkOutDate(reservation.getCheckOutDate())
                .reservationId(reservation.getId().toString())
                .build();
    }
}
