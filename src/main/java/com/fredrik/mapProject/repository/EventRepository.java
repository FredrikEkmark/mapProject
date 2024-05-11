package com.fredrik.mapProject.repository;

import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.databaseEntity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, UUID> {

    List<EventEntity> findAllByGameId(UUID gameId);

    List<EventEntity> findAllByGameIdAndAndPlayerNr(UUID gameId, Player playerNr);

    void deleteAllByGameId(UUID gameId);
}
