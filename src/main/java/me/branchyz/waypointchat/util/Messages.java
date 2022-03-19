package me.branchyz.waypointchat.util;

import me.branchyz.waypointchat.WayPointChat;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public enum Messages {
    PREFIX("prefix", "&6[WayPointChat]&r ", new String[0]),
    BROADCAST_PREFIX("broadcast-prefix", "&6[Broadcast]&r ", new String[0]),
    NO_PERMS("no-permission", "&cYou don't have permission for that!", new String[0]),
    NOT_A_PLAYER("not-a-player", "&cYou need to be a player do that!", new String[0]),
    INVALID_USAGE("invalid-usage", "&cInvalid usage! Usage: %usage%", new String[]{"Placeholders: %usage%"}),
    TITLE_MOTD("title-motd", "&6Welcome to the server!", new String[]{"Placeholders: %player%"}),
    SUBTITLE_MOTD("subtitle-motd", "&6%player%", new String[]{"Placeholders: %player%"}),
    CURSE_WORD_USED("curse-word-used", "&cPlease do not curse in chat!", new String[0]),
    CURSE_WORD_ALERT("curse-word-alert", "&c%player% tried to use a curse word (%curse_word%)!", new String[]{"Placeholders: %player% & %curse_word%"}),
    CHAT_IS_MUTED("chat-is-muted", "&cThe chat is muted!", new String[0]),
    CHAT_MUTED_TOGGLE_ON("chat-muted-toggle-on", "&6The chat is now muted!", new String[0]),
    CHAT_MUTED_TOGGLE_OFF("chat-muted-toggle-off", "&6The chat is now unmuted!", new String[0]),
    CHAT_IS_CLEARED("chat-is-cleared", "&6The chat has been cleared!", new String[0]),
    COUNTDOWN_ENDED("countdown-ended", "&6The countdown %countdown-name% has ended!", new String[]{"Placeholders: %countdown-name%"}),
    COUNTDOWN_COMMAND_HELP("countdown-command-help", "&cUse \"%start-usage%\" or \"%stop-usage%\"", new String[]{"Placeholders: %start-usage% & %stop-usage%"}),
    ACTION_NOT_FOUND("action-not-found", "&cNo action found with the name %action-name%!", new String[]{"Placeholders: %action-name%"}),
    COUNTDOWN_STARTED("countdown-started", "&6The countdown has started!", new String[0]),
    COUNTDOWN_STOPPED("countdown-stopped", "&6The countdown has been stopped!", new String[0]),
    COUNTDOWN_ALREADY_EXIST("countdown-already-exist", "&cThat countdown already exist! You can stop it by using the countdown stop command!", new String[0]),
    COUNTDOWN_DOES_NOT_EXIST("countdown-does-not-exist", "&cThat countdown doesn't exist!", new String[0]);

    private String path;
    private String def;
    private String[] comments;
    private static YamlConfiguration config;

    Messages(String path, String start, String[] comments) {
        this.path = path;
        this.def = start;
        this.comments = comments;
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
        conf.options().setHeader(Arrays.asList(" WayPointChat",
                " Chat Manager",
                " Author: Waypoint (Branchyz)"));

        for(Messages item:Messages.values()) {
            if (conf.getString(item.getPath()) == null)
                conf.set(item.getPath(), item.getDefault());

            conf.setInlineComments(item.getPath(), Arrays.asList(item.getComments()));
        }

        config = conf;

        try {
            conf.save(file);
        } catch(IOException e) {
            e.printStackTrace();
            plugin.disable();
        }
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

    public String[] getComments() {
        return comments;
    }

    public static String getPluginPrefix() {
        return PREFIX.getDefault();
    }
}
