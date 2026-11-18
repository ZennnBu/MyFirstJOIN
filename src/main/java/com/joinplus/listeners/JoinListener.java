
package com.joinplus.listeners;

import com.joinplus.JoinPlus;
import com.joinplus.managers.ActionBarManager;
import com.joinplus.managers.JoinItemsManager;
import com.joinplus.managers.PermissionMessageManager;
import com.joinplus.managers.TitleManager;
import com.joinplus.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {

    private final JoinPlus plugin;
    private final TitleManager titleManager;
    private final ActionBarManager actionBarManager;
    private final JoinItemsManager joinItemsManager;
    private final PermissionMessageManager permissionMessageManager;

    public JoinListener(JoinPlus plugin) {
        this.plugin = plugin;
        this.titleManager = new TitleManager(plugin);
        this.actionBarManager = new ActionBarManager(plugin);
        this.joinItemsManager = new JoinItemsManager(plugin);
        this.permissionMessageManager =
                new PermissionMessageManager(plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.joinMessage(null);

        boolean firstJoin = !player.hasPlayedBefore();
        String path = firstJoin ? "first-join" : "join";

        if (!plugin.getConfig().getBoolean(path + ".enabled", true)) {
            return;
        }

        if (plugin.getConfig().getBoolean(path + ".broadcast", true)) {
            var permissionMessage =
                    permissionMessageManager.getJoinMessage(player);

            if (permissionMessage != null) {
                Bukkit.broadcast(permissionMessage);
            } else {
                String message = plugin.getConfig().getString(
                        path + ".message", ""
                );

                if (message != null && !message.isEmpty()) {
                    message = message.replace("%player%", player.getName());
                    Bukkit.broadcast(MessageUtil.parse(message));
                }
            }
        }

        actionBarManager.sendJoinActionBar(player, firstJoin);
        joinItemsManager.giveJoinItems(player, firstJoin);

        if (firstJoin) {
            titleManager.sendFirstJoinTitle(player);
            playFirstJoinSound(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.quitMessage(null);

        if (!plugin.getConfig().getBoolean("quit.enabled", true)) {
            return;
        }

        if (!plugin.getConfig().getBoolean("quit.broadcast", true)) {
            return;
        }

        var permissionMessage =
                permissionMessageManager.getQuitMessage(player);

        if (permissionMessage != null) {
            Bukkit.broadcast(permissionMessage);
            return;
        }

        String message = plugin.getConfig().getString("quit.message", "");

        if (message == null || message.isEmpty()) {
            return;
        }

        message = message.replace("%player%", player.getName());
        Bukkit.broadcast(MessageUtil.parse(message));
    }

    private void playFirstJoinSound(Player player) {

        if (!plugin.getConfig().getBoolean(
                "first-join.sound.enabled", true)) {
            return;
        }

        String soundName = plugin.getConfig().getString(
                "first-join.sound.name",
                "ENTITY_PLAYER_LEVELUP"
        );

        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase());

            float volume = (float) plugin.getConfig()
                    .getDouble("first-join.sound.volume", 1.0);

            float pitch = (float) plugin.getConfig()
                    .getDouble("first-join.sound.pitch", 1.0);

            player.playSound(
                    player.getLocation(),
                    sound,
                    volume,
                    pitch
            );

        } catch (IllegalArgumentException exception) {
            plugin.getLogger().warning(
                    "Invalid sound configured: " + soundName
            );
        }
    }
}
