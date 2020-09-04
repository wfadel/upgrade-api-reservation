package com.upgrade.availability.model;

import com.upgrade.common.model.BaseUpgradeModel;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "availability")
public class Availability extends BaseUpgradeModel {

    @Column(name = "day")
    private LocalDate day;

    @Column(name = "reservation_id")
    private String reservationId;

    @Version
    @Column(name = "version")
    private Long version = 0L;
}
