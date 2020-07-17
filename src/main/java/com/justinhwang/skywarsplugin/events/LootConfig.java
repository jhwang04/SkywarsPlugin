package com.justinhwang.skywarsplugin.events;

import com.justinhwang.skywarsplugin.SkywarsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class LootConfig implements Listener {

    private SkywarsPlugin plugin;

    public LootConfig(SkywarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = p.getOpenInventory().getTopInventory();

        if(p.equals(plugin.getPlayerConfiguringLoot()) && e.getSlot() >= 45) {
            String loot = p.getOpenInventory().getTitle();

            //if they click anything at the bottom
            ItemStack item = e.getCurrentItem();

            //if player clicks a glass pane
            if(item.getType() == Material.LIGHT_GRAY_STAINED_GLASS_PANE) {
                p.sendMessage(ChatColor.RED + "These slots cannot be used!");
            }

            //if they click on the barrier
            if(item.getType() == Material.BARRIER) {
                p.sendMessage(ChatColor.RED + "Cancelled! " + ChatColor.AQUA + loot + " has been reset to its previous save!");
                closePlayerInventory(p);
            }

            //if player clicks on the brick
            if(item.getType() == Material.BRICK) {
                p.sendMessage(ChatColor.AQUA + "All loot has been erased!");

                ItemStack air = new ItemStack(Material.AIR);
                for(int i = 0; i < 45; i++) {
                    inv.setItem(i, air);
                }
            }

            //if player clicks on netherite sword
            if(item.getType() == Material.NETHERITE_SWORD) {
                p.sendMessage(ChatColor.AQUA + "" + loot + " has been saved!");

                String prefix;

                if(loot.equals("Island Loot")) {
                    prefix = "islandloot";
                } else {
                    prefix = "midloot";
                }

                for(int i = 0; i < 45; i++) {
                    plugin.getLoot().set(prefix + "." + i, inv.getItem(i));
                }

                plugin.saveLootInfo();

                closePlayerInventory(p);
            }

            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        Inventory inv = e.getInventory();

        if(p.equals(plugin.getPlayerConfiguringLoot())) {
            p.sendMessage(ChatColor.RED + "Close the inventory by clicking \"Save\" or \"Cancel\"!");
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    p.openInventory(inv);
                }
            }, 1);
        }
    }

    private void closePlayerInventory(Player p) {
        plugin.setPlayerConfiguringLoot(null);
        p.closeInventory();
    }

}
