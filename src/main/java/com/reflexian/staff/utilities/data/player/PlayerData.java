package com.reflexian.staff.utilities.data.player;

import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.util.UUID;

@Getter@Setter
public class PlayerData {

    private final UUID uuid;
    private String username="";
    private long msPlayed=0;
    private long msAFK=0;
    private long firstSeen=0;
    private long lastSeen=0;

    private long lastMove=0;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }


    public static PlayerData fromResultSet(ResultSet resultSet) {
        try {
            PlayerData playerData = new PlayerData(UUID.fromString(resultSet.getString("uuid")));
            playerData.setUsername(resultSet.getString("username"));
            playerData.setMsPlayed(resultSet.getLong("ms_played"));
            playerData.setMsAFK(resultSet.getLong("ms_afk"));
            playerData.setFirstSeen(resultSet.getLong("first_seen"));
            playerData.setLastSeen(resultSet.getLong("last_seen"));
            return playerData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toSQL(PlayerData playerData) {
        return "INSERT INTO `player_data` (`uuid`, `username`, `ms_played`, `ms_afk`, `first_seen`, `last_seen`) VALUES ('"+playerData.uuid.toString()+"', '"+playerData.username+"', '"+playerData.msPlayed+"', '"+playerData.msAFK+"', '"+playerData.firstSeen+"', '"+playerData.lastSeen+"') ON DUPLICATE KEY UPDATE `username`='"+playerData.username+"', `ms_played`='"+playerData.msPlayed+"', `ms_afk`='"+playerData.msAFK+"', `last_seen`='"+playerData.lastSeen+"';";
    }

    public static String toQuery(UUID uuid) {
        return "SELECT * FROM `player_data` WHERE `uuid` = '"+uuid.toString()+"';";
    }

    public static String toTable() {
        return "CREATE TABLE IF NOT EXISTS `player_data` (" +
                "  `uuid` varchar(36) NOT NULL,\n" +
                "  `username` varchar(16) NOT NULL,\n" +
                "  `ms_played` bigint(20) NOT NULL,\n" +
                "  `ms_afk` bigint(20) NOT NULL,\n" +
                "  `first_seen` bigint(20) NOT NULL,\n" +
                "  `last_seen` bigint(20) NOT NULL,\n" +
                "  PRIMARY KEY (`uuid`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
    }

}
