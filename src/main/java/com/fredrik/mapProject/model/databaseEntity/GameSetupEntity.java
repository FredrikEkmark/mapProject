package com.fredrik.mapProject.model.databaseEntity;

import com.fredrik.mapProject.Utility;
import com.fredrik.mapProject.model.turnChange.Hours;
import com.fredrik.mapProject.model.map.MapSizes;
import jakarta.persistence.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "game_setup")
public class GameSetupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private int seed;

    @ManyToOne()
    @JoinColumn(name = "users_id")
    private UserEntity owner;

    @Column(nullable = false, length = 27)
    private String turnChange;

    @Column(nullable = false)
    private int turn;

    @Column(nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private MapSizes mapSize;

    @Column(nullable = false)
    private boolean isUpdating;

    @Column(nullable = false)
    private Instant startTime;

    @OneToMany(mappedBy = "playerGameId.gameId", cascade = CascadeType.ALL)
    private List<GamePlayerEntity> gamePlayers;

    public GameSetupEntity(UserEntity owner, String turnChange, MapSizes mapSize, int seed) {
        this.id = UUID.randomUUID();
        this.owner = owner;
        this.turnChange = turnChange;
        this.mapSize = mapSize;
        this.seed = seed;
        this.turn = 1;
        this.startTime = Instant.now();
        this.isUpdating = false;
    }

    public GameSetupEntity() {
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public String getTurnChange() {
        return turnChange;
    }

    public String getNextTurnChange() {
        ZoneId zoneId = ZoneId.of("GMT");

        ZonedDateTime now = ZonedDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        char[] hoursCharArray = turnChange.substring(0, Math.min(turnChange.length(), 24)).toCharArray();
        String min = turnChange.substring(Math.max(0, turnChange.length() - 2));

        ZonedDateTime nextTurn = now;

        String isoDateString;

        for (int i = now.getHour(); i < hoursCharArray.length + now.getHour(); i++) {
            if (hoursCharArray[i % 23] == '1') {
                ZonedDateTime baseDate = now;
                if (i > 23) {
                    baseDate = baseDate.plusDays(1);
                }
                 isoDateString = String.format("%s-%s-%sT%s:%s:05.000+00:00",
                         baseDate.getYear(),
                         Utility.padZero(baseDate.getMonthValue()),
                         Utility.padZero(baseDate.getDayOfMonth()),
                         Utility.padZero(i % 23),
                         min);

                nextTurn = ZonedDateTime.parse(isoDateString);

                if (nextTurn.isAfter(now)) {
                    return formatter.format(nextTurn);
                }
            }
        }
        return formatter.format(nextTurn);
    }

    public void setTurnChange(String turnChange) {
        this.turnChange = turnChange;
    }
    public void setTurnChangeFromInputString(String turnChangeInput, String timeZone) {
        // figures out the offsetHours from GMT so that it's saved as GMT
        ZoneId zoneId = ZoneId.of(timeZone);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        ZoneOffset offset = zonedDateTime.getOffset();
        int offsetHours = offset.getTotalSeconds() / 3600;

        StringBuilder builder = new StringBuilder("000000000000000000000000:00"); // Initialize with 24 zeroes
        if (turnChangeInput == null) {this.turnChange = builder.toString(); return;}

        String[] hoursStringArray = turnChangeInput.split(",");
        Hours[] hoursEnumArray = new Hours[hoursStringArray.length];

        for (int i = 0; i < hoursStringArray.length; i++) {
            hoursEnumArray[i] = Hours.valueOf(hoursStringArray[i]);
        }

        for (Hours hour : hoursEnumArray) {// Get the index of the hour
            builder.setCharAt(hour.getTurnChangeIndex(offsetHours), '1'); // Set the corresponding character to '1'
        }

        this.turnChange = builder.toString();
    }

    public MapSizes getMapSize() {
        return mapSize;
    }

    public void setMapSize(MapSizes mapSize) {
        this.mapSize = mapSize;
    }

    public List<GamePlayerEntity> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(List<GamePlayerEntity> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public int getPlayerCount() {
        return gamePlayers != null ? gamePlayers.size() : 0;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public boolean isUpdating() {
        return isUpdating;
    }

    public void setUpdating(boolean updating) {
        isUpdating = updating;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant time) {
        this.startTime = time;
    }
}
