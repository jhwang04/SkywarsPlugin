package com.justinhwang.skywarsplugin.events;

import com.justinhwang.skywarsplugin.SkywarsGame;
import com.justinhwang.skywarsplugin.SkywarsPlugin;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeaths implements Listener {
    private SkywarsPlugin plugin;

    public PlayerDeaths(SkywarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        SkywarsGame game = plugin.skywarsGame;

        if(p.getWorld().equals(game.getWorld())) {
            game.getPlayers().remove(p);

            Player killer = p.getKiller();

            EntityDamageEvent ede = p.getLastDamageCause();
            EntityDamageEvent.DamageCause dc = ede.getCause();

            if(!(killer == null || dc == EntityDamageEvent.DamageCause.SUICIDE)) {
                if (dc == EntityDamageEvent.DamageCause.VOID) {
                    e.setDeathMessage(ChatColor.AQUA + p.getName() + ChatColor.GOLD + " was tossed into the void by " + ChatColor.AQUA + killer.getName());
                } else if (dc == EntityDamageEvent.DamageCause.FIRE_TICK || dc == EntityDamageEvent.DamageCause.FIRE || dc == EntityDamageEvent.DamageCause.LAVA) {
                    e.setDeathMessage(ChatColor.AQUA + p.getName() + ChatColor.GOLD + " was roasted by " + ChatColor.AQUA + killer.getName());
                } else if (dc == EntityDamageEvent.DamageCause.FALL || dc == EntityDamageEvent.DamageCause.FLY_INTO_WALL) {
                    e.setDeathMessage(ChatColor.AQUA + p.getName() + ChatColor.GOLD + " was smacked into the ground by " + ChatColor.AQUA + killer.getName());
                } else if (dc == EntityDamageEvent.DamageCause.PROJECTILE) {
                    e.setDeathMessage(ChatColor.AQUA + p.getName() + ChatColor.GOLD + " was shot by " + ChatColor.AQUA + killer.getName());
                } else {
                    e.setDeathMessage(ChatColor.AQUA + p.getName() + ChatColor.GOLD + " couldn't beat " + ChatColor.AQUA + killer.getName());
                }


                int oldKills = game.getKills().get(killer);
                game.getKills().replace(killer, oldKills + 1);
            } else {
                e.setDeathMessage(ChatColor.AQUA + p.getName() + ChatColor.GOLD + " killed themselves. How sad.");
            }

            Location l = p.getLocation();
            l.setY(l.getBlockY() + 15);

            p.setBedSpawnLocation(l);

            String killerText;
            if(killer != null) {
                killerText = ChatColor.AQUA + "Killed by " + killer.getName();
            } else {
                killerText = ChatColor.AQUA + "You killed yourself!";
            }
            p.sendTitle(ChatColor.RED + "You have been eliminated!", ChatColor.AQUA + killerText, 0, 60, 20);

            Bukkit.getLogger().info("skywarsGame.kills = " + game.getKills().toString());

            plugin.resetPlayerValues(p);
            p.setGameMode(GameMode.SPECTATOR);

            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    p.spigot().respawn();
                }
            }, 1);
        } else {
            p.setBedSpawnLocation(Bukkit.getWorld(plugin.getConfig().getString("lobby_world")).getSpawnLocation());
            p.spigot().respawn();
            Bukkit.dispatchCommand(p, "lobby");
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            World lobby = Bukkit.getWorld(plugin.getConfig().getString("lobby_world"));

            if(p.getLocation().getWorld().equals(lobby) || plugin.skywarsGame.getTime() < -10) {
                e.setDamage(0.0);
            } else if(p.getLocation().getWorld().equals(plugin.skywarsGame.getWorld())){
                if(plugin.skywarsGame.getTime() < 5 && e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
