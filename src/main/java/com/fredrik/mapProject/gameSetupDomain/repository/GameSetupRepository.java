package com.fredrik.mapProject.gameSetupDomain.repository;

import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameSetupRepository extends JpaRepository<GameSetupEntity, UUID> {
}
