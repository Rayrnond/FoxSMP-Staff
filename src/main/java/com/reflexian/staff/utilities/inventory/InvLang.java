package com.reflexian.staff.utilities.inventory;

import com.reflexian.staff.Staff;
import dev.lone.itemsadder.api.CustomStack;
import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class InvLang {

    /**
     *
     * path:
     *  type:
     *  amount:
     *  data:
     *  name:
     *  lore:
     *  - ""
     *  - ""
     *
     */

    public static ItemStack itemStackInvs(YamlConfiguration c, String s, @Nullable BundledPlayerPunishment punishment, Player player) {
        ItemBuilder builder = new ItemBuilder(Material.valueOf(c.getString(s + ".material").toUpperCase()));



        if (c.contains(s + ".itemsadderID")) {
            String id = c.getString(s + ".itemsadderID");
            if (id != null && !id.isEmpty()) {
                CustomStack stack = CustomStack.getInstance(id);

                if (stack == null) {
                    throw new RuntimeException("Failed to serialize item with ID: " + s + ". Could not find ItemsAdder item with id " + id);
                }

                builder = new ItemBuilder(stack.getItemStack());
            }
        }

        if (c.contains(s + ".data")) builder.data(new MaterialData(builder.getMaterial(), (byte) c.getInt(s + ".data")));
        if (c.contains(s + ".amount")) builder.amount(c.getInt(s + ".amount"));
        if (c.contains(s + ".displayname")) builder.displayname(format(c.getString(s + ".displayname"), player));

        if (c.contains(s + ".lore")) {
            List<String> lore = format(c.getStringList(s + ".lore"),player);
            for (String s1 : lore) {
                if (punishment!=null) {
                    s1=s1.replace("%name%", String.valueOf(punishment.getName()));
//                    s1=s1.replace("%type%", String.valueOf(punishment.getPunishmentMap().get(0).getType()));

                    int progress = punishment.getPlayerData().getPunishmentHistory().getPunishments(punishment.getName());
                    if (progress > punishment.getPunishmentMap().keySet().size()) progress = punishment.getPunishmentMap().keySet().size()-1;
                    for (Integer i : punishment.getPunishmentMap().keySet()) {
                        String type = punishment.getPunishmentMap().get(i).getType();
                        type = type.substring(0, 1).toUpperCase() + type.substring(1);

                        s1=s1.replace("%punishment" + i + "type%", type);
                        s1=s1.replace("%punishment" + i + "reason%", punishment.getPunishmentMap().get(i).getReason());
                        s1=s1.replace("%punishment" + i + "duration%", punishment.getPunishmentMap().get(i).getDuration());
                        s1=s1.replace("%punishment" + i + "tier%", punishment.getPunishmentMap().get(i).getTier()+"");
                        s1=s1.replace("%punishment" + i + "color%", progress > i ? "<color:yellow>" : "<color:red>");
                        s1=s1.replace("%punishment" + i + "next%", (progress+1 == i ? Staff.getMessagesConfig().getNextMessage() : "") + Staff.getMessagesConfig().getOffenseMessage().replace("%number%", String.valueOf(i)));
                    }
                    builder.replaceAndSymbol(false);
                    builder.lore(s1);
                    builder.replaceAndSymbol(true);

                } else {
                    for (int i = 0; i < 7; i++) {
                        s1=s1.replace("%punishment" + i + "type%", "");
                        s1=s1.replace("%punishment" + i + "reason%", "");
                        s1=s1.replace("%punishment" + i + "duration%", "");
                        s1=s1.replace("%punishment" + i + "tier%", "");
                        s1=s1.replace("%punishment" + i + "color%", "");
                        s1=s1.replace("%punishment" + i + "next%", "");
                    }
                    builder.lore(s1);
                }
            }
        }
        if (c.getBoolean(s + ".glow")) builder.glow();

        return builder.build();
    }



    public static String format(String s,Player player) {
        if (player!=null) s=PlaceholderAPI.setPlaceholders(player,s);
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> format(List<String> lore,Player player) {
        List<String> l = new ArrayList<>();
        for (String s : lore) {
            if (player!=null) s=PlaceholderAPI.setPlaceholders(player,s);
            l.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return l;
    }

}
