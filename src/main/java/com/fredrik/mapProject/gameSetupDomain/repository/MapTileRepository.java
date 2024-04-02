package com.fredrik.mapProject.gameSetupDomain.repository;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface MapTileRepository extends JpaRepository<MapTileEntity, MapTileId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM MapTileEntity m WHERE m.id.gameId = :gameId")
    void deleteByGameId(UUID gameId);

    @Query("SELECT m FROM MapTileEntity m WHERE m.id.gameId = :gameId")
    List<MapTileEntity> findByGameId(UUID gameId);

    Optional<MapTileEntity> findFirstByTileOwner(Player player);

}
