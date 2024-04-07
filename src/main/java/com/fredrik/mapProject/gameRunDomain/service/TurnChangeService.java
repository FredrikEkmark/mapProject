package com.fredrik.mapProject.gameRunDomain.service;

import org.springframework.stereotype.Service;

@Service
public class TurnChangeService {

    public void runTurnChange(int hour) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Hour must be between 0 and 23.");
        }

        // Your logic for handling the turn change at the specified hour goes here
    }
}
