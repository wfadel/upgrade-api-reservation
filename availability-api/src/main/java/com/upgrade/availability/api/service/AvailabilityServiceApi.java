package com.upgrade.availability.api.service;

import com.upgrade.availability.api.dto.v1.AvailabilityDto;
import java.time.LocalDate;
import java.util.List;

public interface AvailabilityServiceApi {
    AvailabilityDto createAvailability(AvailabilityDto availabilityDto);

    List<AvailabilityDto> findAllAvailabilities(LocalDate startDate, LocalDate endDate);

    List<AvailabilityDto> findAvailabilities(LocalDate startDate, LocalDate endDate);

    AvailabilityDto updateAvailability(AvailabilityDto availabilityDto);

    void deleteByDay(LocalDate day);
}
