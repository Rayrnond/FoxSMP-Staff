package com.reflexian.staff.commands.staff.chat;

import com.reflexian.staff.utilities.data.FilterService;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;

public class StaffFilterCommand extends CommandAPICommand {

    public StaffFilterCommand() {
        super("filter");
        withPermission("staff.filter");
        withOptionalArguments(new StringArgument("word"));
        executes((sender, arguments)-> {

            if (arguments.count()==0) {
                sender.sendMessage("§cUsage: §7/staff filter <word> - Toggle filter for a word.");
                return;
            }
            
            String word = (String) arguments.get("word");
            if (word == null) {
                sender.sendMessage("§cUsage: §7/staff filter <word> - Toggle filter for a word.");
                return;
            }
            
            if (FilterService.isFiltered(word)) {
                FilterService.removeWord(word);
                sender.sendMessage("§aRemoved §7" + word + " §afrom the filter.");
            } else {
                FilterService.addWord(word);
                sender.sendMessage("§aAdded §7" + word + " §ato the filter.");
            }
        });

    }

}
