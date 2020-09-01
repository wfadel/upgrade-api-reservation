package com.upgrade.common.model;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.data.domain.Persistable;

@Data
@MappedSuperclass
public abstract class BaseUpgradeModel implements Persistable<UUID>, Serializable {
    @Id
    @Type(type = "pg-uuid")
    protected UUID id;

    @Override
    public boolean isNew() {
        if (id == null) {
            id = UUID.randomUUID();
            return true;
        }
        return false;
    }
}
