package com.justinhwang.skywarsplugin;

import com.justinhwang.skywarsplugin.commands.SkywarsCommand;
import com.justinhwang.skywarsplugin.events.LootConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class SkywarsPlugin extends JavaPlugin {

    private File configFile;
    private File chestInfoFile;
    private File lootFile;

    private FileConfiguration config;
    private FileConfiguration chestInfo;
    private FileConfiguration loot;

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

        getServer().getPluginManager().registerEvents(new LootConfig(this), this);

        getLogger().info("Skywars plugin has been enabled");
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
}
