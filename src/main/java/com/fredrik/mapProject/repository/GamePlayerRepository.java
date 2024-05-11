package com.fredrik.mapProject.repository;

import com.fredrik.mapProject.model.databaseEntity.GamePlayerEntity;
import com.fredrik.mapProject.model.id.PlayerGameId;
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

    List<GamePlayerEntity> findAllByPlayerGameIdUserId(UUID gameId);

    @Modifying
    @Query(value = "DELETE FROM game_player WHERE game_id = :gameId", nativeQuery = true)
    void deleteAllByGameId(@Param("gameId") UUID gameId);

    @Query("SELECT u.username FROM UserEntity u WHERE u.id = :userId")
    Optional<String> findUsernameByUserId(@Param("userId") UUID userId);
}
