package com.fredrik.mapProject.gamePlayDomain.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fredrik.mapProject.gamePlayDomain.model.PlayerView;
import com.fredrik.mapProject.gamePlayDomain.service.PlayerViewService;
import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
import com.fredrik.mapProject.gameSetupDomain.service.GameSetupService;
import com.fredrik.mapProject.userDomain.model.UserEntity;
import com.fredrik.mapProject.userDomain.service.SecurityUtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class PlayerViewRestController {

    private final SecurityUtilityService securityUtilityService;
    private final PlayerViewService playerViewService;
    private final GameSetupService gameSetupService;

    @Autowired
    public PlayerViewRestController(SecurityUtilityService securityUtilityService, PlayerViewService playerViewService, GameSetupService gameSetupService) {
        this.securityUtilityService = securityUtilityService;
        this.playerViewService = playerViewService;
        this.gameSetupService = gameSetupService;
    }

    @GetMapping("api/test/{gameId}")
    public PlayerView testApi(@PathVariable("gameId") UUID gameId) throws JsonProcessingException {

        UserEntity user = new UserEntity(); // toDO replace with = securityUtilityService.getCurrentUser();

        String uuidString = "efa2f87e-4d48-482e-99eb-754495bcceda"; // toDo delete when auth is ok
        UUID uuid = UUID.fromString(uuidString); // toDo delete when auth is ok
        user.setUsername("Test user"); user.setId(uuid);// toDo delete when auth is ok

        System.out.println(user.getId());

        GameSetupEntity gameSetup = gameSetupService.findById(gameId);

        PlayerView playerView = playerViewService.getPlayerView(gameSetup, user);

        System.out.println(playerView);

        return playerView;
    }
}