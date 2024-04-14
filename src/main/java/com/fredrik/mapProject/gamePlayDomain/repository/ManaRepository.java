package com.fredrik.mapProject.gamePlayDomain.repository;

import com.fredrik.mapProject.gamePlayDomain.model.GamePlayerEntity;
import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gamePlayDomain.model.PlayerGameId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ManaRepository extends JpaRepository<ManaEntity, UUID> {

    void deleteAllByGameId(UUID gameId);

}
