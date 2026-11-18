
package com.joinplus.managers;

import com.joinplus.JoinPlus;
import com.joinplus.utils.MessageUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class JoinItemsManager {

    private final JoinPlus plugin;

    public JoinItemsManager(JoinPlus plugin) {
        this.plugin = plugin;
    }

    public void giveJoinItems(Player player, boolean firstJoin) {

        String path = firstJoin
                ? "first-join.items"
                : "join.items";

        if (!plugin.getConfig().getBoolean(path + ".enabled", false)) {
            return;
        }

        ConfigurationSection section =
                plugin.getConfig().getConfigurationSection(path + ".list");

        if (section == null) {
            return;
        }

        for (String key : section.getKeys(false)) {

            String itemPath = path + ".list." + key;

            String materialName = plugin.getConfig().getString(
                    itemPath + ".material", "STONE"
            );

            Material material = Material.matchMaterial(materialName);

            if (material == null || !material.isItem()) {
                plugin.getLogger().warning(
                        "Invalid join item material: " + materialName
                );
                continue;
            }

            int amount = Math.max(1, Math.min(64,
                    plugin.getConfig().getInt(itemPath + ".amount", 1)));

            ItemStack item = new ItemStack(material, amount);
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                String displayName = plugin.getConfig().getString(
                        itemPath + ".name", ""
                );

                if (!displayName.isEmpty()) {
                    meta.displayName(MessageUtil.parse(displayName));
                }

                item.setItemMeta(meta);
            }

            player.getInventory().addItem(item).values()
                    .forEach(remaining ->
                            player.getWorld().dropItemNaturally(
                                    player.getLocation(), remaining
                            )
                    );
        }
    }
}
