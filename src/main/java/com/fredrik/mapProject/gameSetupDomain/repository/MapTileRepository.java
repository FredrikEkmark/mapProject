package com.fredrik.mapProject.gameSetupDomain.repository;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @Query("SELECT m FROM MapTileEntity m WHERE m.mapTileId.gameId = :gameId AND SUBSTRING(m.visibility, :playerNumber, 1) = '1'")
    List<MapTileEntity> findByGameIdAndPlayerVisibility(@Param("gameId") UUID gameId, @Param("playerNumber") int playerNumber);

    @Query("SELECT m FROM MapTileEntity m WHERE m.id.gameId = :gameId AND m.tileOwner = :player")
    Optional<MapTileEntity> findFirstByTileOwnerAndGameId(Player player, UUID gameId);

    @Transactional
    @Modifying
    @Query("UPDATE MapTileEntity m SET m.tileValue = :tileValue, m.tileOwner = :tileOwner, m.visibility = :visibility WHERE m.mapTileId = :mapTileId")
    void updateMapTileEntitiesByMapTileId(@Param("mapTileId") MapTileId mapTileId,
                                          @Param("tileValue") int tileValue,
                                          @Param("tileOwner") Player tileOwner,
                                          @Param("visibility") String visibility);

    @Transactional
    @Modifying
    default void updateMapTileEntities(List<MapTileEntity> mapTileEntities) {
        for (MapTileEntity mapTileEntity : mapTileEntities) {
            updateMapTileEntitiesByMapTileId(mapTileEntity.getMapTileId(),
                    mapTileEntity.getTileValue(),
                    mapTileEntity.getTileOwner(),
                    mapTileEntity.getVisibility());
        }
    }

}
