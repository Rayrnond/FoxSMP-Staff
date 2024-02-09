package com.reflexian.staff.utilities.data;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.reflexian.staff.Staff;
import com.reflexian.staff.utilities.config.DefaultConfig;
import com.reflexian.staff.utilities.data.player.PlayerData;
import com.reflexian.staff.utilities.data.player.Punishment;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class DatabaseService {

    private DataSource dataSource;
    @Getter private static final Map<String, Map<Integer, Punishment>> punishments = new HashMap<>();
    @Getter private static final Map<String,String> categories = new HashMap<>();

    public DatabaseService() {}

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        }catch (Exception e) {
            initializeDataSource();
            return getConnection();
        }
    }

    public static Map<String, Map<Integer, Punishment>> getPunishmentsFromCategory(String category) {
        Map<String, Map<Integer, Punishment>> map = new HashMap<>();
        punishments.forEach((s, integerPunishmentMap) -> {
            if (categories.get(s).equalsIgnoreCase(category)) {
                map.put(s,integerPunishmentMap);
            }
        });
        return map;
    }

    final Logger logger = Staff.getInstance().getLogger();
    public void initializeDataSource() {
        try {
            final DefaultConfig config = Staff.getConfiguration();

            logger.info("Initializing data source MySQL...");
            MysqlDataSource mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setURL("jdbc:mysql://"+config.getDatabaseHost()+":"+config.getDatabasePort()+"/"+config.getDatabaseName());
            mysqlDataSource.setUser(config.getDatabaseUsername());
            mysqlDataSource.setPassword(config.getDatabasePassword());

            dataSource = mysqlDataSource;
            if (dataSource.getConnection() == null) {
                throw new Exception("Failed to initialize data source, invalid details!");
            }
            logger.info("Data source initialized successfully!");

            Connection connection = Staff.getDatabaseService().getConnection();
            connection.prepareStatement(PlayerData.toTable()).execute();

            ResultSet resultSet = connection.prepareStatement("SELECT * FROM `player_data` LIMIT 1").executeQuery();

            final YamlConfiguration c = Staff.getPunishments();

            Set<String> configs = c.getConfigurationSection("ladder").getKeys(false);

            // get all columns
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                String name = resultSet.getMetaData().getColumnName(i+1);
                if (name.startsWith("offense-")) {
                    String punishment = name.split("-")[1];
                    if (!configs.contains(punishment)) {
                        logger.info("Removing column " + name + " from player_data table");
                        connection.prepareStatement("ALTER TABLE `player_data` DROP COLUMN `"+name+"`").execute();
                    }
                }
            }

            configs.forEach(punishment -> {
                try {
                    // check if column exists
                    try {
                        resultSet.findColumn("offense-"+punishment);
                    }catch (SQLException e) {
                        logger.info("Adding column " + punishment + " to player_data table");
                        connection.prepareStatement("ALTER TABLE `player_data` ADD `offense-"+punishment+"` INT NOT NULL DEFAULT '0'").execute();
                    }

                    // save the config punishment to the map
                    Map<Integer, Punishment> punishmentMap = new HashMap<>();
                    c.getConfigurationSection("ladder."+punishment).getKeys(true).forEach(key -> {
                        if (key.equals("category")) return;
                        String s = c.getString("ladder."+punishment+"."+key);
                        Punishment p = Punishment.builder()
                                .tier(Integer.parseInt(key))
                                .type(s.split(" ")[0])
                                .duration(s.split(" ")[1].split(":")[0])
                                .reason(s.split(":")[1])
                                .build();
                        punishmentMap.put(Integer.parseInt(key), p);
                    });
                    punishments.put(punishment, punishmentMap);
//                    String category = c.getString("ladder."+punishment+".category");
                    categories.put(punishment,c.getString("ladder."+punishment+".category"));
                    logger.info("Loaded punishment ladder for " + punishment + " with category " + categories.get(punishment));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });

            logger.info("Completed data source clean up! Ready to proceed.");
        }catch (Exception e) {
            e.printStackTrace();
            logger.severe("[ERROR] " + e.getLocalizedMessage());
            logger.severe("Failed to initialize data source. Check the mysql database credentials in the config.yml.");
            logger.severe("Disabling plugin");
            Bukkit.getPluginManager().disablePlugin(Staff.getInstance());
        }
    }
}

