package me.branchyz.waypointchat.listener;

import me.branchyz.waypointchat.WayPointChat;
import me.branchyz.waypointchat.util.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private final WayPointChat plugin;

    public JoinListener(WayPointChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final String title = Messages.TITLE_MOTD.toString().replace("%player%", p.getName());
        final String subtitle = Messages.SUBTITLE_MOTD.toString().replace("%player%", p.getName());
        final int fadeIn = plugin.getConfig().getInt("motd-title.fade-in");
        final int stay = plugin.getConfig().getInt("motd-title.stay");
        final int fadeOut = plugin.getConfig().getInt("motd-title.fade-out");

        p.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

}
