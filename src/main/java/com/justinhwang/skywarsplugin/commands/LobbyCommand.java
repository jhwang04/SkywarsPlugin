package com.justinhwang.skywarsplugin.commands;

import com.justinhwang.skywarsplugin.SkywarsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
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
        String returnMessage = "";

        if(sender instanceof Player) {
            Player p = (Player) sender;

            World lobbyWorld = Bukkit.getWorld(plugin.getConfig().getString("lobby_world"));

            if(lobbyWorld != null) {
                p.getInventory().clear();
                p.setGameMode(GameMode.ADVENTURE);
                p.setExp(0);
                p.setRotation(180.0f, 0.0f);
                p.teleport(lobbyWorld.getSpawnLocation());
            } else {
                Bukkit.getLogger().info(ChatColor.RED + "Lobby world could not be found!");
                returnMessage = ChatColor.RED + "The lobby world could not be found.";
            }
        } else {
            returnMessage = ChatColor.RED + "You must be a player to use this command!";
        }

        sender.sendMessage(returnMessage);
        return true;
    }
}
