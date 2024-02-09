package com.reflexian.staff.listeners;

import com.reflexian.staff.Staff;
import com.reflexian.staff.utilities.data.FilterService;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

@Getter
public class ChatListener implements Listener {

    private final Map<UUID,Long> chat = new HashMap<>();
    public static final Map<UUID, com.reflexian.staff.utilities.Queue<String>> durationListeners = new HashMap<>();

    @EventHandler
    public void onSyncChat(PlayerChatEvent event) {
        if (durationListeners.containsKey(event.getPlayer().getUniqueId())) {
            durationListeners.get(event.getPlayer().getUniqueId()).execute(event.getMessage());
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        if (event.getPlayer().hasPermission("chat.bypass")) return;

        // slowmode using chat map
        if (chat.containsKey(event.getPlayer().getUniqueId())) {
            if (System.currentTimeMillis() - chat.get(event.getPlayer().getUniqueId()) < Staff.getInstance().getSlowmode()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Staff.getMessagesConfig().getSlowmodeErrorMessage());
                return;
            }
        }
        chat.put(event.getPlayer().getUniqueId(),System.currentTimeMillis());

        // check if message contains filtered words
        String[] words = event.getMessage().split(" ");
        for (String word : words) {
            if (FilterService.isFiltered(word)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Staff.getMessagesConfig().getChatFilterMessage());
                return;
            }
        }
    }
}
