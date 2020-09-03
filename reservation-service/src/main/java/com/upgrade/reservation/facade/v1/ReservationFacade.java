package com.upgrade.reservation.facade.v1;

import com.upgrade.common.exception.ReservationDateOverlapException;
import com.upgrade.reservation.api.dto.v1.ReservationDto;
import com.upgrade.reservation.api.service.ReservationServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Component
public class ReservationFacade {
    @Autowired
    private ReservationServiceApi reservationService;

    public ReservationDto reserve(ReservationDto reservationDto) {
        try {
            return reservationService.reserve(reservationDto);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ReservationDateOverlapException();
        }
    }
}
