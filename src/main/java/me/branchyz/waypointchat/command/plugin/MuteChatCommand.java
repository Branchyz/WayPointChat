package me.branchyz.waypointchat.command.plugin;

import me.branchyz.waypointchat.WayPointChat;
import me.branchyz.waypointchat.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

public class MuteChatCommand implements CommandExecutor {
    private final WayPointChat plugin;

    public MuteChatCommand(WayPointChat plugin) {this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("waypoint.mutechat")) {
            sender.sendMessage(Messages.PREFIX.toString() + Messages.NO_PERMS);
            return true;
        }

        plugin.setChatMuted(!plugin.isChatMuted());
        sender.sendMessage(Messages.PREFIX.toString() +
                (plugin.isChatMuted() ? Messages.CHAT_MUTED_TOGGLE_ON :
                        Messages.CHAT_MUTED_TOGGLE_OFF));
        return true;
    }
}
