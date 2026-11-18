
package com.joinplus;

import com.joinplus.commands.JoinPlusCommand;
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

        if (getCommand("joinplus") != null) {
            getCommand("joinplus").setExecutor(
                    new JoinPlusCommand(this)
            );
        } else {
            getLogger().severe("Command 'joinplus' is missing from plugin.yml!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("JoinPlus Enabled Successfully!");
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
