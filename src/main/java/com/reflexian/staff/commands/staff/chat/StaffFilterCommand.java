package com.reflexian.staff.commands.staff.chat;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;

public class StaffFilterCommand extends CommandAPICommand {

    public StaffFilterCommand() {
        super("filter");
        withPermission("staff.filter");
        withOptionalArguments(new StringArgument("word"));
        executes((sender, arguments)-> {

            //todo
        });

    }

}
