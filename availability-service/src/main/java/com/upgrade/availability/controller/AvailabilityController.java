package com.upgrade.availability.controller;

import com.upgrade.availability.api.dto.v1.AvailabilityDto;
import com.upgrade.availability.api.service.AvailabilityServiceApi;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/availabilities")
public class AvailabilityController {
    @Value("${upgrade.reservation.maxLookupDays}")
    private int maxLookupDays;

    @Autowired
    private AvailabilityServiceApi availabilityService;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping
    public ResponseEntity<List<AvailabilityDto>> findAvailabilities(@RequestParam(value = "start_date") String startDateStr,
                                                                    @RequestParam(value = "end_date", required = false) String endDateStr) {
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = StringUtils.isEmpty(endDateStr) ? startDate.plusDays(maxLookupDays) :
                LocalDate.parse(endDateStr, formatter);
        return ResponseEntity.ok(availabilityService.findAvailabilities(startDate, endDate));
    }
}
