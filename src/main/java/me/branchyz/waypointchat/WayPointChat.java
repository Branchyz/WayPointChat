package me.branchyz.waypointchat;

import me.branchyz.waypointchat.command.info.InfoCommandManager;
import me.branchyz.waypointchat.command.plugin.*;
import me.branchyz.waypointchat.listener.ChatListener;
import me.branchyz.waypointchat.listener.JoinListener;
import me.branchyz.waypointchat.runnable.AutoBroadcast;
import me.branchyz.waypointchat.runnable.Countdown;
import me.branchyz.waypointchat.util.ActionsConfig;
import me.branchyz.waypointchat.util.Messages;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

public final class WayPointChat extends JavaPlugin {
    private boolean chatMuted = false;
    private CountdownCommand countdownCommand;

    @Override
    public void onEnable() {
        getDataFolder().mkdir();
        // Plugin startup logic
        Messages.initialize(this);
        setupConfig("Config loaded!", false);
        ActionsConfig.setup(this);
        ActionsConfig.get().options().copyDefaults(true);
        ActionsConfig.save(this);
        InfoCommandManager.initialize(this);

        countdownCommand = new CountdownCommand(this);

        getCommand("broadcast").setExecutor(new BroadcastCommand());
        getCommand("mutechat").setExecutor(new MuteChatCommand(this));
        getCommand("clearchat").setExecutor(new ClearChatCommand(this));
        getCommand("countdown").setExecutor(countdownCommand);

        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JoinListener(this), this);
        pm.registerEvents(new ChatListener(this), this);

        AutoBroadcast.initialize(this);
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

    public void setupConfig(String msg, boolean reload) {
        getConfig().options().setHeader(Arrays.asList(" WayPointChat",
                " Chat Manager",
                " Author: Waypoint (Branchyz)",
                " Notes:",
                "",
                " All Messages are in messages.yml (Except info commands output & auto broadcasts)",
                " Use https://mapmaking.fr/tick/ for auto-broadcast interval calculation."));
        getConfig().options().copyDefaults(true);
        getConfig().setInlineComments("countdown-format", Arrays.asList("Placeholders: %time% & %countdown-name%"));
        saveDefaultConfig();
        if(reload) reloadConfig();
        log(msg, Level.INFO);
    }

}
