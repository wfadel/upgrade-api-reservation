package com.upgrade.availability.repository;

import com.upgrade.availability.model.Availability;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AvailabilityRepository extends CrudRepository<Availability, Long> {
    List<Availability> findAllByDayGreaterThanEqualAndDayLessThanEqualOrderByDay(LocalDate startDate, LocalDate endDate);

    @Query(value = "FROM Availability a WHERE a.day >= ?1 AND a.day <= ?2 ORDER BY a.day")
    List<Availability> findAvailabilities(LocalDate startDate, LocalDate endDate);

    void deleteByDay(LocalDate day);
}
