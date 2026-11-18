
package com.joinplus;

import com.joinplus.commands.JoinPlusCommand;
import com.joinplus.commands.JoinPlusTabCompleter;
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

        if (getCommand("joinplus") == null) {
            getLogger().severe("Command missing from plugin.yml!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("joinplus").setExecutor(
                new JoinPlusCommand(this)
        );

        getCommand("joinplus").setTabCompleter(
                new JoinPlusTabCompleter()
        );

        getLogger().info("JoinPlus Enabled Successfully!");
        getLogger().info("Commands Registered!");
        getLogger().info("Tab Completion Registered!");
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
