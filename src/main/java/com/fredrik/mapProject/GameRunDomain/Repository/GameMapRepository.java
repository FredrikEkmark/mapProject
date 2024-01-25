package com.fredrik.mapProject.GameRunDomain.Repository;

import com.fredrik.mapProject.GameRunDomain.Model.GameMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameMapRepository extends JpaRepository<GameMapEntity, UUID> {

}
