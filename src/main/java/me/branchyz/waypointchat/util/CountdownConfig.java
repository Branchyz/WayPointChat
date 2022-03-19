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
        conf.options().setHeader(Arrays.asList(" WayPointChat",
                " Chat Manager",
                " Author: Waypoint (Branchyz)",
                "",
                " Usage:",
                " /cd start <countdown-title> <seconds> <action>",
                " /cd stop <countdown-title>",
                " <countdown-title> = Message in bossbar (including colorcode).",
                " <seconds> = time in seconds",
                " <action> = Action is defined below, use the key here.",
                "",
                " Actions:",
                " Actions are directly perfomed by the console. No slash (/) needed.",
                " You can use any command here, for example; give, exp, chatmute etc."));

        conf.setInlineComments("countdown-format", Arrays.asList("Placeholders: %time% & %countdown-name% (Don't forget that you can also use color codes here!)"));
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
