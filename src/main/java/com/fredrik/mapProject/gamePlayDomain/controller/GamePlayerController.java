package com.fredrik.mapProject.gamePlayDomain.controller;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gamePlayDomain.model.GamePlayerEntity;
import com.fredrik.mapProject.gamePlayDomain.service.GamePlayerService;
import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
import com.fredrik.mapProject.gameSetupDomain.service.GameSetupService;
import com.fredrik.mapProject.userDomain.model.UserEntity;
import com.fredrik.mapProject.userDomain.service.SecurityUtilityService;
import com.fredrik.mapProject.userDomain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class GamePlayerController {

    private final SecurityUtilityService securityUtilityService;
    private final GamePlayerService gamePlayerService;
    private final GameSetupService gameSetupService;

    private final UserService userService;

    @Autowired
    public GamePlayerController(SecurityUtilityService securityUtilityService, GamePlayerService gamePlayerService, GameSetupService gameSetupService, UserService userService) {
        this.securityUtilityService = securityUtilityService;
        this.gamePlayerService = gamePlayerService;
        this.gameSetupService = gameSetupService;
        this.userService = userService;
    }

    @GetMapping("/manage-map-players/{gameId}")
    @PreAuthorize("hasAuthority('GET')")
    public String manageMapPlayers(@PathVariable("gameId") UUID gameId, Model model) {
        List<GamePlayerEntity> gamePlayers = gamePlayerService.getAllGamePlayersByGame(gameId);

        List<String> usernames = new ArrayList<>();
        for (GamePlayerEntity gamePlayer : gamePlayers) {
            Optional<UserEntity> userEntity = userService.findById(gamePlayer.getPlayerGameId().getUserId());
            if (userEntity.isPresent()) {
                usernames.add(userEntity.get().getUsername());
            } else {
                usernames.add("Unknown"); // Or any other default value
            }
        }
        model.addAttribute("gameId", gameId);
        model.addAttribute("usernames", usernames);
        model.addAttribute("gamePlayers", gamePlayers);
        return "manage-map-players";
    }

    @PostMapping("/invite-player/{gameId}")
    @PreAuthorize("hasAuthority('POST')")
    public String inviteNewPlayer(String username,@PathVariable("gameId") UUID gameId, Model model) {

        Optional<UserEntity> user = userService.findByUsername(username);

        if (user.isEmpty()) {
            System.out.println("User not found");
            return "redirect:/manage-map-players/" + gameId;
        }

        GameSetupEntity gameSetup = gameSetupService.findById(gameId);

        gamePlayerService.createNewGamePlayer(gameSetup, user.get().getId());

        return "redirect:/manage-map-players/" + gameId;
    }
}
