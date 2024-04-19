package com.fredrik.mapProject.gameRunDomain.controllerTemporary;

import com.fredrik.mapProject.gameRunDomain.service.TurnChangeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TurnChangeController {

    private final TurnChangeService turnChangeService;


    public TurnChangeController(TurnChangeService turnChangeService) {
        this.turnChangeService = turnChangeService;
    }

    @GetMapping("/api/turn-change")
    public boolean changeTurn() {

        turnChangeService.runTurnChange(12, 00);

        return true;
    }
}
