package com.fredrik.mapProject.gamePlayDomain.repository;

import com.fredrik.mapProject.gamePlayDomain.model.GamePlayerEntity;
import com.fredrik.mapProject.gamePlayDomain.model.PlayerGameId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface GamePlayerRepository extends JpaRepository<GamePlayerEntity, PlayerGameId> {

    Optional<GamePlayerEntity> findByPlayerGameIdGameIdAndPlayerGameIdUserId(UUID gameId, UUID userId);
}
