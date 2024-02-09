package com.reflexian.staff.utilities.data;

import com.reflexian.staff.Staff;
import com.reflexian.staff.utilities.data.player.PlayerData;
import com.reflexian.staff.utilities.data.player.PlayerService;
import org.bukkit.scheduler.BukkitRunnable;

public class AutosaveRunnable extends BukkitRunnable {
    @Override
    public void run() {
        final PlayerService playerService = Staff.getPlayerService();
        for (PlayerData value : playerService.getOnlinePlayers().values()) {
            playerService.save(value);
        }
    }
}
