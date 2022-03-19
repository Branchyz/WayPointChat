package me.branchyz.waypointchat.runnable;

import me.branchyz.waypointchat.WayPointChat;
import me.branchyz.waypointchat.command.plugin.CountdownCommand;
import me.branchyz.waypointchat.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {
    private final WayPointChat plugin;
    private final CountdownCommand cmd;

    private int current; //Sec
    private final int max;

    private final BossBar bar;

    private final String title;

    private final String[] action;

    private double progress = 1.0;

    public Countdown(String title, BarStyle style, BarColor color, int targetSec, String[] action, WayPointChat plugin, CountdownCommand cmd) {
        this.plugin = plugin;
        this.cmd = cmd;

        this.current = targetSec;
        this.max = targetSec;

        this.title = title;

        this.action = action;

        this.bar = Bukkit.createBossBar(title, color, style);
        this.runTaskTimerAsynchronously(plugin, 20, 20);
    }

    public void stop() {
        cancel();
        bar.setVisible(false);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if(!bar.getPlayers().contains(p)) bar.addPlayer(p);
        }

        if(!bar.isVisible()) bar.setVisible(true);

        current--;

        final int sec = current % 60;
        final int min = (current / 60)%60;
        final int hours = (current/60)/60;

        final String strSec= (sec<10) ? "0" + sec : Integer.toString(sec);
        final String strMin= (min<10) ? "0" + min : Integer.toString(min);
        final String strHours= (hours<10) ? "0" + hours : Integer.toString(hours);

        final String format = plugin.getConfig().getString("countdown-format", "%countdown-name%: %time%");
        final String title = format.replace("%countdown-name%", this.title)
                .replace("%time%", strHours + ":" + strMin + ":" + strSec);

        bar.setTitle(title);
        bar.setProgress(progress);
        progress = progress - (1.0/max);


        if(current <= 0) {
            final String broadcast = Messages.COUNTDOWN_ENDED.toString().replace("%countdown-name%", this.title);
            Bukkit.broadcastMessage(broadcast);

            for (String action : action) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action);
                    }
                }.runTask(plugin);
            }
            bar.setVisible(false);
            cancel();
            cmd.getCountdowns().remove(this.title);
        }
    }
}
