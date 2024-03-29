package com.fredrik.mapProject.userDomain.repository;

import com.fredrik.mapProject.userDomain.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);

    Optional<UserEntity> findById(UUID id);

}
