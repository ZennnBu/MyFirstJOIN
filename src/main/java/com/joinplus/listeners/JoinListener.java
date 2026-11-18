
package com.joinplus.listeners;

import com.joinplus.JoinPlus;
import com.joinplus.utils.MessageUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.joinMessage(null);

        boolean firstJoin = !player.hasPlayedBefore();
        String path = firstJoin ? "first-join" : "join";

        if (!plugin.getConfig().getBoolean(path + ".enabled", true)) {
            return;
        }

        String message = plugin.getConfig().getString(path + ".message", "");

        if (message == null || message.isEmpty()) {
            return;
        }

        message = message.replace("%player%", player.getName());

        if (plugin.getConfig().getBoolean(path + ".broadcast", true)) {
            Component component = MessageUtil.parse(message);
            Bukkit.broadcast(component);
        }

        if (firstJoin) {
            sendFirstJoinEffects(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.quitMessage(null);

        if (!plugin.getConfig().getBoolean("quit.enabled", true)) {
            return;
        }

        String message = plugin.getConfig().getString("quit.message", "");

        if (message == null || message.isEmpty()) {
            return;
        }

        message = message.replace("%player%", player.getName());

        if (plugin.getConfig().getBoolean("quit.broadcast", true)) {
            Bukkit.broadcast(MessageUtil.parse(message));
        }
    }

    private void sendFirstJoinEffects(Player player) {

        if (plugin.getConfig().getBoolean(
                "first-join.title.enabled", true)) {

            String title = plugin.getConfig().getString(
                    "first-join.title.title", "");

            String subtitle = plugin.getConfig().getString(
                    "first-join.title.subtitle", "");

            player.showTitle(
                    net.kyori.adventure.title.Title.title(
                            MessageUtil.parse(title),
                            MessageUtil.parse(subtitle)
                    )
            );
        }

        if (plugin.getConfig().getBoolean(
                "first-join.sound.enabled", true)) {

            String soundName = plugin.getConfig().getString(
                    "first-join.sound.name",
                    "ENTITY_PLAYER_LEVELUP"
            );

            try {
                org.bukkit.Sound sound =
                        org.bukkit.Sound.valueOf(soundName.toUpperCase());

                float volume = (float) plugin.getConfig()
                        .getDouble("first-join.sound.volume", 1.0);

                float pitch = (float) plugin.getConfig()
                        .getDouble("first-join.sound.pitch", 1.0);

                player.playSound(
                        player.getLocation(), sound, volume, pitch
                );

            } catch (IllegalArgumentException exception) {
                plugin.getLogger().warning(
                        "Invalid sound: " + soundName
                );
            }
        }
    }
}
