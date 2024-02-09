package com.reflexian.staff.commands.staff.playtime;

import com.reflexian.staff.Staff;
import com.reflexian.staff.utilities.data.player.PlayerData;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.OfflinePlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class StaffPlaytimeCommand extends CommandAPICommand {

    public StaffPlaytimeCommand() {
        super("playtime");
        withPermission("staff.playtime");
        withOptionalArguments(new StringArgument("player"));
        executes((sender, arguments)-> {

            if (arguments.count()==0) {
                sender.sendMessage("§cUsage: §7/staff playtime <player>");
                return;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer((String) arguments.get(0));
            if (target==null) {
                sender.sendMessage("§cPlayer not found or never joined.");
                return;
            }

            sender.sendMessage("§8Processing...");
            Staff.getPlayerService().get(target.getUniqueId(), queue->{
                if (!queue.isPresent()) {
                    sender.sendMessage("§cPlayer not found in database.");
                } else {
                    PlayerData playerData = queue.get();


                    sender.sendMessage(PlaceholderAPI.setPlaceholders(target, Staff.getMessagesConfig().getPlaytimeMessage().replace("%player%", target.getName()).replace("%playtime%", formatMs(playerData.getMsPlayed())).replace("%afktime%", formatMs(playerData.getMsAFK()))));

                }

            });
        });

    }

    private String formatMs(long ms) {
        long days = ms / 86400000;
        long hours = (ms % 86400000) / 3600000;
        long minutes = ((ms % 86400000) % 3600000) / 60000;
        long seconds = (((ms % 86400000) % 3600000) % 60000) / 1000;
        return String.format("%02dD:%02dH:%02dM:%02dS", days, hours, minutes, seconds);
    }
}
