package com.upgrade.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ReservationDateOverlapException extends ResponseStatusException {
    public ReservationDateOverlapException() {
        // TODO the message (along with other messages) should be localized
        super(HttpStatus.CONFLICT, "Reservation period overlaps with a reserved period");
    }
}
