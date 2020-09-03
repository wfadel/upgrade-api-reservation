package com.upgrade.reservation.converter.v1;

import com.upgrade.reservation.api.dto.v1.ReservationDto;
import com.upgrade.reservation.model.Reservation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReservationDto2Reservation implements Converter<ReservationDto, Reservation> {
    @Override
    public Reservation convert(ReservationDto reservationDto) {
        if (reservationDto == null) {
            return null;
        }
        return Reservation.builder()
                .checkInDate(reservationDto.getCheckInDate())
                .checkOutDate(reservationDto.getCheckOutDate())
                .build();
    }
}
