package com.fredrik.mapProject.repository;

import com.fredrik.mapProject.model.databaseEntity.ArmyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArmyRepository extends JpaRepository<ArmyEntity, UUID> {

    List<ArmyEntity> findAllByGameId(UUID gameId);
}
