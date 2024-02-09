package com.reflexian.staff.inventories;

import com.reflexian.staff.listeners.ChatListener;
import com.reflexian.staff.utilities.data.DatabaseService;
import com.reflexian.staff.utilities.data.player.PlayerData;
import com.reflexian.staff.utilities.data.player.Punishment;
import com.reflexian.staff.utilities.inventory.BundledPlayerPunishment;
import com.reflexian.staff.utilities.inventory.ClickAction;
import com.reflexian.staff.utilities.inventory.InvUtils;
import com.reflexian.staff.utilities.inventory.Inventory;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class PunishmentCustomInventory implements Inventory {

    private final PlayerData target;

    public PunishmentCustomInventory(PlayerData target) {
        this.target = target;
    }


    @Override
    public void init(Player player) {

        InvUtils.showInventory(player, "category-custom",
                new ClickAction("warn", (p, clickAction) -> {
                    askForReason(p,"warn");
                }),
                new ClickAction("mute", (p, clickAction) -> {
                    askForReason(p,"mute");
                }),
                new ClickAction("ban", (p, clickAction) -> {
                    askForReason(p,"ban");
                }),
                new ClickAction("ipban", (p, clickAction) -> {
                    askForReason(p,"ipban");
                })
        );
    }

    public void askForReason(Player player, String type) {
        player.closeInventory();
        player.sendMessage("\n\n\n§cPlease enter a reason for this punishment.");
        player.sendMessage("§7Type §ccancel §7to cancel.");
        ChatListener.durationListeners.put(player.getUniqueId(), e->{
            if (e.equalsIgnoreCase("cancel")) {
                player.sendMessage("§cCancelled.");
                ChatListener.durationListeners.remove(player.getUniqueId());
                return;
            }

            if (type.equals("warn")) {
                Punishment punishment = Punishment.builder()
                        .type(type)
                        .reason(e)
                        .build();

                player.sendMessage("§cPunishment created for §c"+target.getUsername()+"§c.");
                Bukkit.dispatchCommand(player,punishment.toCommand(target.getUsername()));
                return;
            }

            player.sendMessage("§cPlease enter a duration for this punishment.");
            player.sendMessage("§7e.g. §c1d §7or §c1y §7or §cpermanent §7or §c1h30m");
            player.sendMessage("§7Type §ccancel §7to cancel.");
            ChatListener.durationListeners.put(player.getUniqueId(), f->{
                if (f.equalsIgnoreCase("cancel")) {
                    player.sendMessage("§cCancelled.");
                    ChatListener.durationListeners.remove(player.getUniqueId());
                    return;
                }

                // validate duration
                if (!f.equalsIgnoreCase("permanent")) {
                    if (!f.matches("^[0-9]+[smhdy]$")) {
                        player.sendMessage("§cInvalid duration. Try again or say cancel.");
                        return;
                    }
                }

                Punishment punishment = Punishment.builder()
                        .type(type)
                        .reason(e)
                        .duration(f)
                        .build();

                player.sendMessage("§cPunishment created for §c"+target.getUsername()+"§c.");
                Bukkit.dispatchCommand(player,punishment.toCommand(target.getUsername()));

            });
        });
    }
}
