package com.justinhwang.skywarsplugin.events;

import com.justinhwang.skywarsplugin.SkywarsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class BoundaryLock implements Listener {
    private SkywarsPlugin plugin;

    public BoundaryLock(SkywarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if(p.getLocation().getWorld().equals(plugin.skywarsGame.getWorld())) {
            if(absVal(p.getLocation().getX()) > 80 || absVal(p.getLocation().getZ()) > 80) {
                e.setCancelled(true);
            } else if(p.getLocation().getY() < 0) {
                Location l;

                if(p.getKiller() != null) {
                    l = p.getKiller().getLocation();
                } else {
                    l = p.getLocation();
                    l.setY(70);
                }

                p.teleport(l);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill " + p.getName());
            }
        } else if(p.getLocation().getWorld().equals(Bukkit.getWorld(plugin.getConfig().getString("lobby_world")))) {
            if(p.getLocation().getY() < 0) {
                Bukkit.dispatchCommand(p, "lobby");
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Location loc = e.getBlock().getLocation();
        if(loc.getBlockY() > 80 && loc.getWorld().equals(plugin.skywarsGame.getWorld())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Location loc = e.getBlock().getLocation();
        if(loc.getBlockY() > 80 && loc.getWorld().equals(plugin.skywarsGame.getWorld())) {
            e.setCancelled(true);
        }
    }

    private double absVal(double x) {
        if(x < 0) {
            x = 0-x;
        }
        return x;
    }
}
