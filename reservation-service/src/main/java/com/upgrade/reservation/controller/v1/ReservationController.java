package com.upgrade.reservation.controller.v1;

import com.upgrade.reservation.api.dto.v1.ReservationDto;
import com.upgrade.reservation.facade.v1.ReservationFacade;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@EnableWebMvc
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    @Autowired
    private ReservationFacade reservationFacade;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDto> reserve(@Valid @RequestBody ReservationDto reservationDto) {
        ReservationDto result = reservationFacade.reserve(reservationDto);
        return ResponseEntity.created(URI.create(String.format("/api/v1/reservation/%s",
                reservationDto.getReservationId()))).body(result);
    }

    @GetMapping(value = "/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDto> get(@PathVariable("reservationId") String reservationId) {
        return ResponseEntity.ok(reservationFacade.get(reservationId));
    }

    @PutMapping(value = "/{reservationId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDto> update(@PathVariable("reservationId") String reservationId, @RequestBody ReservationDto reservationDto) {
        return ResponseEntity.ok(reservationFacade.update(reservationId, reservationDto));
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity cancel(@PathVariable("reservationId") String reservationId) {
        reservationFacade.cancel(reservationId);
        return ResponseEntity.noContent().build();
    }
}
