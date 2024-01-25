package com.fredrik.mapProject.userDomain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // UserEntity could be an OPTIONAL as well if you want it to be
    UserEntity findByUsername(String username);

}
