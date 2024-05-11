package com.fredrik.mapProject.controller.serversideController;

import com.fredrik.mapProject.model.databaseEntity.GamePlayerEntity;
import com.fredrik.mapProject.service.GamePlayerService;
import com.fredrik.mapProject.model.map.MapSizes;
import com.fredrik.mapProject.model.databaseEntity.GameSetupEntity;
import com.fredrik.mapProject.service.GameSetupService;
import com.fredrik.mapProject.model.turnChange.Hours;
import com.fredrik.mapProject.model.databaseEntity.UserEntity;
import com.fredrik.mapProject.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Controller
public class GameSetupController {

    private final GameSetupService gameSetupService;
    private final UserService userService;
    private final GamePlayerService gamePlayerService;


    @Autowired
    public GameSetupController(GameSetupService gameSetupService,
                               UserService userService,
                               GamePlayerService gamePlayerService) {
        this.gameSetupService = gameSetupService;
        this.userService = userService;
        this.gamePlayerService = gamePlayerService;
    }

    @GetMapping("/new-map")
    @PreAuthorize("hasAuthority('GET')")
    public String setupNewGamePage(GameSetupEntity gameSetup, Model model) {

        int randomSeed = new Random().nextInt(1, 100000);

        Hours[] hours = Hours.values();

        model.addAttribute("hours", hours);
        model.addAttribute("newMap", gameSetup);
        model.addAttribute("mapSizes", MapSizes.values());
        model.addAttribute("randomSeed", randomSeed);

        return "new-map";
    }

    @PostMapping("/new-map")
    @PreAuthorize("hasAuthority('POST')")
    public String setupNewGame(
            GameSetupEntity gameSetup,   // Enables Error Messages
            BindingResult result, // Ties the object with result
            MapSizes mapSize,
            String turnChange,
            String timeZone,
            int seed
    ) {

        if (result.hasErrors()) {
            return "new-map";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        Optional<UserEntity> currentUser = userService.findByUsername(username);

        if (currentUser.isEmpty()){
            return "redirect:/error-page";
        }

        gameSetup.setOwner(currentUser.get());
        gameSetup.setSeed(seed);
        gameSetup.setMapSize(mapSize);
        gameSetup.setTurnChangeFromInputString(turnChange, timeZone);
        gameSetup.setTurn(1);
        gameSetup.setUpdating(false);
        gameSetup.setStartTime(Instant.now());

        gameSetupService.createNewGameSetup(gameSetup);

        return "redirect:/my-maps";
    }

    @GetMapping("my-maps")
    @PreAuthorize("hasAuthority('GET')")
    public String showMapsPage( Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<UserEntity> currentUser = userService.findByUsername(username);

        if (currentUser.isEmpty()){
            return "redirect:/error-page";
        }
        List<GameSetupEntity> hostedMaps = currentUser.get().getGameSetups();
        List<GamePlayerEntity> gamePlayers = gamePlayerService.getAllGamePlayersByPlayer(currentUser.get().getId());

        model.addAttribute("userMaps", gamePlayers);
        model.addAttribute("userRole", currentUser.get().getRole().name());
        model.addAttribute("hostedMaps", hostedMaps);

        return "my-maps";
    }

    @GetMapping("/delete-map/{id}")
    @PreAuthorize("hasAuthority('POST')")
    public String deleteMap(@PathVariable("id") UUID mapId) {

        GameSetupEntity map = gameSetupService.findById(mapId);
        if (map == null) {
            return "error-page";
        }

        gameSetupService.delete(map);

        return "redirect:/my-maps";
    }
}
