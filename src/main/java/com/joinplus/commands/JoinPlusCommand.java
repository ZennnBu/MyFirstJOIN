
package com.joinplus.commands;

import com.joinplus.JoinPlus;
import com.joinplus.managers.PermissionMessageManager;
import com.joinplus.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class JoinPlusCommand implements CommandExecutor {

    private final JoinPlus plugin;
    private final PermissionMessageManager permissionManager;

    public JoinPlusCommand(JoinPlus plugin) {
        this.plugin = plugin;
        this.permissionManager = new PermissionMessageManager(plugin);
    }

    private void send(CommandSender sender, String message) {
        String prefix = plugin.getConfig().getString(
                "messages.prefix", "<aqua>[JoinPlus] "
        );

        sender.sendMessage(
                MessageUtil.parse(prefix + message)
        );
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            send(sender, "<gradient:#00FFFF:#8000FF><bold>JoinPlus Commands</bold></gradient>");
            send(sender, "<aqua>/joinplus help <gray>- Show commands");
            send(sender, "<aqua>/joinplus reload <gray>- Reload configuration");
            send(sender, "<aqua>/joinplus info <gray>- Plugin information");
            send(sender, "<aqua>/joinplus test <gray>- Test join message");
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "reload":

                if (!sender.hasPermission("joinplus.reload")) {
                    send(sender, plugin.getConfig().getString(
                            "messages.no-permission",
                            "<red>You don't have permission!"
                    ));
                    return true;
                }

                plugin.reloadConfig();

                send(sender, plugin.getConfig().getString(
                        "messages.reload",
                        "<green>Configuration reloaded!"
                ));
                break;

            case "info":

                send(sender, "<gradient:#00FFFF:#8000FF><bold>JoinPlus</bold></gradient>");
                send(sender, "<gray>Version: <white>"
                        + plugin.getPluginMeta().getVersion());
                send(sender, "<gray>Author: <white>JoinPlusTeam");
                send(sender, "<gray>Status: <green>Enabled");
                send(sender, "<gray>Platform: <white>Paper");
                break;

            case "test":

                if (!sender.hasPermission("joinplus.test")) {
                    send(sender, "<red>You don't have permission!");
                    return true;
                }

                if (sender instanceof Player player) {
                    var permissionMessage =
                            permissionManager.getJoinMessage(player);

                    if (permissionMessage != null) {
                        send(sender, "<gray>Permission-based join message:");
                        sender.sendMessage(permissionMessage);
                        return true;
                    }
                }

                String message = plugin.getConfig().getString(
                        "join.message",
                        "<green>+ <white>%player% <gray>joined the server!"
                );

                message = message.replace(
                        "%player%", sender.getName()
                );

                send(sender, "<gray>Join message test:");
                sender.sendMessage(MessageUtil.parse(message));
                break;

            default:

                send(sender, "<red>Unknown command! Use /joinplus help");
                break;
        }

        return true;
    }
}
