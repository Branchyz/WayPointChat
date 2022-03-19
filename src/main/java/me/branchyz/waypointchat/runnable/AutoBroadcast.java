package me.branchyz.waypointchat.runnable;

import me.branchyz.waypointchat.WayPointChat;
import me.branchyz.waypointchat.util.AutoBroadcastConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.logging.Level;

public class AutoBroadcast extends BukkitRunnable {
    private final String[] messages;
    private static ArrayList<AutoBroadcast> broadcasts = new ArrayList<>();

    private AutoBroadcast(String[] messages, int interval, int start, WayPointChat plugin) {
        this.messages = messages;
        this.runTaskTimerAsynchronously(plugin, start, interval);
    }

    @Override
    public void run() {
        for (String msg : messages) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }

    public static void initialize(WayPointChat plugin) {
        final ConfigurationSection sec = AutoBroadcastConfig.get().getConfigurationSection("auto-broadcast");
        for (String key : sec.getKeys(false)) {
            if (!(sec.contains(key + ".start") && sec.contains(key + ".interval") && sec.contains(key + ".messages")))
                continue;
            final int start = sec.getInt(key + ".start");
            final int interval = sec.getInt(key + ".interval");
            final String[] messages = sec.getStringList(key + ".messages").toArray(new String[0]);

            broadcasts.add(new AutoBroadcast(messages, interval, start, plugin));
            plugin.log("Registered the \"" + key + "\" auto-broadcast!", Level.INFO);
        }
    }

    public static void disable(WayPointChat plugin) {
        for (AutoBroadcast broadcast : broadcasts) {
            broadcast.cancel();
        }
        plugin.log("Unregistered auto-broadcasts!", Level.INFO);
        broadcasts.clear();
    }
}
