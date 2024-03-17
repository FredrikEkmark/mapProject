package com.fredrik.mapProject.gamePlayDomain.controller;

import com.fredrik.mapProject.gamePlayDomain.model.PlayerView;
import com.fredrik.mapProject.gamePlayDomain.service.PlayerViewService;
import com.fredrik.mapProject.gameRunDomain.service.GamePlayerViewService;
import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
import com.fredrik.mapProject.gameSetupDomain.service.GameSetupService;
import com.fredrik.mapProject.userDomain.model.UserEntity;
import com.fredrik.mapProject.userDomain.service.SecurityUtilityService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class PlayerViewController {

    private final SecurityUtilityService securityUtilityService;
    private final PlayerViewService playerViewService;
    private final GameSetupService gameSetupService;

    public PlayerViewController(SecurityUtilityService securityUtilityService, PlayerViewService playerViewService, GameSetupService gameSetupService) {
        this.securityUtilityService = securityUtilityService;
        this.playerViewService = playerViewService;
        this.gameSetupService = gameSetupService;
    }

    @GetMapping("/map/gameId={gameId}")
    @PreAuthorize("hasAuthority('GET')")
    public String setupNewGamePage(@PathVariable("gameId") UUID gameId, Model model) {

        UserEntity user = securityUtilityService.getCurrentUser();

        GameSetupEntity gameSetup = gameSetupService.findById(gameId);

        PlayerView playerView = playerViewService.getPlayerView(gameSetup, user);

        model.addAttribute("playerView", playerView);

        return "map";
    }
}
