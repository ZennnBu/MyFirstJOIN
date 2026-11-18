
package com.joinplus.managers;

import com.joinplus.JoinPlus;
import com.joinplus.utils.MessageUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class PermissionMessageManager {

    private final JoinPlus plugin;

    public PermissionMessageManager(JoinPlus plugin) {
        this.plugin = plugin;
    }

    public Component getJoinMessage(Player player) {
        return getPermissionMessage(player, "join");
    }

    public Component getQuitMessage(Player player) {
        return getPermissionMessage(player, "quit");
    }

    private Component getPermissionMessage(Player player, String type) {

        if (!plugin.getConfig().getBoolean(
                "permission-messages.enabled", false)) {
            return null;
        }

        ConfigurationSection groups = plugin.getConfig()
                .getConfigurationSection("permission-messages.groups");

        if (groups == null) {
            return null;
        }

        List<String> groupNames = new ArrayList<>(
                groups.getKeys(false)
        );

        groupNames.sort(Comparator.comparingInt(
                (String group) -> groups.getInt(
                        group + ".priority", 0
                )
        ).reversed());

        for (String group : groupNames) {

            String permission = groups.getString(
                    group + ".permission", ""
            );

            if (permission.isEmpty()
                    || !player.hasPermission(permission)) {
                continue;
            }

            String message = groups.getString(
                    group + "." + type, ""
            );

            if (message == null || message.isEmpty()) {
                continue;
            }

            message = message.replace("%player%", player.getName());

            return MessageUtil.parse(message);
        }

        return null;
    }
}
