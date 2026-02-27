package com.OnlineElectronicsStore.OnlineElectronicsStore.mapper;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.UserDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;

public class UserMapper {


    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setGender(user.getGender());
        dto.setRole(user.getRole());

        return dto;
    }


    public static User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();

        user.setId(dto.getId());   // иногда можно пропускать
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setGender(dto.getGender());
        user.setRole(dto.getRole());

        return user;
    }
}
