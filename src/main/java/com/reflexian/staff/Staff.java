package com.reflexian.staff;

import com.reflexian.staff.commands.staff.StaffParentCommand;
import com.reflexian.staff.listeners.ChatListener;
import com.reflexian.staff.listeners.JoinListener;
import com.reflexian.staff.listeners.MovementListener;
import com.reflexian.staff.utilities.config.DefaultConfig;
import com.reflexian.staff.utilities.config.MessagesConfig;
import com.reflexian.staff.utilities.data.AutosaveRunnable;
import com.reflexian.staff.utilities.data.DatabaseService;
import com.reflexian.staff.utilities.data.FilterService;
import com.reflexian.staff.utilities.data.player.PlayerService;
import com.reflexian.staff.utilities.inventory.InvUtils;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import fr.minuskube.inv.InventoryManager;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.mikigal.config.ConfigAPI;
import pl.mikigal.config.style.CommentStyle;
import pl.mikigal.config.style.NameStyle;

import java.io.File;
import java.io.InputStreamReader;

@Getter
public final class Staff extends JavaPlugin {

    @Setter private int slowmode = 0;

    @Getter private static Staff instance;
    @Getter private static PlayerService playerService;
    @Getter private static DatabaseService databaseService;
    @Getter private static DefaultConfig configuration;
    @Getter private static MessagesConfig messagesConfig;
    @Getter private static YamlConfiguration punishments;
    private final AutosaveRunnable autosaveRunnable = new AutosaveRunnable();
    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        instance=this;

        configuration = ConfigAPI.init(DefaultConfig.class, NameStyle.UNDERSCORE, CommentStyle.ABOVE_CONTENT, true, this);
        messagesConfig = ConfigAPI.init(MessagesConfig.class, NameStyle.UNDERSCORE, CommentStyle.ABOVE_CONTENT, true, this);

        // load punishments.yml if not exists
        if (!new File(getDataFolder()+"/punishments.yml").exists()) {
            saveResource("punishments.yml",false);
        }
        punishments = YamlConfiguration.loadConfiguration(new File(getDataFolder()+"/punishments.yml"));


        playerService = new PlayerService();
        databaseService = new DatabaseService();
        databaseService.initializeDataSource();


        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        checkInvFile("punish.yml");
        checkInvFile("category-chat.yml");
        checkInvFile("category-custom.yml");
        checkInvFile("category-game.yml");

        InvUtils.init();


        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
        CommandAPI.onEnable();

        new StaffParentCommand().register();

        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new MovementListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);

        autosaveRunnable.runTaskTimerAsynchronously(this,0L,1000*60*5);

        FilterService.load();

    }

    @SneakyThrows
    @Override
    public void onDisable() {
        autosaveRunnable.run();
        databaseService.getConnection().close();
    }


    private void checkInvFile(String file) {
        File configFile = new File(getDataFolder()+ "/inventories", file);
        YamlConfiguration c;
        if (!configFile.exists()) {
            try {
                c = YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource("inventories/"+file), "UTF8"));
                c.save(getDataFolder() + "/inventories" + File.separator + file);
            } catch (Exception e) {
                getLogger().warning("Unable to save " +configFile.getName()+"!");
            }
            getLogger().info("Generated " + configFile.getName()+"!");
        }
    }
}
