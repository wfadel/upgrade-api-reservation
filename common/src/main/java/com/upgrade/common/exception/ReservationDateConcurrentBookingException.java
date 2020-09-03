package com.upgrade.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ReservationDateConcurrentBookingException extends ResponseStatusException {
    public ReservationDateConcurrentBookingException() {
        super(HttpStatus.CONFLICT, "A user just reserved a period that overlaps with the desired period");
    }
}
