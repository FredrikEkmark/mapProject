package com.fredrik.mapProject.service.user;

import com.fredrik.mapProject.model.databaseEntity.UserEntity;
import com.fredrik.mapProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserEntityDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserEntityDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Goes inside database -> tries to find user (with username)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> userDetails = userRepository.findByUsername(username);

        return userDetails.orElse(null);

    }

}
