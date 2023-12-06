package com.reflexian.staff.listeners;

import com.reflexian.staff.Staff;
import com.reflexian.staff.utilities.data.player.PlayerData;
import com.reflexian.staff.utilities.data.player.PlayerService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PlayerService staff = Staff.getPlayerService();
        PlayerData playerData = staff.getOnlineOrDefault(event.getPlayer().getUniqueId());
        if (playerData == null) throw new RuntimeException("Failed to save player data for " + event.getPlayer().getUniqueId());
        if (!playerData.getUsername().equals(event.getPlayer().getName())) {
            playerData.setUsername(event.getPlayer().getName());
        }
        staff.save(playerData);
        staff.getOnlinePlayers().remove(event.getPlayer().getUniqueId());
    }
}
