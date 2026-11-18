
package com.joinplus.listeners;

import com.joinplus.JoinPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {

    private final JoinPlus plugin;

    public JoinListener(JoinPlus plugin) {
        this.plugin = plugin;
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.setJoinMessage(null);

        boolean firstJoin = !player.hasPlayedBefore();

        String path = firstJoin ? "first-join" : "join";

        if (!plugin.getConfig().getBoolean(path + ".enabled", true)) {
            return;
        }

        String message = plugin.getConfig().getString(
                path + ".message", ""
        );

        message = message.replace("%player%", player.getName());

        if (plugin.getConfig().getBoolean(path + ".broadcast", true)) {
            Bukkit.broadcastMessage(color(message));
        }

        if (firstJoin) {
            sendFirstJoinEffects(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.setQuitMessage(null);

        if (!plugin.getConfig().getBoolean("quit.enabled", true)) {
            return;
        }

        String message = plugin.getConfig().getString(
                "quit.message", ""
        );

        message = message.replace("%player%", player.getName());

        if (plugin.getConfig().getBoolean("quit.broadcast", true)) {
            Bukkit.broadcastMessage(color(message));
        }
    }

    private void sendFirstJoinEffects(Player player) {

        if (plugin.getConfig().getBoolean(
                "first-join.title.enabled", true)) {

            String title = plugin.getConfig().getString(
                    "first-join.title.title", ""
            );

            String subtitle = plugin.getConfig().getString(
                    "first-join.title.subtitle", ""
            );

            player.sendTitle(
                    color(title),
                    color(subtitle),
                    10, 60, 20
            );
        }

        if (plugin.getConfig().getBoolean(
                "first-join.sound.enabled", true)) {

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
                        "Invalid sound: " + soundName
                );
            }
        }
    }
}
