package me.branchyz.waypointchat.command.plugin;

import me.branchyz.waypointchat.WayPointChat;
import me.branchyz.waypointchat.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.stream.IntStream;

public class ClearChatCommand implements CommandExecutor {
    private final WayPointChat plugin;

    public ClearChatCommand(WayPointChat plugin) {this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("waypoint.clearchat")) {
            sender.sendMessage(Messages.PREFIX.toString() + Messages.NO_PERMS);
            return true;
        }

        final int numberOfIterations = plugin.getConfig().getInt("clear-chat-iterations", 100);

        IntStream.range(0, numberOfIterations).forEach(i -> Bukkit.broadcastMessage(""));
        Bukkit.broadcastMessage(Messages.PREFIX.toString() + Messages.CHAT_IS_CLEARED);
        return true;
    }
}
