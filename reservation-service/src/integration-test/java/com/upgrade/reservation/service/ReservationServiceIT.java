package com.upgrade.reservation.service;

import com.upgrade.availability.model.Availability;
import com.upgrade.availability.repository.AvailabilityRepository;
import com.upgrade.reservation.BaseTestIT;
import com.upgrade.reservation.api.dto.v1.ReservationDto;
import com.upgrade.reservation.api.service.ReservationServiceApi;
import com.upgrade.user.api.dto.v1.UserDto;
import java.time.LocalDate;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class ReservationServiceIT extends BaseTestIT {

    @Autowired
    private ReservationServiceApi reservationService;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test(expected = ObjectOptimisticLockingFailureException.class)
    public void concurrentReserve() {

        // first transaction will internally get 3 availability records between 3 and 5 days from now
        // those records are versioned so when they are updated in this transaction the version will be increased
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        ReservationDto actual = reservationService.reserve(ReservationDto.builder()
                .checkInDate(LocalDate.now().plusDays(3))
                .checkOutDate(LocalDate.now().plusDays(5))
                .user(UserDto.builder()
                        .email("john.doe@gmail.com")
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .build());

        Assert.assertNotNull(actual.getReservationId());
        Assert.assertEquals(LocalDate.now().plusDays(3), actual.getCheckInDate());
        Assert.assertEquals(LocalDate.now().plusDays(5), actual.getCheckOutDate());
        transactionManager.commit(status);

        List<Availability> availabilities = availabilityRepository.findByReservationId(actual.getReservationId());

        status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        Availability availability = availabilities.get(0);
        // setting the version to a previous value would emulate a concurrent transaction that retrieved the same
        // availability records but tried to update after they were updated by the first transaction
        // this will cause ObjectOptimisticLockingFailureException
        availability.setVersion(0L);
        availability.setReservationId("2");
        availabilityRepository.save(availability);
        transactionManager.commit(status);
    }
}
