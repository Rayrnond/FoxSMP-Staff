package com.reflexian.staff.commands.staff;

import com.reflexian.staff.commands.staff.chat.StaffClearChatCommand;
import com.reflexian.staff.commands.staff.chat.StaffFilterCommand;
import com.reflexian.staff.commands.staff.chat.StaffSlowmodeCommand;
import com.reflexian.staff.commands.staff.playtime.StaffPlaytimeCommand;
import dev.jorel.commandapi.CommandAPICommand;

public class StaffParentCommand extends CommandAPICommand {
    public StaffParentCommand() {
        super("staff");
        withAliases("s");
        withPermission("staff.command");
        executes((sender, args)-> {
            sender.sendMessage("§cUsage: §7/staff <playtime/infractions/punish> <player>");
            sender.sendMessage("§cUsage: §7/staff <slowmode> <amountInSeconds>");
            sender.sendMessage("§cUsage: §7/staff <filter> <word>");
        });
        withSubcommands(new StaffPlaytimeCommand());
        withSubcommands(new StaffClearChatCommand());
        withSubcommands(new StaffSlowmodeCommand());
        withSubcommands(new StaffFilterCommand());
    }

}
