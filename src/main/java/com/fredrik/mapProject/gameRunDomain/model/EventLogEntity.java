package com.fredrik.mapProject.gameRunDomain.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "event_log")
public class EventLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

}
