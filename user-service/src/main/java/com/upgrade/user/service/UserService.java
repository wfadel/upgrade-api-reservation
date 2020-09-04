package com.upgrade.user.service;

import com.upgrade.user.api.dto.v1.UserDto;
import com.upgrade.user.api.service.UserServiceApi;
import com.upgrade.user.model.User;
import com.upgrade.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceApi {
    @Autowired
    private ConversionService conversionService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = conversionService.convert(userDto, User.class);
        userRepository.save(user);
        return conversionService.convert(user, UserDto.class);
    }

    @Override
    public UserDto get(String userId) {
        User result = userRepository.findById(Long.valueOf(userId)).orElse(null);
        return conversionService.convert(result, UserDto.class);
    }

    @Override
    public UserDto getByEmail(String email) {
        User result = userRepository.getByEmail(email);
        return conversionService.convert(result, UserDto.class);
    }

    @Override
    public UserDto getOrCreateUser(UserDto user) {
        if (user == null) {
            return null;
        }
        User result = userRepository.getByEmail(user.getEmail());
        if (result == null) {
            return create(user);
        }
        return conversionService.convert(result, UserDto.class);
    }

    @Override
    public UserDto update(String userId, UserDto userDto) {
        return null;
    }

    @Override
    public void delete(String userId) {

    }
}
