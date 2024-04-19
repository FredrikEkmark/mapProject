package com.fredrik.mapProject.gameRunDomain.service;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gameRunDomain.model.entity.EventEntity;
import com.fredrik.mapProject.gameRunDomain.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class EventService {
    
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void save(EventEntity eventEntity) {
        eventRepository.save(eventEntity);
    }

    public Optional<EventEntity> findByEventId(UUID eventId) {
        return eventRepository.findById(eventId);
    }

    public List<EventEntity> findAllByGameID(UUID gameId) {
        return eventRepository.findAllByGameId(gameId);
    }

    public List<EventEntity> findAllByGameIDAndPlayerNr(UUID gameId, Player playerNr) {
        return eventRepository.findAllByGameIdAndAndPlayerNr(gameId, playerNr);
    }

    public void resetEventsAndSavePersistentEvents(List<EventEntity> eventList, UUID gameId) {
        eventRepository.deleteAllByGameId(gameId);
        for (EventEntity event: eventList) {
            if (!event.getGameId().equals(gameId)) {
                System.out.println("Event " + event.getEventId() + " should be removed");
            } // toDO this logic makes no sense it should be fixed
        }
        eventRepository.saveAll(eventList);
    }

    public void deleteAllByGameId(UUID gameId) {
        eventRepository.deleteAllByGameId(gameId);
    }

    public void deleteByEventId(UUID eventId) {
        eventRepository.deleteById(eventId);

    }
}
