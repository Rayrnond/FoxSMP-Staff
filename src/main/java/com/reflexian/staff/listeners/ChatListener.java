package com.reflexian.staff.listeners;

import com.reflexian.staff.Staff;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatListener implements Listener {

    private final Map<UUID,Long> chat = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission("chat.bypass")) return;
        if (chat.containsKey(event.getPlayer().getUniqueId())) {
            if (System.currentTimeMillis() - chat.get(event.getPlayer().getUniqueId()) < Staff.getInstance().getSlowmode()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cYou are sending messages too fast!");
                return;
            } else {
                chat.remove(event.getPlayer().getUniqueId());
            }
        }
        chat.put(event.getPlayer().getUniqueId(),System.currentTimeMillis());
    }
}
