package com.fredrik.mapProject.repository;

import com.fredrik.mapProject.model.databaseEntity.RegimentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RegimentRepository extends JpaRepository<RegimentEntity, UUID> {
    List<RegimentEntity> findAllByArmyId(UUID armyId);
}
