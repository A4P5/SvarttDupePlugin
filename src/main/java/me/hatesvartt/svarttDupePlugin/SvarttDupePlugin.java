// SvarttDupePlugin.java

package me.hatesvartt.svarttDupePlugin;

import me.hatesvartt.svarttDupePlugin.commands.DupeCommand;
import me.hatesvartt.svarttDupePlugin.dupes.PortalDonkeyDupe;
import me.hatesvartt.svarttDupePlugin.dupes.ItemFrameDupe;
import me.hatesvartt.svarttDupePlugin.dupes.PistonDupe;
import me.hatesvartt.svarttDupePlugin.listeners.OnItemFrameBreakListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class SvarttDupePlugin extends JavaPlugin {

    private static final String RESET = "\u001B[0m";
    private static final String PURPLE = "\u001B[35m";
    private static SvarttDupePlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        Objects.requireNonNull(getCommand("dupe")).setExecutor(new DupeCommand(this));

        if (getCommand("dupe") == null) {
            getLogger().severe("[SvarttDupePlugin] ⚠ Command 'dupe' not found!");
        }

        getLogger().info(PURPLE + "SvarttDupePlugin" + RESET + " initialized!");
        getLogger().info(" ~ Developed by " + PURPLE + "Hatesvartt" + RESET + " ~");

        // Initialize dupe handlers
        ItemFrameDupe itemFrameDupe = new ItemFrameDupe(this);
        PistonDupe pistonDupe = new PistonDupe(this);
        PortalDonkeyDupe endPortalDonkeyDupe = new PortalDonkeyDupe(this);

        // Register listeners
        getServer().getPluginManager().registerEvents(pistonDupe, this);
        getServer().getPluginManager().registerEvents(endPortalDonkeyDupe, this);
        getServer().getPluginManager().registerEvents(
                new OnItemFrameBreakListener(itemFrameDupe, pistonDupe),
                this
        );
    }

    @Override
    public void onDisable() {
        getLogger().info("SvarttDupePlugin disabled.");
    }

    public static SvarttDupePlugin getInstance() {
        return instance;
    }
}

