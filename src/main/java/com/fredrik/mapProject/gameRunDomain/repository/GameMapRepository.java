package com.fredrik.mapProject.gameRunDomain.repository;

import com.fredrik.mapProject.gameRunDomain.model.GameMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameMapRepository extends JpaRepository<GameMapEntity, UUID> {

}
