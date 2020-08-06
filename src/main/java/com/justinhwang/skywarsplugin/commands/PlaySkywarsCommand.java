package com.justinhwang.skywarsplugin.commands;

import com.justinhwang.skywarsplugin.SkywarsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlaySkywarsCommand implements CommandExecutor {
    private SkywarsPlugin plugin;

    public PlaySkywarsCommand(SkywarsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String returnMessage = "";

        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(!p.getWorld().equals(plugin.skywarsGame.getWorld())) {
                p.teleport(plugin.skywarsGame.getWorld().getSpawnLocation());
            }
            returnMessage = ChatColor.AQUA + "Teleported to the game world!";
        } else {
            returnMessage = ChatColor.RED + "Only players can run this command!";
        }

        sender.sendMessage(returnMessage);
        return true;
    }
}
