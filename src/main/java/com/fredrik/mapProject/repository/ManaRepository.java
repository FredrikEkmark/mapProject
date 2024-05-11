package com.fredrik.mapProject.repository;

import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ManaRepository extends JpaRepository<ManaEntity, UUID> {

    void deleteAllByGameId(UUID gameId);

    List<ManaEntity> findAllByGameId(UUID gameId);

}
