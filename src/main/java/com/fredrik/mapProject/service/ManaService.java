package com.fredrik.mapProject.service;

import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.repository.ManaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ManaService {

    private final ManaRepository manaRepository;

    public ManaService(ManaRepository manaRepository) {
        this.manaRepository = manaRepository;
    }

    public void createNewMana(UUID manaID, UUID gameId, Player playerNr) {
        manaRepository.save(new ManaEntity(manaID, gameId, playerNr));
    }

    public void deleteManaById(UUID manaID) {
        manaRepository.deleteById(manaID);
    }

    public void deleteAllManaByGameID(UUID gameId) {
        manaRepository.deleteAllByGameId(gameId);
    }

    public Optional<ManaEntity> findManaById(UUID manaId) {
        return manaRepository.findById(manaId);
    }
    public List<ManaEntity> findAllManaByGameId(UUID gameId) {
        return manaRepository.findAllByGameId(gameId);
    }

    public void updateAll(List<ManaEntity> manaList) {
        manaRepository.saveAll(manaList);
    }
}
