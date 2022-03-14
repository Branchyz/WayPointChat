package me.branchyz.waypointchat;

import me.branchyz.waypointchat.util.Messages;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class WayPointChat extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        getConfig().options().setHeader(Arrays.asList(" WayPointChat",
                " Chat Manager",
                " Author: Waypoint (Branchyz)",
                " Notes:",
                "",
                " ALL Messages are in messages.yml"));
        Messages.initialize(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void disable() {
        this.setEnabled(false);
    }
}
