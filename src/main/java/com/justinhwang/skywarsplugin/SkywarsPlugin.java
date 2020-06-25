package com.justinhwang.skywarsplugin;

import com.justinhwang.skywarsplugin.commands.SkywarsCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class SkywarsPlugin extends JavaPlugin {

    public FileConfiguration config;

    @Override
    public void onEnable() {

        //ensures that a config.yml file exists somewhere, to be accessed. Does nothing if file already exists.
        this.saveDefaultConfig();

        config = this.getConfig();

        getCommand("skywars").setExecutor(new SkywarsCommand(this));

        getLogger().info("Skywars plugin has been enabled");
    }
}
