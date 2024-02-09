package com.reflexian.staff.listeners;

import com.reflexian.staff.Staff;
import com.reflexian.staff.utilities.data.player.PlayerData;
import com.reflexian.staff.utilities.data.player.PlayerService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MovementListener implements Listener {

    private final Map<UUID,Long> lastMove = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        // check if player moved a full block
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {

            calculate(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreQuit(PlayerQuitEvent event) {
        PlayerService staff = Staff.getPlayerService();
        staff.save(calculate(event.getPlayer().getUniqueId()));

        PlayerData playerData = staff.getOnlineOrDefault(event.getPlayer().getUniqueId());
        if (playerData == null) throw new RuntimeException("Failed to save player data for " + event.getPlayer().getUniqueId());
        if (!playerData.getUsername().equals(event.getPlayer().getName())) {
            playerData.setUsername(event.getPlayer().getName());
        }
        staff.save(playerData);
        staff.getOnlinePlayers().remove(event.getPlayer().getUniqueId());
    }
    
    private PlayerData calculate(UUID uuid) {
        PlayerData playerData = Staff.getPlayerService().getOnlineOrDefault(uuid);
        if (System.currentTimeMillis() - lastMove.getOrDefault(uuid,0L) > 300000) {
            playerData.setMsAFK(playerData.getMsAFK() + (System.currentTimeMillis() - lastMove.getOrDefault(uuid,System.currentTimeMillis())));
        } else {
            playerData.setMsPlayed(playerData.getMsPlayed() + (System.currentTimeMillis() - lastMove.getOrDefault(uuid,System.currentTimeMillis())));
        }

        lastMove.remove(uuid);
        lastMove.put(uuid,System.currentTimeMillis());
        return playerData;
    }
}
