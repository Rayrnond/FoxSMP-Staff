package com.reflexian.staff.commands.staff.chat;

import com.reflexian.staff.Staff;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StaffClearChatCommand extends CommandAPICommand {

    public StaffClearChatCommand() {
        super("clearchat");
        withPermission("staff.clearchat");
        executes((sender, arguments)-> {
            for (int i = 0; i < 500; i++) {
                StringBuilder message = new StringBuilder(" ");
                for (int j = 0; j < i; j++) {
                    message.append(" ");
                }
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendMessage(message.toString());
                }
            }
            Bukkit.broadcastMessage(Staff.getMessagesConfig().getClearChatMessage().replace("%player%", sender.getName()));
        });

    }

}
