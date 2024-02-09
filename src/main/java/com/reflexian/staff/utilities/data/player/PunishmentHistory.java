package com.reflexian.staff.utilities.data.player;

import com.reflexian.staff.utilities.data.DatabaseService;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class PunishmentHistory {

    private final Map<String, Integer> punishments = new HashMap<>();
    private final UUID uuid;

    public PunishmentHistory(UUID uuid) {
        this.uuid = uuid;
    }

    public Punishment getPunishment(String type, int tier) {
        Map<Integer,Punishment> m = DatabaseService.getPunishments().get(type);
        return m.getOrDefault(tier, m.get(m.size()));
    }

    public int getPunishments(String type) {
        return punishments.getOrDefault(type, 0);
    }

    public void addPunishment(String type) {
        if (DatabaseService.getPunishments().get(type).size() <= getPunishments(type)) return;
        punishments.put(type, getPunishments(type) + 1);
    }




}
