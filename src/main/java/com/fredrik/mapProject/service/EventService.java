package com.fredrik.mapProject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.databaseEntity.EventEntity;
import com.fredrik.mapProject.model.event.Event;
import com.fredrik.mapProject.model.event.EventType;
import com.fredrik.mapProject.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
        if (validateEventEntity(eventEntity)) {
            eventRepository.save(eventEntity);
        }
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
        System.out.println(eventList.size());
        eventRepository.deleteAllByGameId(gameId);
        if (eventList.size() > 0) {
        eventRepository.saveAll(eventList);
        }
    }

    public void deleteAllByGameId(UUID gameId) {
        eventRepository.deleteAllByGameId(gameId);
    }

    public void deleteByEventId(UUID eventId) {
        eventRepository.deleteById(eventId);

    }

    private boolean validateEventEntity(EventEntity eventEntity) {
        Event event = eventEntity.getEvent();
        EventType eventType = event.getEventType();

        boolean validEventType = Arrays.stream(EventType.values()).toList().contains(eventType);

        boolean validCost = validateEventManpowerCost(eventType, eventEntity);

        return (validEventType && validCost);
    }

    private boolean validateEventManpowerCost(EventType eventType, EventEntity eventEntity)  {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(eventEntity.getCost());
            JsonNode manpowerNode = rootNode.get("manpower");

            switch (eventType) {
                case EXPLORE_EVENT -> {
                    if (manpowerNode.asInt() == 50) {
                        return true;
                    }
                }
                case CLAIM_TILE_EVENT -> {
                    if (manpowerNode.asInt() == 100) {
                        return true;
                    }
                }
                case BUILD_EVENT -> {
                    if (manpowerNode.asInt() <= 200) {
                        return true;
                    }
                }
                default -> {
                    return false;
                }
            }

        }  catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
