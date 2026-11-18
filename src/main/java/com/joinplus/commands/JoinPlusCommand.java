
package com.joinplus.commands;

import com.joinplus.JoinPlus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class JoinPlusCommand implements CommandExecutor {

    private final JoinPlus plugin;

    public JoinPlusCommand(JoinPlus plugin) {
        this.plugin = plugin;
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(color("&8&m----------&r &b&lJoinPlus &8&m----------"));
            sender.sendMessage(color("&b/joinplus &7- Main information"));
            sender.sendMessage(color("&b/joinplus help &7- Show commands"));
            sender.sendMessage(color("&b/joinplus reload &7- Reload configuration"));
            sender.sendMessage(color("&b/joinplus info &7- Plugin information"));
            sender.sendMessage(color("&b/joinplus test &7- Test join message"));
            sender.sendMessage(color("&8&m-------------------------------"));
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "reload":

                if (!sender.hasPermission("joinplus.reload")) {
                    sender.sendMessage(color("&cYou don't have permission!"));
                    return true;
                }

                plugin.reloadConfig();
                sender.sendMessage(color("&aJoinPlus configuration reloaded!"));
                break;

            case "info":

                sender.sendMessage(color("&b&lJoinPlus"));
                sender.sendMessage(color("&7Version: &f" +
                        plugin.getPluginMeta().getVersion()));
                sender.sendMessage(color("&7Status: &aEnabled"));
                break;

            case "test":

                if (!sender.hasPermission("joinplus.test")) {
                    sender.sendMessage(color("&cYou don't have permission!"));
                    return true;
                }

                String message = plugin.getConfig().getString(
                        "join.message", "&a+ &f%player% &7joined the server!"
                );

                message = message.replace("%player%", sender.getName());

                sender.sendMessage(color("&7Test message:"));
                sender.sendMessage(color(message));
                break;

            default:
                sender.sendMessage(color("&cUnknown subcommand! Use /joinplus help"));
                break;
        }

        return true;
    }
}
