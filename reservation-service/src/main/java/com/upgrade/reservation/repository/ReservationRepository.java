package com.upgrade.reservation.repository;

import com.upgrade.reservation.model.Reservation;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
}
