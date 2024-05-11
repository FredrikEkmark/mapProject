package com.fredrik.mapProject.repository;

import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.databaseEntity.EventLogEntity;
import com.fredrik.mapProject.model.id.EventLogId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventLogRepository extends JpaRepository<EventLogEntity, EventLogId> {

    @Transactional
    void deleteAllByEventLogId_GameId(UUID gameId);

    List<EventLogEntity> findAllByEventLogId_PlayerNrAndAndEventLogId_GameId(Player playerNr, UUID gameId);
}

