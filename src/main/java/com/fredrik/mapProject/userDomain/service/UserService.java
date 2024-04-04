package com.fredrik.mapProject.userDomain.service;


import com.fredrik.mapProject.config.AppPasswordConfig;
import com.fredrik.mapProject.userDomain.model.UserEntity;
import com.fredrik.mapProject.userDomain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository, AppPasswordConfig appPasswordConfig) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> findById(UUID id) {
        return userRepository.findById(id);
    }

    public void createNewUser(UserEntity userEntity) {

        userEntity.setAccountEnabled(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setAccountNonExpired(true);
        userEntity.setCredentialsNonExpired(true);

        userRepository.save(userEntity);
    }

    public void updateUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public void deleteUser(UserEntity userEntity) {userRepository.delete(userEntity);}

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
