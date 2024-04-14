package com.fredrik.mapProject.gameRunDomain.repository;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gameRunDomain.model.EventLogEntity;
import com.fredrik.mapProject.gameRunDomain.model.EventLogId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventLogRepository extends JpaRepository<EventLogEntity, EventLogId> {

    void deleteAllByEventLogId_GameId(UUID gameId);

    List<EventLogEntity> findAllByEventLogId_PlayerNrAndAndEventLogId_GameId(Player playerNr, UUID gameId);
}

