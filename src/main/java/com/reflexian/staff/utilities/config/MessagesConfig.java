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

    @Comment("Slowmode error")
    @ConfigPath("staff.slowmode-error")
    default String getSlowmodeErrorMessage() {
        return "&cPlease slow down!";
    }

    @Comment("Chat filter error")
    @ConfigPath("staff.chatfilter-error")
    default String getChatFilterMessage() {
        return "&cYou cannot say that!";
    }

    @Comment("Shown in punish gui for next punishment")
    @ConfigPath("gui.next")
    default String getNextMessage() {
        return "<color:green><bold>Next ><reset> ";
    }

    @Comment("Shown in punish gui for offense")
    @ConfigPath("gui.offense")
    default String getOffenseMessage() {
        return "<color:red>Offense #%number%";
    }


    @Comment("Playtime message")
    @ConfigPath("staff.playtime")
    default String getPlaytimeMessage() {
        return "&7%player%'s &eplaytime&7: &a%playtime%\n&7%player%'s &cafk time&7: &a%afktime%";
    }




}
