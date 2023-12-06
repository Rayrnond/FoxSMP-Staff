package com.reflexian.staff.listeners;

import com.reflexian.staff.Staff;
import com.reflexian.staff.utilities.data.player.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MovementListener implements Listener {

    private final Map<UUID,Long> lastMove = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        // check if player moved a full block
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {

            PlayerData playerData = Staff.getPlayerService().getOnlineOrDefault(event.getPlayer().getUniqueId());
            if (System.currentTimeMillis() - lastMove.getOrDefault(event.getPlayer().getUniqueId(),0L) > 300000) {
                playerData.setMsAFK(playerData.getMsAFK() + lastMove.get(event.getPlayer().getUniqueId()) - System.currentTimeMillis());
            } else {
                playerData.setMsPlayed(playerData.getMsPlayed() + lastMove.get(event.getPlayer().getUniqueId()) - System.currentTimeMillis());
            }

            lastMove.remove(event.getPlayer().getUniqueId());
            lastMove.put(event.getPlayer().getUniqueId(),System.currentTimeMillis());
        }
    }
}
