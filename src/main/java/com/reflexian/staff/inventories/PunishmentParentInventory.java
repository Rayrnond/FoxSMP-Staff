package com.reflexian.staff.inventories;

import com.reflexian.staff.utilities.data.DatabaseService;
import com.reflexian.staff.utilities.data.player.PlayerData;
import com.reflexian.staff.utilities.inventory.ClickAction;
import com.reflexian.staff.utilities.inventory.InvUtils;
import com.reflexian.staff.utilities.inventory.Inventory;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class PunishmentParentInventory implements Inventory {

    private final PlayerData target;

    public PunishmentParentInventory(PlayerData target) {
        this.target = target;
    }

    @Override
    public void init(Player player) {
        final List<ClickAction> actions = new ArrayList<>();

        actions.add(new ClickAction("custom", (p, clickAction) -> {
            p.closeInventory();
            new PunishmentCustomInventory(target).init(p);
        }));

        for (String type : DatabaseService.getCategories().values()) {
            actions.add(new ClickAction("page-"+type, (p, clickAction) -> {
                p.closeInventory();
                new PunishmentCategoryInventory(target,type).init(p);
            }));
        }
        InvUtils.showInventory(player, "punish",
                actions.toArray(new ClickAction[0]
        ));

    }
}
