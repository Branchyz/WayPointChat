package me.branchyz.waypointchat.command.info;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import me.branchyz.waypointchat.WayPointChat;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class InfoCommandManager {
    private static FileConfiguration config;
    private static ArrayList<CommandAPICommand> commands = new ArrayList<>();

    public static void initialize(WayPointChat plugin, boolean register) {
        initializeFile(plugin);
        if(register)
            registerCommands(plugin);
    }

    private static void registerCommands(WayPointChat plugin) {
        for (String key : config.getKeys(false)) {
            if(!(config.contains(key + ".command") && config.contains(key + ".aliases") && config.contains(key + ".output")  && config.contains(key + ".permission"))) continue;
            final String command = config.getString(key + ".command");
            final String[] aliases = config.getStringList(key + ".aliases").toArray(new String[0]);
            final String[] output = config.getStringList(key + ".output").toArray(new String[0]);
            final String permission = config.getString(key + ".permission");

            registerCommand(command, aliases, output, permission);
            plugin.log("Registered the \"" + command + "\" command!", Level.INFO);
        }
    }

    private static void registerCommand(String command, String[] aliases, String[] output, String permission) {
        final CommandAPICommand cmd = new CommandAPICommand(command);

        cmd.withAliases(aliases).withPermission("waypoint." + permission)
                .executes((executor, args) -> {
                    for (String s : output) {
                        final String msg = ChatColor.translateAlternateColorCodes('&', s);
                        executor.sendMessage(msg);
                    }
                })
                .register();
        commands.add(cmd);
    }

    public static void unregisterCommands(WayPointChat plugin) {
        for (CommandAPICommand cmd : commands) {
            CommandAPI.unregister(cmd.getName());
            plugin.log("Unregistered the \"" + cmd.getName() + "\" command!", Level.INFO);
        }
    }

    private static void initializeFile(WayPointChat plugin) {
        File file = new File(plugin.getDataFolder(), "commands.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
                plugin.saveResource("commands.yml", true);
            } catch (IOException e) {
                e.printStackTrace();
                plugin.disable();
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

        config = conf;
    }


}
