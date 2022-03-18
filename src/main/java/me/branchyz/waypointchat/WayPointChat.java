package me.branchyz.waypointchat;

import me.branchyz.waypointchat.command.info.InfoCommandManager;
import me.branchyz.waypointchat.command.plugin.BroadcastCommand;
import me.branchyz.waypointchat.command.plugin.MuteChatCommand;
import me.branchyz.waypointchat.listener.ChatListener;
import me.branchyz.waypointchat.listener.JoinListener;
import me.branchyz.waypointchat.util.Messages;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;

public final class WayPointChat extends JavaPlugin {
    private boolean chatMuted = false;

    @Override
    public void onEnable() {
        getDataFolder().mkdir();
        // Plugin startup logic
        Messages.initialize(this);
        getConfig().options().setHeader(Arrays.asList(" WayPointChat",
                " Chat Manager",
                " Author: Waypoint (Branchyz)",
                " Notes:",
                "",
                " ALL Messages/Strings are in messages.yml (Except info commands output)"));
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        InfoCommandManager.initialize(this);

        getCommand("broadcast").setExecutor(new BroadcastCommand());
        getCommand("mutechat").setExecutor(new MuteChatCommand(this));

        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new JoinListener(this), this);
        pluginManager.registerEvents(new ChatListener(this), this);
    }

    @Override
    public void onDisable() {
        InfoCommandManager.unregisterCommands(this);
    }

    public void disable() {
        this.setEnabled(false);
    }

    public WayPointChat log(final Object log, final Level level) {
        getLogger().log(level, log.toString());
        return this;
    }

    public boolean isChatMuted() {
        return chatMuted;
    }

    public void setChatMuted(boolean chatMuted) {
        this.chatMuted = chatMuted;
    }
}
