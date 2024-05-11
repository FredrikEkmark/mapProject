package com.fredrik.mapProject.gameRunDomain.controller;

import com.fredrik.mapProject.gameRunDomain.service.TurnChangeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TurnChangeRestController {

    private final TurnChangeService turnChangeService;


    public TurnChangeRestController(TurnChangeService turnChangeService) {
        this.turnChangeService = turnChangeService;
    }


    @GetMapping("/api/turn-change/{gameId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> changeTurn(@PathVariable("gameId") UUID gameId) {

        turnChangeService.runTurnChangeByGameId(gameId);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}

