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

    private PunishmentHistory punishmentHistory;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.punishmentHistory = new PunishmentHistory(uuid);
    }


    public static PlayerData fromResultSet(ResultSet resultSet) {
        try {
            PlayerData playerData = new PlayerData(UUID.fromString(resultSet.getString("uuid")));
            playerData.setUsername(resultSet.getString("username"));
            playerData.setMsPlayed(resultSet.getLong("ms_played"));
            playerData.setMsAFK(resultSet.getLong("ms_afk"));
            playerData.setFirstSeen(resultSet.getLong("first_seen"));
            playerData.setLastSeen(resultSet.getLong("last_seen"));

            playerData.setPunishmentHistory(new PunishmentHistory(playerData.getUuid()));

            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                String columnName = resultSet.getMetaData().getColumnName(i);
                if (columnName.startsWith("offense-")) {
                    playerData.getPunishmentHistory().getPunishments().put(columnName.replace("offense-",""),resultSet.getInt(columnName));
                }
            }
            return playerData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toSQL(PlayerData playerData) {
        StringBuilder builder = new StringBuilder("INSERT INTO `player_data` (`uuid`,`username`,`ms_played`,`ms_afk`,`first_seen`,`last_seen`");
        playerData.getPunishmentHistory().getPunishments().forEach((p1,p2) -> {
            builder.append(",`offense-").append(p1).append("`");
        });
        builder.append(") VALUES ('").append(playerData.getUuid().toString()).append("','").append(playerData.getUsername()).append("',").append(playerData.getMsPlayed()).append(",").append(playerData.getMsAFK()).append(",").append(playerData.getFirstSeen()).append(",").append(playerData.getLastSeen());
        playerData.getPunishmentHistory().getPunishments().forEach((p1,p2) -> {
            builder.append(",").append(p2);
        });
        builder.append(") ON DUPLICATE KEY UPDATE `username` = '").append(playerData.getUsername()).append("', `ms_played` = ").append(playerData.getMsPlayed()).append(", `ms_afk` = ").append(playerData.getMsAFK()).append(", `last_seen` = ").append(playerData.getLastSeen());
        playerData.getPunishmentHistory().getPunishments().forEach((p1,p2) -> {
            builder.append(",`offense-").append(p1).append("` = ").append(p2);
        });
        builder.append(";");
        return builder.toString();
    }
    public static String toQuery(String username) {
        return "SELECT * FROM `player_data` WHERE `username` = '"+username.toString().toLowerCase()+"';";
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
