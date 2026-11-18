
package com.joinplus.managers;

import com.joinplus.JoinPlus;
import com.joinplus.utils.MessageUtil;
import org.bukkit.entity.Player;

public final class ActionBarManager {

    private final JoinPlus plugin;

    public ActionBarManager(JoinPlus plugin) {
        this.plugin = plugin;
    }

    public void sendJoinActionBar(Player player, boolean firstJoin) {

        String path = firstJoin
                ? "first-join.actionbar"
                : "join.actionbar";

        if (!plugin.getConfig().getBoolean(path + ".enabled", false)) {
            return;
        }

        String message = plugin.getConfig().getString(
                path + ".message", ""
        );

        if (message == null || message.isEmpty()) {
            return;
        }

        message = message.replace("%player%", player.getName());

        player.sendActionBar(MessageUtil.parse(message));
    }
}
