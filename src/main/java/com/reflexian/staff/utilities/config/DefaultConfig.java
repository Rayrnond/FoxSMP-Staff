package com.reflexian.staff.utilities.config;

import pl.mikigal.config.Config;
import pl.mikigal.config.annotation.Comment;
import pl.mikigal.config.annotation.ConfigName;
import pl.mikigal.config.annotation.ConfigPath;

@ConfigName("config.yml")
@Comment("""
        This is the config file for the plugin. You can change the database settings here.
        """)
public interface DefaultConfig extends Config {


    @Comment("This is the host for the database")
    @ConfigPath("mysql.host")
    default String getDatabaseHost() {
        return "127.0.0.1";
    }
    @Comment("This is the port for the database")
    @ConfigPath("mysql.port")
    default int getDatabasePort() {
        return 3306;
    }
    @Comment("This is the database")
    @ConfigPath("mysql.database")
    default String getDatabaseName() {
        return "levitycosmetics";
    }
    @Comment("This is the username for the database")
    @ConfigPath("mysql.username")
    default String getDatabaseUsername() {
        return "root";
    }
    @Comment("This is the password for the database")
    @ConfigPath("mysql.password")
    default String getDatabasePassword() {
        return "password";
    }





}
