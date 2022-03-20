package me.branchyz.waypointchat.util;

import me.branchyz.waypointchat.WayPointChat;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class CountdownConfig {
    private static File file;
    private static FileConfiguration conf;

    public static void setup(WayPointChat plugin){
        file = new File(plugin.getDataFolder(), "countdown.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
                plugin.saveResource("countdown.yml", true);
            } catch (IOException e) {
                e.printStackTrace();
                plugin.disable();
            }
        }
        conf = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get(){
        return conf;
    }

    public static void reload(){
        conf = YamlConfiguration.loadConfiguration(file);
    }
}
