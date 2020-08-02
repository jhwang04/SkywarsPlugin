package com.justinhwang.skywarsplugin.events;

import com.justinhwang.skywarsplugin.SkywarsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SendToLobby implements Listener {

    private SkywarsPlugin plugin;

    public SendToLobby(SkywarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        Bukkit.dispatchCommand(p,"lobby");
    }
}
