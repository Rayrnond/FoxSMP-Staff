package com.reflexian.staff.commands.staff.chat;

import com.reflexian.staff.Staff;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;

public class StaffSlowmodeCommand extends CommandAPICommand {

    public StaffSlowmodeCommand() {
        super("slowmode");
        withPermission("staff.slowmode");
        withOptionalArguments(new IntegerArgument("amount"));
        executes((sender, arguments)-> {
            if (arguments.count()==0) {
                sender.sendMessage("§cUsage: §7/staff slowmode <amountInSeconds>");
                return;
            }
            Staff.getInstance().setSlowmode(((Integer) arguments.get("amount")) * 1000);
            Bukkit.broadcastMessage(Staff.getMessagesConfig().getSlowmodeMessage().replace("%player%", sender.getName()).replace("%amount%", String.valueOf(arguments.get("amount"))));
        });

    }

}
