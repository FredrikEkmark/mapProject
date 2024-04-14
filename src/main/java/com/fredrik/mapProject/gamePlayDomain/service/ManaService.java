package com.fredrik.mapProject.gamePlayDomain.service;

import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gamePlayDomain.repository.ManaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ManaService {

    private final ManaRepository manaRepository;

    public ManaService(ManaRepository manaRepository) {
        this.manaRepository = manaRepository;
    }

    public void createNewMana(UUID manaID, UUID gameId) {
        manaRepository.save(new ManaEntity(manaID, gameId));
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
}