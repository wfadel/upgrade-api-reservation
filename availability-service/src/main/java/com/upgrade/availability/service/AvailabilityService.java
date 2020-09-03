package com.upgrade.availability.service;

import com.upgrade.availability.api.dto.v1.AvailabilityDto;
import com.upgrade.availability.api.service.AvailabilityServiceApi;
import com.upgrade.availability.model.Availability;
import com.upgrade.availability.repository.AvailabilityRepository;
import com.upgrade.common.exception.ReservationDateException;
import com.upgrade.common.exception.ReservationDateOverlapException;
import com.upgrade.common.util.ConversionUtil;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AvailabilityService implements AvailabilityServiceApi {
    @Value("${upgrade.reservation.maxLookupDays}")
    private int maxLookupDays;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private ConversionUtil conversionUtil;

    @Autowired
    private AvailabilityRepository availabilityRepository;


    @Transactional
    @Override
    public AvailabilityDto createAvailability(AvailabilityDto availabilityDto) {
        Availability availability = conversionService.convert(availabilityDto, Availability.class);
        availability = availabilityRepository.save(availability);
        return conversionService.convert(availability, AvailabilityDto.class);
    }

    @Transactional
    @Override
    public List<AvailabilityDto> findAllAvailabilities(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            // TODO throw proper UpgradeException
            throw new ReservationDateException("Start date must be before end date");
        }

        List<Availability> result = availabilityRepository.findAllByDayGreaterThanEqualAndDayLessThanEqualOrderByDay(startDate, endDate);
        return conversionUtil.convert(result, AvailabilityDto.class);
    }

    @Transactional
    @Override
    public List<AvailabilityDto> findAvailabilities(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            // TODO throw proper UpgradeException
            throw new RuntimeException("Start date should be before end date");
        }


        List<Availability> result = availabilityRepository.findAvailabilities(startDate, endDate);
        return conversionUtil.convert(result, AvailabilityDto.class);
    }

    @Transactional
    @Override
    public AvailabilityDto updateAvailability(AvailabilityDto availabilityDto) {
        Availability availability = availabilityRepository.findById(availabilityDto.getId()).get();
        availability.setReservationId(availabilityDto.getReservationId());
        availability = availabilityRepository.save(availability);
        return conversionService.convert(availability, AvailabilityDto.class);
    }

    @Transactional
    @Override
    public void deleteByDay(LocalDate day) {
        availabilityRepository.deleteByDay(day);
    }

    @Transactional
    @Override
    public void updateAvailabilitiesReservationId(String reservationId, List<AvailabilityDto> availabilities) {
        CollectionUtils.emptyIfNull(availabilities).forEach(availability -> {
            availability.setReservationId(reservationId);
            updateAvailability(availability);
        });
    }

    @Transactional
    @Override
    public List<AvailabilityDto> findAvailabilitiesRange(LocalDate startDate, LocalDate endDate) {
        List<AvailabilityDto> result = findAllAvailabilities(startDate, endDate);
        result = CollectionUtils.emptyIfNull(result).stream()
                .filter(availabilityDto -> StringUtils.isEmpty(availabilityDto.getReservationId())).collect(Collectors.toList());
        // validate that all the days are still available
        long numberOfDaysInTheRange = ChronoUnit.DAYS.between(startDate, endDate.plusDays(1));
        if (result.size() != numberOfDaysInTheRange) {
            throw new ReservationDateOverlapException();
        }

        return result;
    }

    @Transactional
    @Override
    public List<AvailabilityDto> findByReservationId(String reservationId) {
        List<Availability> result = availabilityRepository.findByReservationId(reservationId);
        return conversionUtil.convert(result, AvailabilityDto.class);
    }
}
