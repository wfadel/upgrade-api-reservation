package com.upgrade.user.converter.v1;

import com.upgrade.user.api.dto.v1.UserDto;
import com.upgrade.user.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class User2UserDtoConverter implements Converter<User, UserDto> {
    @Override
    public UserDto convert(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userId(user.getId().toString())
                .build();
    }
}
