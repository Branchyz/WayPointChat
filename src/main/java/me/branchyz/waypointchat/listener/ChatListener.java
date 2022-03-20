package me.branchyz.waypointchat.listener;

import me.branchyz.waypointchat.WayPointChat;
import me.branchyz.waypointchat.util.CurseWordsConfig;
import me.branchyz.waypointchat.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;

public class ChatListener implements Listener {
    private final WayPointChat plugin;

    public ChatListener(WayPointChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();

        if(plugin.isChatMuted()) {
            if(!p.hasPermission("waypoint.mutechat.bypass")) {
                e.setCancelled(true);
                p.sendMessage(Messages.PREFIX.toString() + Messages.CHAT_IS_MUTED);
                return;
            }

        }

        if(p.hasPermission("waypoint.cursewords.bypass")) return;

        final ArrayList<String> curseWords = new ArrayList<>(CurseWordsConfig.get().getStringList("curse-words"));
        final String msg = e.getMessage().toLowerCase();

        for (String curseWord : curseWords) {
            if(msg.contains(curseWord.toLowerCase())) {
                e.setCancelled(true);
                p.sendMessage(Messages.PREFIX.toString() + Messages.CURSE_WORD_USED);
                final String alert = Messages.PREFIX + Messages.CURSE_WORD_ALERT.toString().replace("%player%", p.getName()).replace("%curse-word%", curseWord);
                Bukkit.getOnlinePlayers().stream().filter(alertPlayer -> alertPlayer.hasPermission("waypoint.cursewords.alert")).
                        forEach(alertPlayer -> alertPlayer.sendMessage(alert));
                break;
            }
        }
    }
}
