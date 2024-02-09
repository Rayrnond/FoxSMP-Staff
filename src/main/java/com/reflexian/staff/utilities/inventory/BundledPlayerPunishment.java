package com.reflexian.staff.utilities.inventory;

import com.reflexian.staff.utilities.data.player.PlayerData;
import com.reflexian.staff.utilities.data.player.Punishment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Builder@AllArgsConstructor@Getter@Setter
public class BundledPlayerPunishment {

    private String name;
    private PlayerData playerData;
    private Map<Integer, Punishment> punishmentMap=new HashMap<>();


}
