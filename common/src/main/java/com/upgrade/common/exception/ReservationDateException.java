package com.upgrade.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ReservationDateException extends ResponseStatusException {
    public ReservationDateException(String msg) {
        super(HttpStatus.BAD_REQUEST, msg);
    }
}
