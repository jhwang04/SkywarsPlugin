package com.justinhwang.skywarsplugin.events;

import com.justinhwang.skywarsplugin.SkywarsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
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
                if(plugin.skywarsGame.getTime() < -10) {
                    if(plugin.skywarsGame.getPlayers().size() == plugin.getChestInfo().getInt("numberOfIslands")) {
                        e.getPlayer().sendTitle(ChatColor.RED + "Game already full!", ChatColor.RED + "You will spectate this game!", 0, 60, 20);
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
                } else if(plugin.skywarsGame.getTime() >= -10) {
                    e.getPlayer().sendTitle(ChatColor.RED + "Game in progress", ChatColor.RED + "You are a spectator for this game", 0, 60, 20);
                    e.getPlayer().setGameMode(GameMode.SPECTATOR);

                }

            } else if (worldFrom.equals(gameWorld.getName()) && !worldTo.equals(gameWorld.getName()) && !e.getTo().equals(gameWorld.getSpawnLocation())) {
                //if player teleports FROM world
                plugin.skywarsGame.getPlayers().remove(e.getPlayer());
                plugin.resetPlayerValues(e.getPlayer());
            } else if(worldFrom.equals(gameWorld.getName()) && e.getTo().equals(gameWorld.getSpawnLocation())) {
                e.getPlayer().sendMessage(ChatColor.RED + "You are already in the game!");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if(e.getPlayer().getLocation().getWorld().equals(plugin.skywarsGame.getWorld())) {
            if(plugin.skywarsGame.getTime() > -10 && plugin.skywarsGame.getTimerGoing() == true) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill " + e.getPlayer().getName());
            } else {
                Bukkit.dispatchCommand(e.getPlayer(), "lobby");
            }
        }

    }
}
