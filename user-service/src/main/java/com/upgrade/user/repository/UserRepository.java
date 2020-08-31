package com.upgrade.user.repository;

import com.upgrade.user.model.User;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, UUID> {
    User getByEmail(String email);
}
