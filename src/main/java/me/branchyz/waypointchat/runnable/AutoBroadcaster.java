package me.branchyz.waypointchat.runnable;

import me.branchyz.waypointchat.WayPointChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class AutoBroadcaster extends BukkitRunnable {
    private final String[] messages;
    private final int interval;
    private final int start;

    private AutoBroadcaster(String[] messages, int interval, int start, WayPointChat plugin) {
        this.messages = messages;
        this.interval = interval;
        this.start = start;
        this.runTaskTimerAsynchronously(plugin, start, interval);
    }

    @Override
    public void run() {
        for (String msg : messages) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }

    public static void initialize(WayPointChat plugin) {
        final ConfigurationSection sec = plugin.getConfig().getConfigurationSection("auto-broadcast");
        for (String key : sec.getKeys(false)) {
            if (!(sec.contains(key + ".start") && sec.contains(key + ".interval") && sec.contains(key + ".messages")))
                continue;
            final int start = sec.getInt(key + ".start");
            final int interval = sec.getInt(key + ".interval");
            final String[] messages = sec.getStringList(key + ".messages").toArray(new String[0]);

            new AutoBroadcaster(messages, interval, start, plugin);
            plugin.log("Registered the \"" + key + "\" auto-broadcast!", Level.INFO);
        }
    }
}
