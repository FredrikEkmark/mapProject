package com.fredrik.mapProject.service.user;

import com.fredrik.mapProject.model.databaseEntity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class SecurityUtilityService {

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserEntity) {
            return (UserEntity) authentication.getPrincipal();
        }
        return null;
    }
}

