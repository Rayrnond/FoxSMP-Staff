package com.reflexian.staff.inventories;

import com.reflexian.staff.utilities.data.DatabaseService;
import com.reflexian.staff.utilities.data.player.PlayerData;
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
public class PunishmentCategoryInventory implements Inventory {

    private final PlayerData target;
    private final String page;

    public PunishmentCategoryInventory(PlayerData target, String page) {
        this.target = target;
        this.page = page;
    }


    @Override
    public void init(Player player) {
        final List<ClickAction> actions = new ArrayList<>();

        actions.add(new ClickAction("custom", (p, clickAction) -> {
            p.closeInventory();
            new PunishmentCustomInventory(target).init(p);
        }));

        final Map<String,BundledPlayerPunishment> punishments = new HashMap<>();

        for (String type : DatabaseService.getPunishmentsFromCategory(page).keySet()) {
            BundledPlayerPunishment bundledPlayerPunishment = new BundledPlayerPunishment(page,target,DatabaseService.getPunishmentsFromCategory(page).get(type));
            actions.add(new ClickAction("punishment-"+type, (p, clickAction) -> {
                p.closeInventory();
                p.sendMessage("§aYou have successfully punished §7"+target.getUsername()+" §afor §7"+type+"§a.");
                target.getPunishmentHistory().addPunishment(type);
                Bukkit.dispatchCommand(player, target.getPunishmentHistory().getPunishment(type,target.getPunishmentHistory().getPunishments(type)).toCommand(target.getUsername()));
            }));
            punishments.put(type,bundledPlayerPunishment);
        }
        InvUtils.showInventory(player, "category-"+page, punishments,
                actions.toArray(new ClickAction[0]
                )
        );
    }
}
