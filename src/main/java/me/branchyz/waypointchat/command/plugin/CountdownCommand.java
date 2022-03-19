package me.branchyz.waypointchat.command.plugin;

import me.branchyz.waypointchat.WayPointChat;
import me.branchyz.waypointchat.runnable.Countdown;
import me.branchyz.waypointchat.util.CountdownConfig;
import me.branchyz.waypointchat.util.Messages;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class CountdownCommand implements CommandExecutor {
    private static final String START_USAGE = "/%label% start <countdown-title> <seconds> <action>";
    private static final String STOP_USAGE = "/%label% stop <countdown-title>";

    private final HashMap<String, Countdown> countdowns = new HashMap<>();

    private final WayPointChat plugin;

    public HashMap<String, Countdown> getCountdowns() {
        return countdowns;
    }

    public CountdownCommand(WayPointChat plugin) {this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("waypoint.countdown")) {
            sender.sendMessage(Messages.PREFIX.toString() + Messages.NO_PERMS);
            return true;
        }

        if(!(args.length >= 1)) {
            final String stopUsage = STOP_USAGE.replace("%label%", label);
            final String startUsage = START_USAGE.replace("%label%", label);

            sender.sendMessage(Messages.PREFIX + Messages.COUNTDOWN_COMMAND_HELP.toString().
                    replace("%start-usage%", startUsage).replace("%stop-usage%", stopUsage));
            return true;
        }

        String subCmd = args[0];
        if(subCmd.equalsIgnoreCase("start")) {
            executeStartSubCommand(sender, command, label, args);
        } else if(subCmd.equalsIgnoreCase("stop")) {
            executeStopSubCommand(sender, command, label, args);
        } else {
            final String stopUsage = STOP_USAGE.replace("%label%", label);
            final String startUsage = START_USAGE.replace("%label%", label);

            sender.sendMessage(Messages.PREFIX + Messages.COUNTDOWN_COMMAND_HELP.toString().
                    replace("%start-usage%", startUsage).replace("%stop-usage%", stopUsage));
            return true;
        }

        return true;
    }

    private void executeStartSubCommand(CommandSender sender, Command command, String label, String[] args) {
        final String usage = Messages.INVALID_USAGE.toString()
                .replace("%usage%", START_USAGE.replace("%label%", label));

        if(!(args.length >= 4)) {
            sender.sendMessage(Messages.PREFIX + usage);
            return;
        }

        if(!isInteger(args[2])) {
            sender.sendMessage(Messages.PREFIX + usage);
            return;
        }

        final String title = args[1];
        if(countdowns.containsKey(title)) {
            sender.sendMessage(Messages.PREFIX.toString() + Messages.COUNTDOWN_ALREADY_EXIST);
            return;
        }

        final int sec = Integer.parseInt(args[2]);

        final String actionKey = args[3];
        if(!CountdownConfig.get().contains(actionKey)) {
            sender.sendMessage(Messages.PREFIX + Messages.ACTION_NOT_FOUND.toString().replace("%action-name%", actionKey));
            return;
        }

        final String[] action = CountdownConfig.get().getStringList(actionKey).toArray(new String[0]);

        countdowns.put(title, new Countdown(title, BarStyle.SOLID, BarColor.WHITE, sec, action, plugin, this));
        sender.sendMessage(Messages.PREFIX.toString() + Messages.COUNTDOWN_STARTED);
    }

    private void executeStopSubCommand(CommandSender sender, Command command, String label, String[] args) {
        final String usage = Messages.INVALID_USAGE.toString()
                .replace("%usage%", STOP_USAGE.replace("%label%", label));

        if(!(args.length >= 2)) {
            sender.sendMessage(Messages.PREFIX + usage);
            return;
        }

        final String title = args[1];
        if(!countdowns.containsKey(title)) {
            sender.sendMessage(Messages.PREFIX.toString() + Messages.COUNTDOWN_DOES_NOT_EXIST);
            return;
        }

        countdowns.get(title).stop();
        countdowns.remove(title);
        sender.sendMessage(Messages.PREFIX.toString() + Messages.COUNTDOWN_STOPPED);
    }

    private boolean isInteger(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException ex)
        {
            return false;
        }
    }
}
