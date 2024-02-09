package com.reflexian.staff.utilities.data.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor@NoArgsConstructor@Builder@Getter
public class Punishment {

    private int tier;
    private String type;
    private String reason;
    private String duration;
    private boolean ip=false;
    private PlayerData playerData = null;

    @Override
    public String toString() {
        return "Punishment{" +
                "tier=" + tier +
                ", type='" + type + '\'' +
                ", reason='" + reason + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }

    //5742bf6a-950c-11ee-b9d1-0242ac120002
    public String toCommand(String name) {
        // ban (player) (reason) --sender-uuid=<uuid> --sender=PunishmentSystem

        String duration = getDuration();
        if (duration.equalsIgnoreCase("permanent")) duration = "";

        return type+" "+name+" "+reason+" " + duration;
    }
}
