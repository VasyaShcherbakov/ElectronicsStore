package com.OnlineElectronicsStore.OnlineElectronicsStore.mapper.auth;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.UserDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.auth.AuthRegisterRequestDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.auth.AuthResponseDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.mapper.UserMapper;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Role;

public class AuthMapper {

    private final UserMapper userMapper = new UserMapper();

    /**
     * Map register request → User entity.
     * Password будет зашифрован в сервисе.
     */
    public User toUser(AuthRegisterRequestDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setGender(dto.getGender());
        user.setPassword(dto.getPassword()); // hashing later in AuthService
        user.setRole(Role.USER); // default role
        return user;
    }

}
