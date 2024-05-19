package com.fredrik.mapProject.service;

import com.fredrik.mapProject.model.databaseEntity.ArmyEntity;
import com.fredrik.mapProject.model.databaseEntity.RegimentEntity;
import com.fredrik.mapProject.repository.ArmyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ArmyService {

    private final ArmyRepository armyRepository;
    private final RegimentService regimentService;

    public ArmyService(ArmyRepository armyRepository, RegimentService regimentService) {
        this.armyRepository = armyRepository;
        this.regimentService = regimentService;
    }

    public List<ArmyEntity> findAllByGameID(UUID id) {
        List<ArmyEntity> armyEntityList = armyRepository.findAllByGameId(id);
        for (ArmyEntity armyEntity: armyEntityList) {
            List<RegimentEntity> regimentEntityList = regimentService.findAllByArmyId(armyEntity.getArmyId());
            armyEntity.setRegiments(regimentEntityList);
        }
        return armyEntityList;
    }

    public void updateActiveArmies(List<ArmyEntity> updatedArmies,
                                   List<ArmyEntity> removedArmies,
                                   List<RegimentEntity> removedRegiments) {
        regimentService.deleteAll(removedRegiments);
        armyRepository.deleteAll(removedArmies);
        for (ArmyEntity armyEntity: updatedArmies) {
            regimentService.updateAll(armyEntity.getRegiments());
        }
        armyRepository.saveAll(updatedArmies);
    }
}
