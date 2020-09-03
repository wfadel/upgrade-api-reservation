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
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService implements ReservationServiceApi {

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
        UserDto userDto = userServiceApi.getOrCreateUser(reservationDto.getUser());

        Reservation reservation = conversionService.convert(reservationDto, Reservation.class);
        reservation.setUserId(userDto.getUserId());
        Reservation dbReservation = reservationRepository.save(reservation);
        ReservationDto result = conversionService.convert(dbReservation, ReservationDto.class);
        result.setUser(userServiceApi.get(dbReservation.getUserId()));
        List<AvailabilityDto> availabilities = findAvailabilities(reservationDto.getCheckInDate(), reservationDto.getCheckOutDate());
        updateAvailabilities(result.getReservationId(), availabilities);
        return result;
    }

    private void updateAvailabilities(String reservationId, List<AvailabilityDto> availabilities) {
        CollectionUtils.emptyIfNull(availabilities).forEach(availability -> {
            availability.setReservationId(reservationId);
            availabilityService.updateAvailability(availability);
        });
    }

    private List<AvailabilityDto> findAvailabilities(LocalDate startDate, LocalDate endDate) {
        List<AvailabilityDto> result = availabilityService.findAllAvailabilities(startDate, endDate);
        // validate that all the days are still available
        long numberOfDaysInTheRange = ChronoUnit.DAYS.between(startDate, endDate.plusDays(1));
        if (result.size() != numberOfDaysInTheRange) {
            // TODO throw proper UpgradeException
            throw new RuntimeException();
        }

        return result;
    }
}
