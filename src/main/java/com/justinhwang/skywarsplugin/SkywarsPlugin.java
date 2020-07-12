package com.justinhwang.skywarsplugin;

import com.justinhwang.skywarsplugin.commands.SkywarsCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class SkywarsPlugin extends JavaPlugin {

    public FileConfiguration config;
    public FileConfiguration chestInfo;
    public FileConfiguration loot;

    @Override
    public void onEnable() {
        //ensures that a config.yml file exists somewhere, to be accessed. Does nothing if file already exists.
        this.saveDefaultConfig();
        config = this.getConfig();

        //ensures that a copy of chestInfo.yml and loot.yml are saved
        this.saveResource("chestInfo.yml", true);
        chestInfo = YamlConfiguration.loadConfiguration(new InputStreamReader(getResource("chestInfo.yml")));

        chestInfo.set("testkey", "newvalue");
        try {
            chestInfo.save("chestInfo.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        getCommand("skywars").setExecutor(new SkywarsCommand(this));

        getLogger().info("Skywars plugin has been enabled");
    }
}
