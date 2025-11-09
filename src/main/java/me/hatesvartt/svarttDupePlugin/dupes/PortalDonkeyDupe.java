// PortalDonkeyDupe.java

package me.hatesvartt.svarttDupePlugin.dupes;

import me.hatesvartt.svarttDupePlugin.SvarttDupePlugin;
import org.bukkit.PortalType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PortalDonkeyDupe implements Listener {

    private final SvarttDupePlugin plugin;
    private final Set<UUID> recentlyInPortal = new HashSet<>();

    public PortalDonkeyDupe(SvarttDupePlugin plugin) {
        this.plugin = plugin;
    }

    // track donkeys entering end portals
    @EventHandler
    public void onEntityPortalEnter(EntityPortalEnterEvent event) {
        FileConfiguration config = plugin.getConfig();
        boolean enabled = config.getBoolean("portal-donkey-dupe.enabled", false);
        boolean nether_allowed = config.getBoolean("portal-donkey-dupe.allowed-portals.nether_portal", false);
        boolean end_allowed = config.getBoolean("portal-donkey-dupe.allowed-portals.end_portal", false);

        if (event.getPortalType() == PortalType.NETHER) {
            if (!nether_allowed) return;
        }

        if (!(event.getPortalType() == PortalType.ENDER)) {
            if (!end_allowed) return;
    }

        if (event.getEntity() instanceof Donkey donkey && enabled) {
            recentlyInPortal.add(donkey.getUniqueId());
            donkey.getScheduler().runDelayed(plugin, task -> recentlyInPortal.remove(donkey.getUniqueId()), null, 100L);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        FileConfiguration config = plugin.getConfig();
        boolean enabled = config.getBoolean("portal-donkey-dupe.enabled", false);
        if (!enabled) return;

        Entity entity = event.getEntity();
        if (!(entity instanceof Donkey donkey)) return;

        boolean hadChest = donkey.isCarryingChest();

        if (recentlyInPortal.contains(donkey.getUniqueId())) {
            if (hadChest) {
                int successRate = config.getInt("portal-donkey-dupe.success_rate", 50);
                if (Math.random() * 100 >= successRate) return;

                // summons a fake clone of the donkey
                Donkey fake = donkey.getWorld().spawn(donkey.getLocation(), Donkey.class);
                fake.setCarryingChest(donkey.isCarryingChest());
                fake.setCustomName(donkey.getCustomName());
                fake.setAdult();
                
                // Copy full inventory
                if (donkey.isCarryingChest()) {
                    fake.getInventory().setContents(donkey.getInventory().getContents().clone());
                } else {
                    ItemStack saddle = donkey.getInventory().getSaddle();
                    if (saddle != null) fake.getInventory().setSaddle(saddle.clone());
                }
                
                // Kill instantly
                fake.getScheduler().run(plugin, task -> fake.setHealth(0), null);
            }
        }
    }

}

