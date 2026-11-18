
package com.joinplus.managers;

import com.joinplus.JoinPlus;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public final class UpdateChecker {

    private static final String MODRINTH_PROJECT_ID = "YOUR_PROJECT_ID";

    private final JoinPlus plugin;

    public UpdateChecker(JoinPlus plugin) {
        this.plugin = plugin;
    }

    public void checkForUpdates() {

        if (!plugin.getConfig().getBoolean(
                "update-checker.enabled", true)) {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(
                plugin,
                () -> {
                    try {
                        URI uri = URI.create(
                                "https://api.modrinth.com/v2/project/"
                                        + MODRINTH_PROJECT_ID
                                        + "/version"
                        );

                        HttpURLConnection connection =
                                (HttpURLConnection) uri.toURL()
                                        .openConnection();

                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(5000);
                        connection.setReadTimeout(5000);
                        connection.setRequestProperty(
                                "User-Agent",
                                "JoinPlus/1.0.0"
                        );

                        if (connection.getResponseCode() != 200) {
                            plugin.getLogger().warning(
                                    "Could not check for updates."
                            );
                            connection.disconnect();
                            return;
                        }

                        StringBuilder response = new StringBuilder();

                        try (BufferedReader reader =
                                     new BufferedReader(
                                             new InputStreamReader(
                                                     connection.getInputStream(),
                                                     StandardCharsets.UTF_8
                                             )
                                     )) {

                            String line;

                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                        }

                        connection.disconnect();

                        String json = response.toString();

                        String latestVersion = extractVersion(json);

                        if (latestVersion == null) {
                            plugin.getLogger().warning(
                                    "Could not read latest version."
                            );
                            return;
                        }

                        String currentVersion =
                                plugin.getPluginMeta().getVersion();

                        if (!latestVersion.equalsIgnoreCase(currentVersion)) {
                            plugin.getLogger().info(
                                    "A new JoinPlus version is available: "
                                            + latestVersion
                            );
                        } else {
                            plugin.getLogger().info(
                                    "JoinPlus is up to date!"
                            );
                        }

                    } catch (Exception exception) {
                        plugin.getLogger().warning(
                                "Update check failed: "
                                        + exception.getMessage()
                        );
                    }
                }
        );
    }

    private String extractVersion(String json) {

        java.util.regex.Matcher matcher =
                java.util.regex.Pattern.compile(
                        "\"version_number\"\\s*:\\s*\"([^\"]+)\""
                ).matcher(json);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
}
