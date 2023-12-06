package com.reflexian.staff.utilities.data;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.reflexian.staff.Staff;
import com.reflexian.staff.utilities.config.DefaultConfig;
import com.reflexian.staff.utilities.data.player.PlayerData;
import org.bukkit.Bukkit;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseService {

    private DataSource dataSource;

    public DatabaseService() {}

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        }catch (Exception e) {
            initializeDataSource();
            return getConnection();
        }
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


            logger.info("Completed data source clean up! Ready to proceed.");
        }catch (Exception e) {
//            e.printStackTrace();
            logger.severe("[ERROR] " + e.getLocalizedMessage());
            logger.severe("Failed to initialize data source. Check the mysql database credentials in the config.yml.");
            logger.severe("Disabling plugin");
            Bukkit.getPluginManager().disablePlugin(Staff.getInstance());
        }
    }
}

