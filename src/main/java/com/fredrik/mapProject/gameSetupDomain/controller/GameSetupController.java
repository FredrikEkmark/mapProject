package com.fredrik.mapProject.gameSetupDomain.controller;

import com.fredrik.mapProject.gameSetupDomain.MapSizes;
import com.fredrik.mapProject.gameSetupDomain.TurnLength;
import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
import com.fredrik.mapProject.gameSetupDomain.service.GameSetupService;
import com.fredrik.mapProject.userDomain.model.UserEntity;
import com.fredrik.mapProject.userDomain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Controller
public class GameSetupController {

    private final GameSetupService gameSetupService;
    private final UserService userService;

    @Autowired
    public GameSetupController(GameSetupService gameSetupService, UserService userService) {
        this.gameSetupService = gameSetupService;
        this.userService = userService;
    }

    @GetMapping("/new-map")
    @PreAuthorize("hasAuthority('GET')")
    public String setupNewGamePage(GameSetupEntity gameSetup, Model model) {

        int randomSeed = new Random().nextInt(1, 100000);

        model.addAttribute("newMap", gameSetup);
        model.addAttribute("turnLengths", TurnLength.values());
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
            TurnLength turnLength,
            int seed
    ) {

        if (result.hasErrors()) {
            return "new-map";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        UserEntity currentUser = userService.findByUsername(username);

        gameSetup.setUser(currentUser);
        gameSetup.setSeed(seed);
        gameSetup.setMapSize(mapSize);
        gameSetup.setTurnLength(turnLength);

        gameSetupService.createNewGameSetup(gameSetup);

        return "redirect:/my-maps";
    }

    @GetMapping("my-maps")
    @PreAuthorize("hasAuthority('GET')")
    public String showMapsPage(GameSetupEntity gameSetup, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity currentUser = userService.findByUsername(username);
        List<GameSetupEntity> userMaps = currentUser.getGameSetups();

        model.addAttribute("userMaps", userMaps);

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

    @GetMapping("/myPerms")
    public ResponseEntity<String> viewPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the principal (authenticated user)
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            String authorities = userDetails.getAuthorities().toString();

            // Now you have the username of the currently logged-in user
            System.out.println("Currently logged-in user: " + username + authorities);
        } else {
            // Handle the case where the principal is not a UserDetails object
            System.out.println("Unable to determine the currently logged-in user");
        }

        // Your method logic...

        return new ResponseEntity<>("Check log", HttpStatus.ACCEPTED);
    }
}
