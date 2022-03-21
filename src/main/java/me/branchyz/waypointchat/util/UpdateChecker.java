package me.branchyz.waypointchat.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {
    private static void getVersion(int id, JavaPlugin plugin, final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + id).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                plugin.getLogger().info("Unable to check for updates: " + exception.getMessage());
            }
        });
    }

    public static boolean hasUpdate(int id, JavaPlugin plugin) {
        final boolean[] result = new boolean[1];
        getVersion(id, plugin, version -> {
            result[0] =  plugin.getDescription().getVersion().equalsIgnoreCase(version);
        });

        return result[0];
    }
}
