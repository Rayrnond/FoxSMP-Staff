package com.reflexian.staff.commands.staff.infractions;

import com.reflexian.staff.Staff;
import com.reflexian.staff.utilities.data.player.PlayerData;
import com.reflexian.staff.utilities.data.player.PunishmentHistory;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class StaffResetCommand extends CommandAPICommand {

    public StaffResetCommand() {
        super("reset");
        withPermission("staff.reset");
        withOptionalArguments(new StringArgument("player"));
        executes((sender, arguments)-> {

            if (arguments.count()==0) {
                sender.sendMessage("§cUsage: §7/staff reset <player>");
                return;
            }

            Player target = Bukkit.getPlayer((String) arguments.get(0));
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
                    playerData.setPunishmentHistory(new PunishmentHistory(playerData.getUuid()));
                    playerData.setMsAFK(0);
                    playerData.setMsPlayed(0);
                    playerData.setFirstSeen(0);
                    Staff.getPlayerService().save(playerData);
                    sender.sendMessage("§aSuccessfully reset player data.");
                }

            });
        });

    }

}
