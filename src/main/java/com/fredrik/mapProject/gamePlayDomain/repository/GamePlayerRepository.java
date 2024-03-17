package com.fredrik.mapProject.gamePlayDomain.repository;

import com.fredrik.mapProject.gamePlayDomain.model.GamePlayerEntity;
import com.fredrik.mapProject.gamePlayDomain.model.PlayerGameId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamePlayerRepository extends JpaRepository<GamePlayerEntity, PlayerGameId> {
}
