package com.justinhwang.skywarsplugin.commands;

import com.justinhwang.skywarsplugin.SkywarsPlugin;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class LobbyCommand implements CommandExecutor {
    private SkywarsPlugin plugin;

    public LobbyCommand(SkywarsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

            World lobbyWorld = Bukkit.getWorld(plugin.getConfig().getString("lobby_world"));

            if(lobbyWorld != null) {
                plugin.resetPlayerValues(p);

                if(plugin.skywarsGame != null) {
                    if(p.getWorld().equals(plugin.skywarsGame.getWorld()) && plugin.skywarsGame.getTime() > -10 && plugin.skywarsGame.getTimerGoing() == true) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill " + p.getName());
                    }
                }

                Location l = lobbyWorld.getSpawnLocation();
                l.setPitch(0.0f);
                l.setYaw(180f);
                p.teleport(lobbyWorld.getSpawnLocation());
            } else {
                Bukkit.getLogger().info(ChatColor.RED + "Lobby world could not be found!");
                sender.sendMessage(ChatColor.RED + "The lobby world could not be found.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
        }
        return true;
    }
}
