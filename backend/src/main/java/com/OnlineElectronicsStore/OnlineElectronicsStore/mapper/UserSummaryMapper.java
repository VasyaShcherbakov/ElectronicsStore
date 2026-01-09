package com.OnlineElectronicsStore.OnlineElectronicsStore.mapper;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.UserSummaryDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserSummaryMapper {

    public UserSummaryDto toDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserSummaryDto(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
