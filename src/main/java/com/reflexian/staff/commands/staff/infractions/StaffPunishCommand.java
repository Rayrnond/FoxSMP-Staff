package com.reflexian.staff.commands.staff.infractions;

import com.reflexian.staff.Staff;
import com.reflexian.staff.inventories.PunishmentParentInventory;
import com.reflexian.staff.utilities.data.player.PlayerData;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class StaffPunishCommand extends CommandAPICommand {

    public StaffPunishCommand() {
        super("punish");
        withPermission("staff.punish");
        withOptionalArguments(new StringArgument("player"));
        executesConsole((sender, arguments)-> {
            sender.sendMessage("§cThis command can only be executed by players.");
        });
        executesPlayer((sender, arguments)-> {

            if (arguments.count()==0) {
                sender.sendMessage("§cUsage: §7/staff punish <player>");
                return;
            }

            Player target = Bukkit.getPlayer((String) arguments.get(0));
            if (target==null) {
                sender.sendMessage("§cPlayer not found or never joined.");
                return;
            }

            new PunishmentParentInventory(Staff.getPlayerService().getOnlineOrDefault(target.getUniqueId())).init((org.bukkit.entity.Player) sender);
        });

    }
}
