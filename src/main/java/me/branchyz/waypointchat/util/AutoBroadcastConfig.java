package me.branchyz.waypointchat.util;

import me.branchyz.waypointchat.WayPointChat;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class AutoBroadcastConfig {
    private static File file;
    private static FileConfiguration conf;

    public static void setup(WayPointChat plugin){
        file = new File(plugin.getDataFolder(), "auto-broadcast.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
                plugin.saveResource("auto-broadcast.yml", true);
            } catch (IOException e) {
                e.printStackTrace();
                plugin.disable();
            }
        }
        conf = YamlConfiguration.loadConfiguration(file);
        conf.options().setHeader(Arrays.asList(" WayPointChat",
                " Chat Manager",
                " Author: Waypoint (Branchyz)",
                "",
                " Notes:",
                " Use https://mapmaking.fr/tick/ for auto-broadcast interval calculation."));
        save(plugin);
    }

    public static FileConfiguration get(){
        return conf;
    }

    public static void save(WayPointChat plugin) {
        try {
            conf.save(file);
        } catch(IOException e) {
            e.printStackTrace();
            plugin.disable();
        }
    }

    public static void reload(){
        conf = YamlConfiguration.loadConfiguration(file);
    }
}
