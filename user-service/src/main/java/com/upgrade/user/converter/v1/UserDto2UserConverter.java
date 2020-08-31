package com.upgrade.user.converter.v1;

import com.upgrade.user.api.dto.v1.UserDto;
import com.upgrade.user.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDto2UserConverter implements Converter<UserDto, User> {
    @Override
    public User convert(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        return User.builder()
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .build();
    }
}
