package me.branchyz.waypointchat;

import me.branchyz.waypointchat.command.info.InfoCommandManager;
import me.branchyz.waypointchat.command.plugin.*;
import me.branchyz.waypointchat.listener.ChatListener;
import me.branchyz.waypointchat.listener.JoinListener;
import me.branchyz.waypointchat.runnable.AutoBroadcast;
import me.branchyz.waypointchat.runnable.Countdown;
import me.branchyz.waypointchat.util.AutoBroadcastConfig;
import me.branchyz.waypointchat.util.CountdownConfig;
import me.branchyz.waypointchat.util.CurseWordsConfig;
import me.branchyz.waypointchat.util.Messages;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;

public final class WayPointChat extends JavaPlugin {

    private boolean chatMuted = false;
    private CountdownCommand countdownCommand;

    @Override
    public void onEnable() {
        setupConfigs();

        countdownCommand = new CountdownCommand(this);

        getCommand("broadcast").setExecutor(new BroadcastCommand());
        getCommand("mutechat").setExecutor(new MuteChatCommand(this));
        getCommand("clearchat").setExecutor(new ClearChatCommand(this));
        getCommand("countdown").setExecutor(countdownCommand);

        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JoinListener(this), this);
        pm.registerEvents(new ChatListener(this), this);

        AutoBroadcast.initialize(this);

        new Metrics(this, 14695);
    }

    @Override
    public void onDisable() {
        InfoCommandManager.unregisterCommands(this);
        AutoBroadcast.disable(this);
        for (String s : countdownCommand.getCountdowns().keySet()) {
            final Countdown countdown = countdownCommand.getCountdowns().get(s);
            countdown.stop();
            log("Stopped countdown \"" + countdown + "\"!", Level.INFO);
        }
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

    private void setupDefaultConfig() {
        getConfig().options().setHeader(Arrays.asList(" WayPointChat",
                " Chat Manager",
                " Author: Waypoint (Branchyz)"));
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    private void setupConfigs() {
        getDataFolder().mkdir();

        Messages.initialize(this);
        log("messages.yml loaded!", Level.INFO);

        setupDefaultConfig();
        log("config.yml loaded!", Level.INFO);

        CountdownConfig.setup(this);
        CountdownConfig.get().options().copyDefaults(true);
        CountdownConfig.save(this);
        log("countdown.yml loaded!", Level.INFO);

        CurseWordsConfig.setup(this);
        CurseWordsConfig.get().options().copyDefaults(true);
        CurseWordsConfig.save(this);
        log("cursed-words.yml loaded!", Level.INFO);

        AutoBroadcastConfig.setup(this);
        AutoBroadcastConfig.get().options().copyDefaults(true);
        AutoBroadcastConfig.save(this);
        log("auto-broadcast.yml loaded!", Level.INFO);

        if(Bukkit.getPluginManager().getPlugin("CommandAPI") != null)
            InfoCommandManager.initialize(this, true);
        else {
            log("You must have the CommandAPI dependency to use info commands!", Level.WARNING);
            InfoCommandManager.initialize(this, false);
        }
        log("commands.yml loaded!", Level.INFO);
    }
}
