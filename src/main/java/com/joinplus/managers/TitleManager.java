
package com.joinplus.managers;

import com.joinplus.JoinPlus;
import com.joinplus.utils.MessageUtil;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

import java.time.Duration;

public final class TitleManager {

    private final JoinPlus plugin;

    public TitleManager(JoinPlus plugin) {
        this.plugin = plugin;
    }

    public void sendFirstJoinTitle(Player player) {

        if (!plugin.getConfig().getBoolean(
                "first-join.title.enabled", true)) {
            return;
        }

        String titleText = plugin.getConfig().getString(
                "first-join.title.title", "");

        String subtitleText = plugin.getConfig().getString(
                "first-join.title.subtitle", "");

        int fadeIn = plugin.getConfig().getInt(
                "first-join.title.fade-in", 10);

        int stay = plugin.getConfig().getInt(
                "first-join.title.stay", 60);

        int fadeOut = plugin.getConfig().getInt(
                "first-join.title.fade-out", 20);

        Title.Times times = Title.Times.times(
                Duration.ofMillis(fadeIn * 50L),
                Duration.ofMillis(stay * 50L),
                Duration.ofMillis(fadeOut * 50L)
        );

        Title title = Title.title(
                MessageUtil.parse(titleText),
                MessageUtil.parse(subtitleText),
                times
        );

        player.showTitle(title);
    }
}
