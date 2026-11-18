
package com.joinplus.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public final class JoinPlusTabCompleter implements TabCompleter {

    private static final List<String> COMMANDS = List.of(
            "help",
            "info",
            "reload",
            "test"
    );

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {

        List<String> suggestions = new ArrayList<>();

        if (args.length != 1) {
            return suggestions;
        }

        String input = args[0].toLowerCase();

        for (String subCommand : COMMANDS) {

            if (!subCommand.startsWith(input)) {
                continue;
            }

            if (subCommand.equals("reload")
                    && !sender.hasPermission("joinplus.reload")) {
                continue;
            }

            if (subCommand.equals("test")
                    && !sender.hasPermission("joinplus.test")) {
                continue;
            }

            suggestions.add(subCommand);
        }

        return suggestions;
    }
}
