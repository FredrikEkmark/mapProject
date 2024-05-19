package com.fredrik.mapProject.service;

import com.fredrik.mapProject.model.databaseEntity.RegimentEntity;
import com.fredrik.mapProject.repository.RegimentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RegimentService {

    private  final RegimentRepository regimentRepository;

    public RegimentService(RegimentRepository regimentRepository) {
        this.regimentRepository = regimentRepository;
    }

    public List<RegimentEntity> findAllByArmyId(UUID armyId) {
        return regimentRepository.findAllByArmyId(armyId);
    }

    public void updateAll(List<RegimentEntity> regiments) {
        regimentRepository.saveAll(regiments);
    }

    public void deleteAll(List<RegimentEntity> removedRegiments) {
        regimentRepository.deleteAll(removedRegiments);
    }
}
