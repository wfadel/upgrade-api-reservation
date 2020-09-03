package com.upgrade.availability.job;

import com.upgrade.availability.api.dto.v1.AvailabilityDto;
import com.upgrade.availability.api.service.AvailabilityServiceApi;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class AvailabilityJob {
    @Value("${upgrade.reservation.maxLookupDays}")
    private int maxLookupDays;

    @Autowired
    private AvailabilityServiceApi availabilityService;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateAvailabilities() {
        LocalDate day = LocalDate.now();
        // delete today since users cannot book at the same day
        log.info("Deleted expired availability {}", day);
        availabilityService.deleteByDay(day);
        // add a day to the end of the days list to be able to show availabilities within a month
        LocalDate dayToAdd = day.plusDays(maxLookupDays);
        availabilityService.createAvailability(AvailabilityDto.builder().day(dayToAdd).build());
        log.info("Added new availability {}", dayToAdd);
    }
}
