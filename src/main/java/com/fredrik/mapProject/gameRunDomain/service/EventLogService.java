package com.fredrik.mapProject.gameRunDomain.service;

import com.fredrik.mapProject.gameRunDomain.model.EventLogEntity;
import com.fredrik.mapProject.gameRunDomain.repository.EventLogRepository;
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

    public List<EventLogEntity> findAllByGameID(UUID gameId) {
        return eventLogRepository.findAllByEventIdGameId(gameId);
    }

    public void resetEventLogAndSavePersistentEvents(List<EventLogEntity> eventLogList, UUID gameId) {
        eventLogRepository.deleteAllByEventIdGameId(gameId);
        for (EventLogEntity event: eventLogList) {
            if (event.getEventId().getGameId() != gameId) {
                eventLogList.remove(event);
                System.out.println("Event " + event.getEventId() + " was removed");
            }
        }
        eventLogRepository.saveAll(eventLogList);
    }

    public void deleteAllByGameId(UUID gameId) {
        eventLogRepository.deleteAllByEventIdGameId(gameId);
    }
}
