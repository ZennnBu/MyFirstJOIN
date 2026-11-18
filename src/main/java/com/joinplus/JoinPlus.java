
package com.joinplus;

import com.joinplus.listeners.JoinListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class JoinPlus extends JavaPlugin {

    private static JoinPlus instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(
                new JoinListener(this), this
        );

        getLogger().info("================================");
        getLogger().info("        JoinPlus Enabled!");
        getLogger().info("        Version: " + getPluginMeta().getVersion());
        getLogger().info("        JoinListener Registered!");
        getLogger().info("================================");
    }

    @Override
    public void onDisable() {
        getLogger().info("JoinPlus Disabled!");
        instance = null;
    }

    public static JoinPlus getInstance() {
        return instance;
    }
}
