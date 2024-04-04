package com.fredrik.mapProject.gamePlayDomain.repository;

import com.fredrik.mapProject.gamePlayDomain.model.GamePlayerEntity;
import com.fredrik.mapProject.gamePlayDomain.model.PlayerGameId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GamePlayerRepository extends JpaRepository<GamePlayerEntity, PlayerGameId> {

    Optional<GamePlayerEntity> findByPlayerGameIdGameIdAndPlayerGameIdUserId(UUID gameId, UUID userId);

    List<GamePlayerEntity> findAllByPlayerGameIdGameId(UUID gameId);

    @Modifying
    @Query(value = "DELETE FROM game_player WHERE game_id = :gameId", nativeQuery = true)
    void deleteAllByGameId(@Param("gameId") UUID gameId);
}
