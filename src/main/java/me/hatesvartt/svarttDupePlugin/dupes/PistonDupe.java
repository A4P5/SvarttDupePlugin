// PistonDupe.java

package me.hatesvartt.svarttDupePlugin.dupes;

import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import me.hatesvartt.svarttDupePlugin.SvarttDupePlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Piston;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class PistonDupe implements Listener {

    private final SvarttDupePlugin plugin;
    private final Set<Block> retractingPistons = new HashSet<>();

    public PistonDupe(SvarttDupePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        FileConfiguration config = plugin.getConfig();
        if (!config.getBoolean("piston-dupe.enabled", false)) return;

        Block piston = event.getBlock();
        retractingPistons.add(piston);

        // keeps piston in "retracting" set for a few ticks so player has time to hit the frame - sexy folia compatible
        plugin.getServer().getGlobalRegionScheduler().runDelayed(plugin, task -> {
            retractingPistons.remove(piston);
        }, 5L);
    }

    @EventHandler
    public void handleItemFrameBreak(PlayerItemFrameChangeEvent event) {
        FileConfiguration config = plugin.getConfig();
        if (!config.getBoolean("piston-dupe.enabled", true)) return;

        ItemFrame frame = event.getItemFrame();
        ItemStack item = event.getItemStack();

        if (item == null || item.getType() == Material.AIR) return;

        Block attachedBlock = frame.getLocation().getBlock().getRelative(frame.getAttachedFace());

        if ((attachedBlock.getType() != Material.PISTON && attachedBlock.getType() != Material.STICKY_PISTON)
                || !(attachedBlock.getBlockData() instanceof Piston)) return;

        if (!retractingPistons.contains(attachedBlock)) return;
        int successRate = config.getInt("piston-dupe.success_rate", 50);
        if (Math.random() * 100 >= successRate) return;

        frame.getWorld().dropItemNaturally(frame.getLocation(), item.clone());
    }
}
