package com.upgrade.reservation.service;

import com.upgrade.availability.api.dto.v1.AvailabilityDto;
import com.upgrade.availability.api.service.AvailabilityServiceApi;
import com.upgrade.common.exception.NotFoundException;
import com.upgrade.common.exception.ReservationDateException;
import com.upgrade.reservation.api.dto.v1.ReservationDto;
import com.upgrade.reservation.api.service.ReservationServiceApi;
import com.upgrade.reservation.model.Reservation;
import com.upgrade.reservation.repository.ReservationRepository;
import com.upgrade.user.api.dto.v1.UserDto;
import com.upgrade.user.api.service.UserServiceApi;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
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

    @Transactional
    @Override
    public ReservationDto update(ReservationDto reservationDto) {
        validateReservationDates(reservationDto.getCheckInDate(), reservationDto.getCheckOutDate());

        Reservation reservation = getModel(reservationDto.getReservationId());
        reservation.setCheckInDate(reservationDto.getCheckInDate());
        reservation.setCheckOutDate(reservationDto.getCheckOutDate());
        Reservation dbReservation = reservationRepository.save(reservation);

        List<AvailabilityDto> availabilities = availabilityService.findByReservationId(reservationDto.getReservationId());
        availabilityService.updateAvailabilitiesReservationId(null, availabilities);

        availabilities = availabilityService.findAvailabilitiesRange(reservationDto.getCheckInDate(), reservationDto.getCheckOutDate());
        availabilityService.updateAvailabilitiesReservationId(reservationDto.getReservationId(), availabilities);

        return conversionService.convert(dbReservation, ReservationDto.class);
    }

    @Transactional
    @Override
    public ReservationDto get(String reservationId) {
        Reservation result = reservationRepository.findById(UUID.fromString(reservationId))
                .orElseThrow(() -> new NotFoundException(String.format("Resrvation not found by id %s", reservationId)));

        return conversionService.convert(getModel(reservationId), ReservationDto.class);
    }

    @Transactional
    @Override
    public void cancel(String reservationId) {
        List<AvailabilityDto> availabilities = availabilityService.findByReservationId(reservationId);
        availabilityService.updateAvailabilitiesReservationId(null, availabilities);
        reservationRepository.deleteById(UUID.fromString(reservationId));
    }

    private Reservation getModel(String reservationId) {
        return reservationRepository.findById(UUID.fromString(reservationId))
                .orElseThrow(() -> new NotFoundException(String.format("Resrvation not found by id %s", reservationId)));
    }

    private void validateReservationDates(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate.isAfter(checkOutDate)) {
            throw new ReservationDateException("Check in date must be before check out date");
        }

        if (checkInDate.isEqual(LocalDate.now()) || checkInDate.isBefore(LocalDate.now())) {
            throw new ReservationDateException("Check in date must be at least one day ahead");
        }

        if (checkInDate.isAfter(LocalDate.now().plusDays(maxLookupDays))) {
            throw new ReservationDateException(String.format("Check in date must be %s days ahead at max", maxLookupDays));
        }

        if (checkInDate.until(checkOutDate).getDays() > maxReserveDays - 1) {
            throw new ReservationDateException(String.format("The campsite can be reserved for a maximum of %s days", maxReserveDays));
        }
    }
}
