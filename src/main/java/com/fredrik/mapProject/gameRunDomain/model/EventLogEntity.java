package com.fredrik.mapProject.gameRunDomain.model;

import com.fredrik.mapProject.gamePlayDomain.Player;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "event_log")
public class EventLogEntity {

    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.AUTO)
    private EventLogId eventLogId;

    private String log;
}
