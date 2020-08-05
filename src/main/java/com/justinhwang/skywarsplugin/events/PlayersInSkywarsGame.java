package com.justinhwang.skywarsplugin.events;

import com.justinhwang.skywarsplugin.SkywarsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayersInSkywarsGame implements Listener {
    private SkywarsPlugin plugin;

    public PlayersInSkywarsGame(SkywarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        World gameWorld = Bukkit.getWorld(plugin.getConfig().getString("template_world") + "_game");

        String worldFrom = e.getFrom().getWorld().getName();
        String worldTo = e.getTo().getWorld().getName();

        if(plugin.skywarsGame != null) {
            if (!worldFrom.equals(gameWorld.getName()) && worldTo.equals(gameWorld.getName())) {
                //if player teleports TO world
                if(plugin.skywarsGame.getPlayers().size() == plugin.getChestInfo().getInt("numberOfIslands")) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "This game is full!");
                } else {
                    plugin.skywarsGame.getPlayers().add(e.getPlayer());
                    if(plugin.skywarsGame.kills.get(e.getPlayer()) == null) {
                        plugin.skywarsGame.kills.put(e.getPlayer(), 0);
                    }

                    if(plugin.skywarsGame.getPlayers().size() == plugin.getChestInfo().getInt("numberOfIslands")) {
                        plugin.broadcastToPlayers(ChatColor.GOLD + "Game full!", "", Bukkit.getWorld(plugin.getConfig().getString("template_world") + "_game"), 0, 20, 10);
                        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                            @Override
                            public void run() {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skywars startgame");
                            }
                        }, 20);
                    }
                }
            } else if (worldFrom.equals(gameWorld.getName()) && !worldTo.equals(gameWorld.getName())) {
                //if player teleports FROM world
                plugin.skywarsGame.getPlayers().remove(e.getPlayer());
            }
        }
    }
}
