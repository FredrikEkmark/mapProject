package com.fredrik.mapProject.repository;

import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.databaseEntity.MapTileEntity;
import com.fredrik.mapProject.model.id.MapTileId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    Optional<MapTileEntity> findByMapTileId(MapTileId mapTileId);

    @Query("SELECT m FROM MapTileEntity m WHERE m.mapTileId.gameId = :gameId AND SUBSTRING(m.visibility, :playerNumber, 1) = '1'")
    List<MapTileEntity> findByGameIdAndPlayerVisibility(@Param("gameId") UUID gameId, @Param("playerNumber") int playerNumber);

    @Query("SELECT m FROM MapTileEntity m WHERE m.id.gameId = :gameId AND m.tileOwner = :player")
    Optional<MapTileEntity> findFirstByTileOwnerAndGameId(Player player, UUID gameId);

    @Transactional
    @Modifying
    @Query("UPDATE MapTileEntity m SET m.tileValue = :tileValue, m.tileOwner = :tileOwner, m.visibility = :visibility, m.building = :building, m.unit = :unit WHERE m.mapTileId = :mapTileId")
    void updateMapTileEntityByMapTileId(@Param("mapTileId") MapTileId mapTileId,
                                        @Param("tileValue") int tileValue,
                                        @Param("tileOwner") Player tileOwner,
                                        @Param("visibility") String visibility,
                                        @Param("building") String building,
                                        @Param("unit") String unit);

    @Transactional
    @Modifying
    default void updateMapTileEntities(List<MapTileEntity> mapTileEntities) {
        saveAll(mapTileEntities);
    }

}
