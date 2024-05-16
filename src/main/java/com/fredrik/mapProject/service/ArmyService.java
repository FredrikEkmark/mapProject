package com.fredrik.mapProject.service;

import com.fredrik.mapProject.model.databaseEntity.ArmyEntity;
import com.fredrik.mapProject.repository.ArmyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ArmyService {

    private final ArmyRepository armyRepository;

    public ArmyService(ArmyRepository armyRepository) {
        this.armyRepository = armyRepository;
    }

    public List<ArmyEntity> findAllByGameID(UUID id) {
        return armyRepository.findAllByGameId(id);
    }

    public void updateActiveArmies(List<ArmyEntity> updatedArmies, List<ArmyEntity> removedArmies) {
        armyRepository.deleteAll(removedArmies);
        armyRepository.saveAll(updatedArmies);
    }
}
