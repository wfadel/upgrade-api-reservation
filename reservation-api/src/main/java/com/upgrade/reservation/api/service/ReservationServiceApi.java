package com.upgrade.reservation.api.service;

import com.upgrade.reservation.api.dto.v1.ReservationDto;

public interface ReservationServiceApi {
    ReservationDto reserve(ReservationDto reservationDto);

    ReservationDto get(String reservationId);

    ReservationDto update(ReservationDto reservationDto);

    void cancel(String reservationId);
}
