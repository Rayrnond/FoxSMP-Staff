package com.reflexian.staff;

import com.reflexian.staff.commands.staff.StaffParentCommand;
import com.reflexian.staff.utilities.config.DefaultConfig;
import com.reflexian.staff.utilities.config.MessagesConfig;
import com.reflexian.staff.utilities.data.DatabaseService;
import com.reflexian.staff.utilities.data.player.PlayerService;
import com.reflexian.staff.utilities.inventory.InvUtils;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import fr.minuskube.inv.InventoryManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import pl.mikigal.config.ConfigAPI;
import pl.mikigal.config.style.CommentStyle;
import pl.mikigal.config.style.NameStyle;

@Getter
public final class Staff extends JavaPlugin {

    @Setter private int slowmode = 0;

    @Getter private static Staff instance;
    @Getter private static PlayerService playerService;
    @Getter private static DatabaseService databaseService;
    @Getter private static DefaultConfig configuration;
    @Getter private static MessagesConfig messagesConfig;
    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        instance=this;

        configuration = ConfigAPI.init(DefaultConfig.class, NameStyle.UNDERSCORE, CommentStyle.ABOVE_CONTENT, true, this);
        messagesConfig = ConfigAPI.init(MessagesConfig.class, NameStyle.UNDERSCORE, CommentStyle.ABOVE_CONTENT, true, this);

        playerService = new PlayerService();
        databaseService = new DatabaseService();
        databaseService.initializeDataSource();


        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        InvUtils.init();


        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
        CommandAPI.onEnable();

        new StaffParentCommand().register();
//        converter = new ConverterImpl();

//        this.loadConfigs();
//        getCommand("cosmetics").setExecutor(new ShopCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
