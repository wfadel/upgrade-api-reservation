package com.upgrade.reservation.service;

import com.upgrade.availability.api.dto.v1.AvailabilityDto;
import com.upgrade.availability.api.service.AvailabilityServiceApi;
import com.upgrade.reservation.api.dto.v1.ReservationDto;
import com.upgrade.reservation.api.service.ReservationServiceApi;
import com.upgrade.reservation.model.Reservation;
import com.upgrade.reservation.repository.ReservationRepository;
import com.upgrade.user.api.dto.v1.UserDto;
import com.upgrade.user.api.service.UserServiceApi;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService implements ReservationServiceApi {
    @Value("${upgrade.reservation.maxLookupDays}")
    private int maxLookupDays;

    @Value("${upgrade.reservation.maxReserveDays}")
    private int maxReserveDays;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserServiceApi userServiceApi;

    @Autowired
    private AvailabilityServiceApi availabilityService;

    @Transactional
    @Override
    public ReservationDto reserve(ReservationDto reservationDto) {
        validateReservationDates(reservationDto.getCheckInDate(), reservationDto.getCheckOutDate());

        UserDto userDto = userServiceApi.getOrCreateUser(reservationDto.getUser());

        Reservation reservation = conversionService.convert(reservationDto, Reservation.class);
        reservation.setUserId(userDto.getUserId());
        Reservation dbReservation = reservationRepository.save(reservation);
        ReservationDto result = conversionService.convert(dbReservation, ReservationDto.class);
        result.setUser(userServiceApi.get(dbReservation.getUserId()));
        List<AvailabilityDto> availabilities = availabilityService.findAvailabilitiesRange(reservationDto.getCheckInDate(), reservationDto.getCheckOutDate());
        availabilityService.updateAvailabilitiesReservationId(result.getReservationId(), availabilities);
        return result;
    }

    private void validateReservationDates(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate.isAfter(checkOutDate)) {
            throw new RuntimeException();
        }

        if (checkInDate.isEqual(LocalDate.now()) || checkInDate.isBefore(LocalDate.now())) {
            throw new RuntimeException();
        }

        if (checkInDate.isAfter(LocalDate.now().plusDays(maxLookupDays))) {
            throw new RuntimeException();
        }

        if (checkInDate.until(checkOutDate).getDays() > maxReserveDays) {
            throw new RuntimeException();
        }
    }
}
