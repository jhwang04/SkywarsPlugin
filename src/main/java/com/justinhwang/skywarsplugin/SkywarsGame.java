package com.justinhwang.skywarsplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SkywarsGame {
    private int time;
    private List<Player> players;
    private World world;
    private SkywarsPlugin plugin;
    private boolean isGameOver;

    public SkywarsGame(int time, List<Player> players, World world, SkywarsPlugin plugin) {
        this.time = time;
        this.players = players;
        this.world = world;
        this.plugin = plugin;
        isGameOver = false;
    }

    public void startGame() {
        nextSecond();
    }

    private void nextSecond() {
        /*Bukkit.getScheduler().runTaskLater((Plugin) plugin, new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getLogger().info("time = " + time);

                time++;
                nextSecond();
            }
        }, 20);*/


        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                Bukkit.getLogger().info("time = " + time);



                //titles
                if(time < -10) {
                    plugin.broadcastToPlayers(ChatColor.GOLD + "Game Starting!", ChatColor.BOLD + "" + ChatColor.AQUA + "\u25B6 "+ (-10 - time) + " \u25C0", world, 0, 20, 10);
                } else if(time >= -10 && time < 0) {
                    plugin.broadcastToPlayers(ChatColor.AQUA + "\u25B6 " + (0-time) + " \u25C0", "",world, 0, 20, 10);
                } else if(time > 300) {
                    //this is just for testing purposes, to make sure that no infinite loops are created
                    endGame();
                }




                //actions
                if(time == -10) {
                    int c = 1;
                    for(Player p : players) {
                        Location l = p.getLocation();
                        int cageX = plugin.getChestInfo().getInt("cage_" + c + ".x");
                        int cageY = plugin.getChestInfo().getInt("cage_" + c + ".y");
                        int cageZ = plugin.getChestInfo().getInt("cage_" + c + ".z");
                        l.setX(cageX + 0.5);
                        l.setY(cageY);
                        l.setZ(cageZ + 0.5);
                        plugin.resetPlayerValues(p);
                        p.teleport(l);
                        c++;
                    }
                }



                //incrementing for the next time
                time++;

                if(isGameOver == true) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skywars resetmap");
                } else {
                    nextSecond();
                }

            }
        }, 20);
    }

    public void endGame() {
        this.isGameOver = true;
    }
}
