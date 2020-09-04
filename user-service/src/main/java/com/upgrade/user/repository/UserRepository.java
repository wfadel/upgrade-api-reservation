package com.upgrade.user.repository;

import com.upgrade.user.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User getByEmail(String email);
}
