package com.justinhwang.skywarsplugin;

import com.justinhwang.skywarsplugin.commands.LobbyCommand;
import com.justinhwang.skywarsplugin.commands.SkywarsCommand;
import com.justinhwang.skywarsplugin.events.LootConfig;
import com.justinhwang.skywarsplugin.events.PlayersInSkywarsGame;
import com.justinhwang.skywarsplugin.events.SendToLobby;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class SkywarsPlugin extends JavaPlugin {

    private File configFile;
    private File chestInfoFile;
    private File lootFile;

    private FileConfiguration config;
    private FileConfiguration chestInfo;
    private FileConfiguration loot;

    public SkywarsGame skywarsGame;

    private Player playerConfiguringLoot;

    @Override
    public void onEnable() {
        //ensures that a config.yml file exists somewhere, to be accessed. Does nothing if file already exists.
        this.saveDefaultConfig();
        config = this.getConfig();

        //saves the default version of chestInfo.yml if it does not exist
        chestInfoFile = new File(getDataFolder(), "chestInfo.yml");
        chestInfo = YamlConfiguration.loadConfiguration(chestInfoFile);
        if(chestInfoFile.exists() == false) {
            saveResource("chestInfo.yml", false);
        }

        //saves the default version of loot.yml if it does not exist
        lootFile = new File(getDataFolder(), "loot.yml");
        loot = YamlConfiguration.loadConfiguration(lootFile);
        if(lootFile.exists() == false) {
            saveResource("loot.yml", false);
        }


        //registering the command
        getCommand("skywars").setExecutor(new SkywarsCommand(this));

        getCommand("lobby").setExecutor(new LobbyCommand(this));

        getServer().getPluginManager().registerEvents(new LootConfig(this), this);

        getServer().getPluginManager().registerEvents(new SendToLobby(this), this);

        getServer().getPluginManager().registerEvents(new PlayersInSkywarsGame(this), this);


        String lobbyWorldName = getConfig().getString("lobby_world");
        World lobbyWorld = Bukkit.getWorld(lobbyWorldName);
        if(lobbyWorld == null) {
            Bukkit.createWorld(new WorldCreator(lobbyWorldName));
            Bukkit.getLogger().info(ChatColor.GOLD + "Lobby world loaded");
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skywars resetmap");


        getLogger().info("Skywars plugin has been enabled");
    }

    @Override
    public void onDisable() {

    }

    public FileConfiguration getChestInfo() {
        return chestInfo;
    }

    public File getChestInfoFile() {
        return chestInfoFile;
    }

    public void saveChestInfo() {
        try {
            getChestInfo().save(getChestInfoFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public FileConfiguration getLoot() {
        return loot;
    }

    public File getLootFile() {
        return lootFile;
    }

    public void saveLootInfo() {
        try {
            getLoot().save(getLootFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Player getPlayerConfiguringLoot() {return playerConfiguringLoot;}

    public void setPlayerConfiguringLoot(Player p) {this.playerConfiguringLoot = p;}

    public void resetPlayerValues(Player p) {
        p.getInventory().clear();
        p.setGameMode(GameMode.ADVENTURE);
        p.getActivePotionEffects().clear();
        p.setExp(0);
        p.setFlying(false);
        p.setSwimming(false);
        p.setItemOnCursor(null);
        p.setFoodLevel(20);
        p.setHealth(20.0);
        p.setSaturation(20f);

        p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
    }


    //For resetting the world

    public boolean unloadWorld(World world) {
        if(!world.equals(null)) {
            Bukkit.getServer().unloadWorld(world, false);
            return true;
        } else {
            return false;
        }
    }

    //COPIED FROM BUKKIT FORUMS, https://bukkit.org/threads/unload-delete-copy-worlds.182814/
    public void copyWorld(File source, File target) {
        List<String> filesToIgnore = Arrays.asList("uid.dat", "session.lock", "playerdata");
        try {
            if(!filesToIgnore.contains(source.getName())) {
                if(source.isDirectory()) {
                    if(!target.exists()) {
                        if(!target.mkdirs()) {
                            throw new IOException("Could not create world directory");
                        }
                    }
                    String[] files = source.list();
                    for(String file : files) {
                        File sourceFile = new File(source, file);
                        File targetFile = new File(target, file);
                        copyWorld(sourceFile, targetFile);
                    }
                } else {
                    FileInputStream in = new FileInputStream(source);
                    FileOutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;

                    while((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }

                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeWorld(File worldFile) {
        /*if(!world.equals(null)) {
            File worldFolder = world.getWorldFolder();
            worldFolder.delete();
            return true;
        } else {
            return false;
        }*/
        if(worldFile.exists()) {
            File files[] = worldFile.listFiles();
            for(int f = 0; f < files.length; f++) {
                if(files[f].isDirectory() == true) {
                    removeWorld(files[f]);
                } else {
                    files[f].delete();
                }
            }
        }
        return worldFile.delete();
    }

    public void broadcastToPlayers(String title, String subtitle, World world) {
        List<Player> players = world.getPlayers();

        for(Player p : players) {
            p.sendTitle(title, subtitle, 10, 70, 20);
        }
    }

    public void broadcastToPlayers(String title, String subtitle, World world, int fadeIn, int stay, int fadeOut) {
        List<Player> players = world.getPlayers();

        for(Player p : players) {
            p.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }


}
