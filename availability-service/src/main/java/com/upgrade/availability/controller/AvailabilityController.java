package com.upgrade.availability.controller;

import com.upgrade.availability.api.dto.v1.AvailabilityDto;
import com.upgrade.availability.api.service.AvailabilityServiceApi;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/availabilities")
public class AvailabilityController {

    @Autowired
    private AvailabilityServiceApi availabilityService;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @GetMapping
    public ResponseEntity<List<AvailabilityDto>> findAvailabilities(@RequestParam(value = "start_date") String startDate,
                                                                    @RequestParam(value = "end_date", required = false) String endDate) {
        return ResponseEntity.ok(availabilityService.findAvailabilities(LocalDate.parse(startDate, formatter),
                LocalDate.parse(endDate, formatter)));
    }
}
