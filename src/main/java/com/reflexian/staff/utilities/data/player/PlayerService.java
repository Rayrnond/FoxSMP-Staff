package com.reflexian.staff.utilities.data.player;

import com.reflexian.staff.Staff;
import com.reflexian.staff.utilities.Queue;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public class PlayerService {

    private final Map<UUID,PlayerData> onlinePlayers = new HashMap<>();
    private final Map<UUID,PlayerData> cachedPlayers = new HashMap<>();

    private final long lastCacheRefresh = System.currentTimeMillis();

    public PlayerService() {}

    public PlayerData getOnlineOrDefault(UUID uuid) {
        if (onlinePlayers.containsKey(uuid)) return onlinePlayers.get(uuid);
        PlayerData data = new PlayerData(uuid);
        onlinePlayers.put(uuid,data);
        save(data);
        return data;
    }


    public void get(String name, Queue<Optional<PlayerData>> queue) {
        if (Bukkit.getPlayer(name) != null) {
            queue.execute(Optional.of(onlinePlayers.get(Bukkit.getPlayer(name).getUniqueId())));
            return;
        }
        try {
            ResultSet resultSet = Staff.getDatabaseService().getConnection().prepareStatement(PlayerData.toQuery(name)).executeQuery();
            if (resultSet.next()) {
                PlayerData data = PlayerData.fromResultSet(resultSet);
                cachedPlayers.put(data.getUuid(),data);
                queue.execute(Optional.of(data));
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        queue.execute(Optional.empty());
    }

    public void get(UUID uuid, Queue<Optional<PlayerData>> queue) {

        if (cachedPlayers.containsKey(uuid)) {
            queue.execute(Optional.of(cachedPlayers.get(uuid)));
            return;
        } else if (onlinePlayers.containsKey(uuid)) {
            queue.execute(Optional.of(onlinePlayers.get(uuid)));
            return;
        }

        Bukkit.getScheduler().scheduleAsyncDelayedTask(Staff.getInstance(),()->{
            try {
                ResultSet resultSet = Staff.getDatabaseService().getConnection().prepareStatement(PlayerData.toQuery(uuid)).executeQuery();
                if (resultSet.next()) {
                    PlayerData data = PlayerData.fromResultSet(resultSet);
                    cachedPlayers.put(uuid,data);
                    queue.execute(Optional.of(data));
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            queue.execute(Optional.empty());
        });
    }


    public void save(PlayerData data) {
        onlinePlayers.replace(data.getUuid(),data);
        cachedPlayers.replace(data.getUuid(),data);
        CompletableFuture.runAsync(()->{
            try {
                Staff.getDatabaseService().getConnection().prepareStatement(PlayerData.toSQL(data)).execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
