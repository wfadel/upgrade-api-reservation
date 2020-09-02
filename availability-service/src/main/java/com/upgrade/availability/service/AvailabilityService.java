package com.upgrade.availability.service;

import com.upgrade.availability.api.dto.v1.AvailabilityDto;
import com.upgrade.availability.api.service.AvailabilityServiceApi;
import com.upgrade.availability.model.Availability;
import com.upgrade.availability.repository.AvailabilityRepository;
import com.upgrade.common.util.ConversionUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AvailabilityService implements AvailabilityServiceApi {

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
            throw new RuntimeException("Start date should be before end date");
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
}