
package com.joinplus.managers;

import com.joinplus.JoinPlus;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public final class ParticleManager {

    private final JoinPlus plugin;

    public ParticleManager(JoinPlus plugin) {
        this.plugin = plugin;
    }

    public void playJoinParticles(Player player, boolean firstJoin) {

        String path = firstJoin
                ? "first-join.particles"
                : "join.particles";

        if (!plugin.getConfig().getBoolean(path + ".enabled", false)) {
            return;
        }

        String particleName = plugin.getConfig().getString(
                path + ".type", "HAPPY_VILLAGER"
        );

        int count = Math.max(0, Math.min(100,
                plugin.getConfig().getInt(path + ".count", 20)));

        double offsetX = plugin.getConfig().getDouble(
                path + ".offset-x", 0.5
        );

        double offsetY = plugin.getConfig().getDouble(
                path + ".offset-y", 1.0
        );

        double offsetZ = plugin.getConfig().getDouble(
                path + ".offset-z", 0.5
        );

        try {
            Particle particle = Particle.valueOf(
                    particleName.toUpperCase()
            );

            Location location = player.getLocation().add(
                    0, 1, 0
            );

            player.getWorld().spawnParticle(
                    particle,
                    location,
                    count,
                    offsetX,
                    offsetY,
                    offsetZ,
                    0.0
            );

        } catch (IllegalArgumentException exception) {
            plugin.getLogger().warning(
                    "Invalid particle configured: " + particleName
            );
        }
    }
}
