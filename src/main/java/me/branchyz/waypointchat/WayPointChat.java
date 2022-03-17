package me.branchyz.waypointchat;

import me.branchyz.waypointchat.command.plugin.BroadcastCommand;
import me.branchyz.waypointchat.listener.JoinListener;
import me.branchyz.waypointchat.util.Messages;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class WayPointChat extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        getConfig().options().setHeader(Arrays.asList(" WayPointChat",
                " Chat Manager",
                " Author: Waypoint (Branchyz)",
                " Notes:",
                "",
                " ALL Messages/Strings are in messages.yml"));
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        Messages.initialize(this);

        getCommand("broadcast").setExecutor(new BroadcastCommand());

        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new JoinListener(this), this);
    }

    public void disable() {
        this.setEnabled(false);
    }
}
