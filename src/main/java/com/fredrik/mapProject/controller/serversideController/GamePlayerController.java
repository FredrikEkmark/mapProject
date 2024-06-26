package com.fredrik.mapProject.controller.serversideController;

import com.fredrik.mapProject.model.databaseEntity.GamePlayerEntity;
import com.fredrik.mapProject.model.id.PlayerGameId;
import com.fredrik.mapProject.service.GamePlayerService;
import com.fredrik.mapProject.model.databaseEntity.GameSetupEntity;
import com.fredrik.mapProject.service.GameSetupService;
import com.fredrik.mapProject.model.databaseEntity.UserEntity;
import com.fredrik.mapProject.service.user.SecurityUtilityService;
import com.fredrik.mapProject.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    public GamePlayerController(SecurityUtilityService securityUtilityService,
                                GamePlayerService gamePlayerService,
                                GameSetupService gameSetupService,
                                UserService userService) {
        this.securityUtilityService = securityUtilityService;
        this.gamePlayerService = gamePlayerService;
        this.gameSetupService = gameSetupService;
        this.userService = userService;
    }

    @GetMapping("/manage-map-players/{gameId}")
    @PreAuthorize("hasAuthority('GET')")
    public String manageMapPlayers(@PathVariable("gameId") UUID gameId, Model model) {
        List<GamePlayerEntity> gamePlayers = gamePlayerService.getAllGamePlayersByGame(gameId);

        for (GamePlayerEntity gamePlayer : gamePlayers) {
            Optional<UserEntity> userEntity = userService.findById(gamePlayer.getPlayerGameId().getUserId());
            userEntity.ifPresent(entity -> gamePlayer.setUsername(entity.getUsername()));
        }
        model.addAttribute("gameId", gameId);
        model.addAttribute("gamePlayers", gamePlayers);
        return "manage-map-players";
    }

    @PostMapping("/invite-player/{gameId}")
    @PreAuthorize("hasAuthority('POST')")
    public String inviteNewPlayer(String username,@PathVariable("gameId") UUID gameId) {

        Optional<UserEntity> user = userService.findByUsername(username);

        if (user.isEmpty()) {
            System.out.println("User not found");
            return "redirect:/manage-map-players/" + gameId;
        }

        GameSetupEntity gameSetup = gameSetupService.findById(gameId);

        gamePlayerService.createNewGamePlayer(gameSetup, user.get().getId());

        return "redirect:/manage-map-players/" + gameId;
    }

    @GetMapping("/delete-game-player/{gameId}/{playerId}")
    @PreAuthorize("hasAuthority('GET')")
    public String deletePlayer(@PathVariable("gameId") UUID gameId, @PathVariable("playerId") UUID playerId ) {

        UserEntity user = securityUtilityService.getCurrentUser();
        GameSetupEntity gameSetup = gameSetupService.findById(gameId);

        Optional<GamePlayerEntity> gamePlayer = Optional.empty();

        if (user.getId().equals(gameSetup.getOwner().getId())) {
            gamePlayer = gamePlayerService.getGamePlayer(new PlayerGameId(gameId, playerId));
        }

        gamePlayer.ifPresent(gamePlayerService::deleteGamePlayerById);

        return "redirect:/manage-map-players/" + gameId;
    }
}
