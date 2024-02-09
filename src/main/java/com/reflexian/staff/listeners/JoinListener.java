package com.reflexian.staff.listeners;

import com.reflexian.staff.Staff;
import com.reflexian.staff.utilities.data.player.PlayerData;
import com.reflexian.staff.utilities.data.player.PlayerService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent event) {

        PlayerService staff = Staff.getPlayerService();
        staff.getCachedPlayers().remove(event.getUniqueId());
        staff.getOnlinePlayers().remove(event.getUniqueId());
        staff.get(event.getUniqueId(), (data) -> {
            if (data.isPresent()) {
                staff.getOnlinePlayers().put(event.getUniqueId(), data.get());
            }else {
                PlayerData playerData = staff.getOnlineOrDefault(event.getUniqueId());
                staff.save(playerData);
//                throw new RuntimeException("Failed to load player data for " + event.getUniqueId());
            }
        });

    }

}
