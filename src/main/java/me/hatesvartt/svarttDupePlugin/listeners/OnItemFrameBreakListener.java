// OnItemFrameBreakListener.java

package me.hatesvartt.svarttDupePlugin.listeners;

import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent.ItemFrameChangeAction;
import me.hatesvartt.svarttDupePlugin.dupes.ItemFrameDupe;
import me.hatesvartt.svarttDupePlugin.dupes.PistonDupe;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnItemFrameBreakListener implements Listener {

    private final PistonDupe pistonDupe;
    private final ItemFrameDupe itemFrameDupe;

    public OnItemFrameBreakListener(ItemFrameDupe itemFrameDupe, PistonDupe pistonDupe) {
        this.pistonDupe = pistonDupe; // use the same instance
        this.itemFrameDupe = itemFrameDupe;
    }

    @EventHandler
    public void onItemFrameBreak(PlayerItemFrameChangeEvent event) {
        if (event.getAction() != PlayerItemFrameChangeEvent.ItemFrameChangeAction.REMOVE) return;
        pistonDupe.handleItemFrameBreak(event);
        itemFrameDupe.handleItemFrameBreak(event);
    }
}
