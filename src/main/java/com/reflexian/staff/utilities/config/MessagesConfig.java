package com.reflexian.staff.utilities.config;

import pl.mikigal.config.Config;
import pl.mikigal.config.annotation.Comment;
import pl.mikigal.config.annotation.ConfigName;
import pl.mikigal.config.annotation.ConfigPath;

@ConfigName("messages.yml")
@Comment("""
        This is the messages file for the plugin.
        """)
public interface MessagesConfig extends Config {


    @Comment("Clear chat message")
    @ConfigPath("staff.clearchat")
    default String getClearChatMessage() {
        return "&7Chat cleared by &e%player%&7.";
    }

    @Comment("Slowmode message")
    @ConfigPath("staff.slowmode")
    default String getSlowmodeMessage() {
        return "&7Slowmode set to &e%amount%&7.";
    }





}
