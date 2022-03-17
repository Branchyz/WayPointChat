package me.branchyz.waypointchat.command.plugin;

import me.branchyz.waypointchat.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BroadcastCommand implements CommandExecutor {
    private static final String USAGE = "/%label% <message>";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("waypoint.broadcast")) {
            sender.sendMessage(Messages.PREFIX.toString() + Messages.NO_PERMS);
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage(Messages.PREFIX + Messages.INVALID_USAGE.toString().replace("%usage%", USAGE).replace("%label%", label));
            return true;
        }

        StringBuilder msg = new StringBuilder(Messages.BROADCAST_PREFIX.toString());
        for (String arg : args) {
            msg.append(arg).append(" ");
        }

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg.toString()));
        return false;
    }
}
