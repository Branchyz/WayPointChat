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
    INVALID_USAGE("invalid-usage", "&cInvalid usage! Usage: %usage%", new String[]{" invalid-usage placeholders: %usage%"}),
    TITLE_MOTD("title-motd", "&6Welcome to the server!", new String[]{" title-motd placeholders: %player%"}),
    SUBTITLE_MOTD("subtitle-motd", "&6%player%", new String[]{" subtitle-motd placeholders: %player%"});

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

            conf.setComments(item.getPath(), Arrays.asList(item.getComments()));
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
}
