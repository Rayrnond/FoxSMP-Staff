package com.reflexian.staff.utilities;

import com.reflexian.staff.Staff;
import com.reflexian.staff.utilities.data.player.PlayerData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Placeholders extends PlaceholderExpansion {

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "staff";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Raymond";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.00.00";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        PlayerData playerData = Staff.getPlayerService().getOnlineOrDefault(player.getUniqueId());
        if (playerData == null) return "";

        switch (params) {
//            case "pet_level" -> {
//                if (playerData.getPet() == null) return "";
//                return String.valueOf(playerData.getPet().getLevel());
//            }
//            case "pet_xp" -> {
//                if (playerData.getPet() == null) return "";
//                return String.valueOf(playerData.getPet().getXp());
//            }
//            case "has_pet" -> {
//                if (playerData.getPet() == null) return "false";
//                return "true";
//            }
//            case "pet_name" -> {
//                if (playerData.getPet() == null) return "";
//                return playerData.getPet().getName();
//            }
        }
        return "";
    }
}

