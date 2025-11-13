package me.hatesvartt.svarttDupePlugin.commands;

import me.hatesvartt.svarttDupePlugin.SvarttDupePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;

public class DupeCommand implements CommandExecutor {
    private final SvarttDupePlugin plugin;

    public DupeCommand(SvarttDupePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.YELLOW + "===" + ChatColor.WHITE + " Active Dupes " + ChatColor.YELLOW + "===");
        sender.sendMessage(ChatColor.WHITE + "Itemframe Dupe: " + ChatColor.YELLOW + plugin.getConfig().get("itemframe-dupe.enabled"));
        sender.sendMessage(ChatColor.WHITE + "PortalDonkey Dupe: " + ChatColor.YELLOW + plugin.getConfig().get("portal-donkey-dupe.enabled"));
        sender.sendMessage(ChatColor.WHITE + "Piston Dupe: " + ChatColor.YELLOW + plugin.getConfig().get("piston-dupe.enabled"));
        return true;
    }
}
