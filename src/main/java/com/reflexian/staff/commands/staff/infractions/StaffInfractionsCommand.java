package com.reflexian.staff.commands.staff.infractions;

import com.reflexian.staff.Staff;
import com.reflexian.staff.utilities.Queue;
import com.reflexian.staff.utilities.data.player.Punishment;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import litebans.api.Database;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.upperlevel.spigot.book.BookUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class StaffInfractionsCommand extends CommandAPICommand {

    public StaffInfractionsCommand() {
        super("infractions");
        withPermission("staff.filter");
        withOptionalArguments(new StringArgument("player"));
        executesConsole((sender, arguments)-> {
            sender.sendMessage("§cConsole cannot use this command.");
        });
        executesPlayer((sender, arguments)-> {

            if (arguments.count()==0) {
                sender.sendMessage("§cUsage: §7/staff infractions <player>");
                return;
            }


            Player target = Bukkit.getPlayer((String) arguments.get(0));
            if (target==null) {
                sender.sendMessage("§cPlayer not found or never joined.");
                return;
            }

            sender.sendMessage("§8Processing...");
            getPunishments(target.getUniqueId(), queue-> {
//                int total = (int) Math.ceil(queue.keySet().stream().mapToInt(k -> queue.get(k).size()).sum() / 6.0);
                List<BaseComponent[]> pages = new ArrayList<>();


                for (String key : queue.keySet()) {
                    var page = new BookUtil.PageBuilder();

                    int i = 0;
                    page.add(
                            BookUtil.TextBuilder.of(key.toUpperCase())
                                    .color(ChatColor.BLACK)
                                    .style(ChatColor.BOLD)
                                    .build()
                    );
                    page.newLine();
                    page.newLine();

                    int before = pages.size();
                    for (Punishment punishment : queue.get(key)) {
                        String name = punishment.getType().toUpperCase();
                        if (name.endsWith("S")) {
                            name = name.substring(0,name.length()-1);
                        }
                        page.add(
                                BookUtil.TextBuilder.of(name)
                                        .style(ChatColor.BOLD)
                                        .color(ChatColor.RED)
                                        .build()
                        );
                        page.add(
                                BookUtil.TextBuilder.of(" "+punishment.getReason())
                                        .color(ChatColor.BLACK)
                                        .build()
                        );
                        page.newLine();
                        page.newLine();
                        i++;
                        if (i == 7) {
                            pages.add(page.build());
                            i=0;
                        } else if (punishment.equals(queue.get(key).get(queue.get(key).size() - 1))){
                            pages.add(page.build());
                            break;
                        }
                    }

                    if (pages.size()==before) {
                        pages.add(page.build());
                    }

                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(Staff.getInstance(),()->{
                    BookUtil.openPlayer(sender,BookUtil.writtenBook().pages(pages).build());
                });
            });

        });
    }


    public void getPunishments(UUID uuid, Queue<Map<String, LinkedList<Punishment>>> q) {
        Bukkit.getScheduler().runTaskAsynchronously(Staff.getInstance(), () -> {

            final Map<String,LinkedList<Punishment>> p = new HashMap<>();
            LinkedList<String> options = new LinkedList<>(Arrays.asList("bans", "mutes", "warnings"));
            for (String option : options) {

                String query = "SELECT * FROM {"+option+"} WHERE uuid=?";
                try (PreparedStatement st = Database.get().prepareStatement(query)) {;
                    st.setString(1, uuid.toString());
                    try (ResultSet rs = st.executeQuery()) {
                        while (rs.next()) {
                            String reason = rs.getString("reason");
                            boolean ip = option.equals("bans") && rs.getString("ipban").equals("1");

                            Punishment punishment = Punishment.builder()
                                    .type(option)
                                    .reason(reason)
                                    .ip(ip)
                                    .duration("-1")
                                    .build();

                            if (ip) {
                                option="ipbans";
                            }
                            p.computeIfAbsent(option, k -> new LinkedList<>());
                            p.get(option).add(punishment);

//                            long time = rs.getLong("time");
//                            long until = rs.getLong("until");
//                            long id = rs.getLong("id");
//                            boolean active = rs.getBoolean("active");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        q.execute(null);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    q.execute(null);
                    return;
                }



            }
            q.execute(p);

        });
    }

}
