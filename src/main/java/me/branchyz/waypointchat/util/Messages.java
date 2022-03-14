package me.branchyz.waypointchat.util;

import me.branchyz.waypointchat.WayPointChat;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public enum Messages {
    PREFIX("prefix", "&6[WayPointChat]&r "),
    NO_PERMS("no-permission", "&cYou don't have permission for that!"),
    TITLE_MOTD("title-motd", "&6Welcome to the server!"),
    SUBTITLE_MOTD("subtitle-motd", "&6%player%");

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
                plugin.saveResource("messages.yml", false);
            } catch (IOException e) {
                e.printStackTrace();
                plugin.disable();
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

        for(Messages item:Messages.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
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
}
