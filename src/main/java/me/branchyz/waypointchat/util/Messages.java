package me.branchyz.waypointchat.util;

import me.branchyz.waypointchat.WayPointChat;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

public enum Messages {
    PREFIX("prefix", "&6[WayPointChat]&r "),
    BROADCAST_PREFIX("broadcast-prefix", "&6[Broadcast]&r "),
    NO_PERMS("no-permission", "&cYou don't have permission for that!"),
    NOT_A_PLAYER("not-a-player", "&cYou need to be a player do that!"),
    INVALID_USAGE("invalid-usage", "&cInvalid usage! Usage: %usage%"),
    TITLE_MOTD("title-motd", "&6Welcome to the server!"),
    SUBTITLE_MOTD("subtitle-motd", "&6%player%"),
    CURSE_WORD_USED("curse-word-used", "&cPlease do not curse in chat!"),
    CURSE_WORD_ALERT("curse-word-alert", "&c%player% tried to use a curse word (%curse-word%)!"),
    CHAT_IS_MUTED("chat-is-muted", "&cThe chat is muted!"),
    CHAT_MUTED_TOGGLE_ON("chat-muted-toggle-on", "&6The chat is now muted!"),
    CHAT_MUTED_TOGGLE_OFF("chat-muted-toggle-off", "&6The chat is now unmuted!"),
    CHAT_IS_CLEARED("chat-is-cleared", "&6The chat has been cleared!"),
    COUNTDOWN_ENDED("countdown-ended", "&6The countdown %countdown-name% has ended!"),
    COUNTDOWN_COMMAND_HELP("countdown-command-help", "&cUse \"%start-usage%\" or \"%stop-usage%\""),
    ACTION_NOT_FOUND("action-not-found", "&cNo action found with the name %action-name%!"),
    COUNTDOWN_STARTED("countdown-started", "&6The countdown has started!"),
    COUNTDOWN_STOPPED("countdown-stopped", "&6The countdown has been stopped!"),
    COUNTDOWN_ALREADY_EXIST("countdown-already-exist", "&cThat countdown already exist!"),
    COUNTDOWN_DOES_NOT_EXIST("countdown-does-not-exist", "&cThat countdown doesn't exist!");

    private String path;
    private String def;
    private static YamlConfiguration config;

    Messages(String path, String start) {
        this.path = path;
        this.def = start;
    }

    public static void initialize(WayPointChat plugin) {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
                plugin.saveResource("messages.yml", true);
            } catch (IOException e) {
                e.printStackTrace();
                plugin.disable();
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
        for(Messages msg : Messages.values()) {
            if (conf.getString(msg.getPath()) == null)
                plugin.log("Message " + msg.getPath() + " not found!", Level.WARNING);
        }

        config = conf;
    }

    @Override
    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', config.getString(this.path, def));
    }

    public String getDefault() {
        return this.def;
    }

    public String getPath() {
        return this.path;
    }

    public static String getPluginPrefix() {
        return PREFIX.getDefault();
    }
}
