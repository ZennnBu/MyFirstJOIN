
package com.joinplus.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class JoinPlusTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {

        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {

            String input = args[0].toLowerCase();

            if ("help".startsWith(input)) {
                suggestions.add("help");
            }

            if ("info".startsWith(input)) {
                suggestions.add("info");
            }

            if (sender.hasPermission("joinplus.reload")
                    && "reload".startsWith(input)) {
                suggestions.add("reload");
            }

            if (sender.hasPermission("joinplus.test")
                    && "test".startsWith(input)) {
                suggestions.add("test");
            }
        }

        return suggestions;
    }
}
