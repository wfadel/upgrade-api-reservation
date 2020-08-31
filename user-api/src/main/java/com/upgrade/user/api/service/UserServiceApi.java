package com.upgrade.user.api.service;

import com.upgrade.user.api.dto.v1.UserDto;

public interface UserServiceApi {
    UserDto create(UserDto user);
    UserDto get(String userId);
    UserDto getByEmail(String email);
    UserDto getOrCreateUser(UserDto userDto);
    UserDto update(String userId, UserDto userDto);
    void delete(String userId);
}
