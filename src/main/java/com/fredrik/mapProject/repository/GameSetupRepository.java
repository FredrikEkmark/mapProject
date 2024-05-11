package com.fredrik.mapProject.repository;

import com.fredrik.mapProject.model.databaseEntity.GameSetupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GameSetupRepository extends JpaRepository<GameSetupEntity, UUID> {

    @Query("SELECT g FROM GameSetupEntity g WHERE SUBSTRING(g.turnChange, :hour + 1, 1) = '1' AND g.turnChange LIKE CONCAT('%:', :minute)")
    List<GameSetupEntity> findAllByTurnChange(@Param("hour") int hour, @Param("minute") String minute);

}
