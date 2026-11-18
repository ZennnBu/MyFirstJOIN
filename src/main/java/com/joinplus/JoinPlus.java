
package com.joinplus;

import org.bukkit.plugin.java.JavaPlugin;

public final class JoinPlus extends JavaPlugin {

    private static JoinPlus instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        getLogger().info("================================");
        getLogger().info("        JoinPlus Enabled!");
        getLogger().info("        Version: " + getPluginMeta().getVersion());
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
