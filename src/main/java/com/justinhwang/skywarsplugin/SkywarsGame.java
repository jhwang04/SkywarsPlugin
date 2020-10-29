package com.justinhwang.skywarsplugin;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkywarsGame {
    private int time;
    private List<Player> players;
    private World world;
    private SkywarsPlugin plugin;
    private boolean isGameOver;
    private boolean timerGoing;
    private int periods;
    private boolean killed;
    public Map<Player, Integer> kills;
    private boolean fallDamageValid;
    //private Map<Player, Scoreboard> scoreboards;

    public SkywarsGame(int time, List<Player> players, World world, SkywarsPlugin plugin, boolean timerGoing) {
        this.time = time;
        this.players = new ArrayList<Player>();
        this.world = world;
        this.plugin = plugin;
        this.isGameOver = false;
        this.killed = false;
        this.timerGoing = timerGoing;
        this.kills = new HashMap<Player, Integer>();
        this.fallDamageValid = false;
        Bukkit.getLogger().info("New skywars game instantiated!");
    }

    public void startGame() {
        nextSecond();
    }

    private void nextSecond() {

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

            for(Player p : world.getPlayers()) {
                if(!players.contains(p)) {
                    p.setGameMode(GameMode.SPECTATOR);
                }
            }
        } else if(time == 0) {
            for(int c = 1; c <= plugin.getChestInfo().getInt("numberOfIslands"); c++) {
                int cageX = plugin.getChestInfo().getInt("cage_" + c + ".x");
                int cageY = plugin.getChestInfo().getInt("cage_" + c + ".y");
                int cageZ = plugin.getChestInfo().getInt("cage_" + c + ".z");

                for(int x = -1; x <= 1; x++) {
                    for(int y = -2; y <= 3; y++) {
                        for(int z = -1; z <= 1; z++) {
                            world.getBlockAt(cageX + x, cageY + y, cageZ + z).setType(Material.AIR);
                        }
                    }
                }
            }

            for(Player p : players) {
                plugin.resetPlayerValues(p);
                p.setGameMode(GameMode.SURVIVAL);
            }

        } else if(time == 90 || time == 180) { //first refill
            refill();
        }

        //titles
        if(time <= -25) { //WAITING
            periods++;
            if(periods > 3) {
                periods = 0;
            }
            String dotString = "";
            for(int i = 0; i < periods; i++) {
                dotString += ".";
            }

            //scoreboard
            for(Player p : players) {
                String[] lines = new String[] {
                        ChatColor.RED + "" + ChatColor.BOLD + "Waiting" + dotString,
                        " ",
                        ChatColor.AQUA + "Players: " + ChatColor.WHITE + players.size() + "/" + plugin.getChestInfo().getInt("numberOfIslands"),
                        "  ",
                        ChatColor.GOLD + "Plugin by jhwang04"
                };

                setNewScoreboard(p, ChatColor.GOLD + "" + ChatColor.BOLD + "       SKYWARS       ", lines);
            }
        } else if(time > -25 && time < -10) { //LARGE CAGE COUNTDOWN
            if(players.size() < 2) {
                plugin.broadcastToPlayers(ChatColor.RED + "Start Cancelled!", ChatColor.RED + "Not enough players!", world, 0, 40, 20);
                time = -25;
                timerGoing = false;
            } else {
                plugin.broadcastToPlayers(ChatColor.GOLD + "Game Starting!", ChatColor.BOLD + "" + ChatColor.AQUA + "\u25B6 "+ (-10 - time) + " \u25C0", world, 0, 20, 10);
            }

            //scoreboard
            for(Player p : world.getPlayers()) {
                String[] lines = new String[] {
                        ChatColor.AQUA + "Starting in: " + ChatColor.WHITE + toMinutes(-10 - time),
                        " ",
                        ChatColor.AQUA + "Players: " + ChatColor.WHITE + players.size() + "/" + plugin.getChestInfo().getInt("numberOfIslands"),
                        "  ",
                        ChatColor.GOLD + "Plugin by jhwang04"
                };

                setNewScoreboard(p, ChatColor.GOLD + "" + ChatColor.BOLD + "       SKYWARS       ", lines);
            }
        } else if(time >= -10 && time < 0) { //INDIVIDUAL CAGE COUNTDOWN
            plugin.broadcastToPlayers(ChatColor.AQUA + "\u25B6 " + (0-time) + " \u25C0", "",world, 0, 20, 10);

            //scoreboard
            for(Player p : players) {
                String[] lines = new String[] {
                        ChatColor.AQUA + "Cages open: " + ChatColor.WHITE + toMinutes(0 - time),
                        " ",
                        ChatColor.AQUA + "Players left: " + ChatColor.WHITE + players.size() + "/" + plugin.getChestInfo().getInt("numberOfIslands"),
                        "  ",
                        ChatColor.AQUA + "Kills: " + ChatColor.WHITE + kills.get(p),
                        "   ",
                        ChatColor.GOLD + "Plugin by jhwang04"
                };

                setNewScoreboard(p, ChatColor.GOLD + "" + ChatColor.BOLD + "       SKYWARS       ", lines);
            }
        } else if(time >= 0 && time <= 90) { //BEFORE REFILL

            //scoreboard
            for(Player p : world.getPlayers()) {
                String[] lines = new String[] {
                        ChatColor.AQUA + "Game time: " + ChatColor.WHITE + toMinutes(time),
                        " ",
                        ChatColor.AQUA + "Chest Refill: " + ChatColor.WHITE + toMinutes(90 - time),
                        "  ",
                        ChatColor.AQUA + "Players left: " + ChatColor.WHITE + players.size() + "/" + plugin.getChestInfo().getInt("numberOfIslands"),
                        "   ",
                        ChatColor.AQUA + "Kills: " + ChatColor.WHITE + kills.get(p),
                        "    ",
                        ChatColor.GOLD + "Plugin by jhwang04"
                };

                setNewScoreboard(p, ChatColor.GOLD + "" + ChatColor.BOLD + "       SKYWARS       ", lines);
            }
        } else if(time > 90 && time <= 180) { //BEFORE 2ND REFILL

            //scoreboard
            for(Player p : world.getPlayers()) {
                String[] lines = new String[] {
                        ChatColor.AQUA + "Game time: " + ChatColor.WHITE + toMinutes(time),
                        " ",
                        ChatColor.AQUA + "Chest Refill: " + ChatColor.WHITE + toMinutes(180 - time),
                        "  ",
                        ChatColor.AQUA + "Players left: " + ChatColor.WHITE + players.size() + "/" + plugin.getChestInfo().getInt("numberOfIslands"),
                        "   ",
                        ChatColor.AQUA + "Kills: " + ChatColor.WHITE + kills.get(p),
                        "    ",
                        ChatColor.GOLD + "Plugin by jhwang04"
                };

                setNewScoreboard(p, ChatColor.GOLD + "" + ChatColor.BOLD + "       SKYWARS       ", lines);
            }
        } else if(time > 180 && time <= 300) { //BEFORE GAME END

            //scoreboard
            for(Player p : world.getPlayers()) {
                String[] lines = new String[] {
                        ChatColor.AQUA + "Game time: " + ChatColor.WHITE + toMinutes(time),
                        " ",
                        ChatColor.AQUA + "Chest Refill: " + ChatColor.WHITE + toMinutes(300 - time),
                        "  ",
                        ChatColor.AQUA + "Players left: " + ChatColor.WHITE + players.size() + "/" + plugin.getChestInfo().getInt("numberOfIslands"),
                        "   ",
                        ChatColor.AQUA + "Kills: " + ChatColor.WHITE + kills.get(p),
                        "    ",
                        ChatColor.GOLD + "Plugin by jhwang04"
                };

                setNewScoreboard(p, ChatColor.GOLD + "" + ChatColor.BOLD + "       SKYWARS       ", lines);
            }
        } else if (time > 300) {
            endGame();
        }

        if(players.size() == 1 && time > -10) {
            endGame();
        }


        //running each second recursively, every 20 ticks
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                //incrementing for the next time
                if(timerGoing == true) {
                    time++;
                }

                if(isGameOver == true) {
                    timerGoing = false;
                    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skywars resetmap");
                        }
                    }, 200);
                } else if(killed == true) {
                    Bukkit.getLogger().info("Skywars game was killed");
                } else {
                    nextSecond();
                }

            }
        }, 20);
    }

    public void endGame() {
        this.isGameOver = true;

        for(Player p : world.getPlayers()) {

            if (players.contains(p) && players.size() == 1) {
                p.sendTitle(ChatColor.BOLD + "" + ChatColor.GOLD + "YOU WIN", ChatColor.AQUA + "You won with " + kills.get(p) + " kills!", 0, 100, 20);
            } else {
                p.sendTitle(ChatColor.BOLD + "" + ChatColor.RED + "GAME OVER", ChatColor.AQUA + "Try not to jump into the void next time!", 0, 100, 20);
            }
        }
    }

    public boolean getTimerGoing() {
        return timerGoing;
    }

    public void setTimerGoing(boolean b) {
        this.timerGoing = b;
    }

    public List<Player> getPlayers() {
        return players;
    }

    private void setNewScoreboard(Player p, String title, String[] lines) {
        Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = s.registerNewObjective("SkywarsGame", "dummy", "SkywarsGame");
        obj.setDisplayName(title);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        for(int i = 0; i < lines.length; i++) {
            Score score = obj.getScore(lines[i]);
            score.setScore(lines.length - i);
        }
        p.setScoreboard(s);
    }

    private String toMinutes(int time) {
        String minutes = "" + time/60;
        String seconds = "" + time%60;
        if(seconds.length() == 1) {
            seconds = "0" + seconds;
        }
        String formattedTime = minutes + ":" + seconds;
        return formattedTime;
    }

    public void kill() {
        this.killed = true;
    }

    public void refill() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skywars fillchests");
        for(Player p : world.getPlayers()) {
            p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 3.0f, 1.0f);
            plugin.broadcastToPlayers(ChatColor.AQUA + "Chests have been refilled!", "", world, 10, 40, 10);
        }
    }

    public World getWorld() {
        return world;
    }

    public Map<Player, Integer> getKills() {
        return kills;
    }

    public boolean getFallDamageValid() {
        return fallDamageValid;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

}
