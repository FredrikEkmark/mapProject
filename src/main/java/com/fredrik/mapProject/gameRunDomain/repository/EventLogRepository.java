package com.fredrik.mapProject.gameRunDomain.repository;

import com.fredrik.mapProject.gamePlayDomain.model.GamePlayerEntity;
import com.fredrik.mapProject.gameRunDomain.model.EventId;
import com.fredrik.mapProject.gameRunDomain.model.EventLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventLogRepository extends JpaRepository<EventLogEntity, EventId> {

    List<EventLogEntity> findAllByEventIdGameId(UUID gameId);

    void deleteAllByEventIdGameId(UUID gameId);
}
