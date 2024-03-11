package com.fredrik.mapProject.userDomain.service;

import com.fredrik.mapProject.userDomain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

        // TODO - What if we do not FIND user?

        return userRepository.findByUsername(username);
    }

}
