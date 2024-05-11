package com.fredrik.mapProject.service;

import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.databaseEntity.EventLogEntity;
import com.fredrik.mapProject.repository.EventLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventLogService {

    private final EventLogRepository eventLogRepository;

    @Autowired
    public EventLogService(EventLogRepository eventLogRepository) {
        this.eventLogRepository = eventLogRepository;
    }

    public void save(List<EventLogEntity> eventLogEntityList) {
        eventLogRepository.saveAll(eventLogEntityList);
    }

    public void deleteGameEventLog(UUID gameId) {
        eventLogRepository.deleteAllByEventLogId_GameId(gameId);
    }

    public List<EventLogEntity> findPlayerEventLog(Player playerNr, UUID gameId) {
        return eventLogRepository.findAllByEventLogId_PlayerNrAndAndEventLogId_GameId(playerNr, gameId);
    }
}
