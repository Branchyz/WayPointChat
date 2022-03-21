package me.branchyz.waypointchat.listener;

import me.branchyz.waypointchat.WayPointChat;
import me.branchyz.waypointchat.util.Messages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
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
        final int fadeIn = plugin.getConfig().getInt("motd-title.fade-in", 20);
        final int stay = plugin.getConfig().getInt("motd-title.stay", 60);
        final int fadeOut = plugin.getConfig().getInt("motd-title.fade-out", 20);

        p.sendTitle(title, subtitle, fadeIn, stay, fadeOut);

        if(!e.getPlayer().hasPermission("waypoint.version")) return;
        if(plugin.isLatestVersion()) return;

        final TextComponent msg = new TextComponent(Messages.getPluginPrefix() + format("&cA new version is available, click here to download!"));
        msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/waypointchat-1-12-2-1-18-2.100846/"));
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(format("Visit the Spigot website!")).create()));

        p.spigot().sendMessage(msg);
    }

    private String format(String msg) { return ChatColor.translateAlternateColorCodes('&', msg); }

}
