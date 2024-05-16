package com.fredrik.mapProject.model.databaseEntity;

import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;
import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.unit.nameGenerator.UnitNameGenerator;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "unit_army")
public class ArmyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID armyId;

    @Column(nullable = false)
    private UUID gameId;

    @Column(nullable = false, length = 24)
    @Enumerated(EnumType.STRING)
    private Player owner;

    @Column(nullable = false, length = 64)
    private String armyName;

    @Column(nullable = false)
    private int armyNumber;

    @OneToMany(mappedBy = "army", cascade = CascadeType.ALL)
    private List<RegimentEntity> regiments;

    @Embedded
    @Column(nullable = false)
    private MapCoordinates armyCoordinates;

    @Column(nullable = false)
    private boolean fortified;


    public ArmyEntity(UUID gameId, Player owner, int armyNumber, MapCoordinates armyCoordinates) {
        this.armyId = UUID.randomUUID();
        this.gameId = gameId;
        this.owner = owner;
        this.armyName = UnitNameGenerator.getGeneratedArmyName(armyNumber);
        this.armyNumber = armyNumber;
        this.regiments = new ArrayList<>();
        this.armyCoordinates = armyCoordinates;
        this.fortified = false;
    }

    public ArmyEntity() {
    }

    public int regimentsSize() {
        return regiments.size();
    }

    public UUID getArmyId() {
        return armyId;
    }

    public void setArmyId(UUID armyId) {
        this.armyId = armyId;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public String getArmyName() {
        return armyName;
    }

    public void setArmyName(String armyName) {
        this.armyName = armyName;
    }

    public int getArmyNumber() {
        return armyNumber;
    }

    public void setArmyNumber(int armyNumber) {
        this.armyNumber = armyNumber;
    }

    public List<RegimentEntity> getRegiments() {
        return regiments;
    }

    public void setRegiments(List<RegimentEntity> regiments) {
        this.regiments = regiments;
    }

    public MapCoordinates getArmyCoordinates() {
        return armyCoordinates;
    }

    public void setArmyCoordinates(MapCoordinates armyCoordinates) {
        this.armyCoordinates = armyCoordinates;
    }

    public boolean isFortified() {
        return fortified;
    }

    public void setFortified(boolean fortified) {
        this.fortified = fortified;
    }

    public int getArmyMovement() {
        int movement = regiments.get(0).getMovement();
        for (RegimentEntity regiment: regiments) {
            if (regiment.getMovement() < movement) {
                movement = regiment.getMovement();
            }
        }
        return  movement;
    }
}
