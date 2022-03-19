package me.branchyz.waypointchat.runnable;

import me.branchyz.waypointchat.WayPointChat;
import me.branchyz.waypointchat.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.temporal.ChronoUnit;

public class Countdown extends BukkitRunnable {
    private final WayPointChat plugin;

    private int current; //Sec
    private final int max;

    private final BossBar bar;

    private final String title;

    private final String[] actions;

    private Countdown(String title, BarStyle style, BarColor color, int targetSec, String[] actions, WayPointChat plugin) {
        this.plugin = plugin;

        this.current = targetSec;
        this.max = targetSec;

        this.title = title;

        this.actions = actions;

        this.bar = Bukkit.createBossBar(title, color, style);
        this.runTaskTimerAsynchronously(plugin, 20, 20);
    }

    public String getTitle() {
        return title;
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
        bar.setProgress((current*100.0) / max);

        if(current <= 0) {
            final String broadcast = Messages.COUNTDOWN_ENDED.toString().replace("%countdown-name%", title);
            Bukkit.broadcastMessage(broadcast);

            for (String action : actions) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action);
            }
            cancel();
        }
    }
}
