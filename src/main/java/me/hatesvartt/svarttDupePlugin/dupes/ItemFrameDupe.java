// ItemFrameDupe.java

package me.hatesvartt.svarttDupePlugin.dupes;

import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import me.hatesvartt.svarttDupePlugin.SvarttDupePlugin;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;

public class ItemFrameDupe {

    private final SvarttDupePlugin plugin;

    public ItemFrameDupe(SvarttDupePlugin plugin) {
        this.plugin = plugin;
    }

    public void handleItemFrameBreak(PlayerItemFrameChangeEvent event) {
        FileConfiguration config = plugin.getConfig();
        boolean enabled = config.getBoolean("itemframe-dupe.enabled", false);
        if (!enabled) return;

        int successRate = config.getInt("itemframe-dupe.success_rate", 50);
        if (Math.random() * 100 >= successRate) return;

        //Player player = event.getPlayer();
        ItemFrame frame = event.getItemFrame();
        ItemStack item = event.getItemStack();
        if (item == null || item.getType() == Material.AIR) return;

        frame.getWorld().dropItemNaturally(frame.getLocation(), item);
    }
}
