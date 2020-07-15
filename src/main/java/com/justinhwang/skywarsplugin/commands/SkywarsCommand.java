package com.justinhwang.skywarsplugin.commands;

import com.justinhwang.skywarsplugin.SkywarsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class SkywarsCommand implements CommandExecutor {

    private SkywarsPlugin plugin;
    private static String headerLine = ChatColor.AQUA + "-----------------------------------------------------\n";
    private static String defaultHelpMessage = "\n\n" +
            ChatColor.AQUA + "-----------------------------------------------------\n" +
            ChatColor.GOLD + "                          Skywars Help\n" +
            ChatColor.GOLD + "   Type \"/skywars help <islands/chests/etc>\" for more info.\n" +
            ChatColor.AQUA + "-----------------------------------------------------\n" +
            ChatColor.GOLD + "- /skywars islands\n" +
            ChatColor.GRAY + "   - Edit the number of player spawn islands on your map.\n" +
            ChatColor.GOLD + "- /skywars chests\n" +
            ChatColor.GRAY + "   - Edit the number and location of each chest on the map.\n" +
            ChatColor.GOLD + "- /skywars loot\n" +
            ChatColor.GRAY + "   - Edit the chest loot tables.\n" +
            ChatColor.GOLD + "- /skywars cages\n" +
            ChatColor.GRAY + "   - Edit the location of each cage on each island.\n" +
            ChatColor.GOLD + "- /skywars resetmap\n" +
            ChatColor.GRAY + "   - Will generate a new skywars map from the template.\n" +
            ChatColor.GOLD + "- /skywars startgame\n" +
            ChatColor.GRAY + "   - Starts a game of skywars with the players in the game world.\n" +
            ChatColor.GOLD + "- /skywars config\n" +
            ChatColor.GRAY + "   - Edit the designated lobby world and template world";


    public SkywarsCommand(SkywarsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length == 0) {
            //if just "/skywars" is written, with no args, it will send this message.
            sender.sendMessage(ChatColor.AQUA + "-----------------------------------------------------\n" +
                               ChatColor.GOLD + "                    Skywars Plugin - By jhwang04\n" +
                               ChatColor.GOLD + "         Type \"/skywars help\" for a list of commands!\n" +
                               ChatColor.AQUA + "-----------------------------------------------------\n");
        } else if(args.length >= 1) {
            String arg0 = args[0].toLowerCase();
            switch(arg0) {
                case "help":
                    //If "/skywars help" is written, then the "help()" method will determine what to do next
                    help(args, sender);
                    break;
                case "islands":
                    //if "/skywars islands" is written, then the "islands()" method will determine what to do next
                    islands(args, sender);
                    break;
                case "chests":
                    //if "/skywars chests" is written, then the "chests()" method will choose what to do next
                    chests(args, sender);
                    break;
                case "loot":
                    //if "/skywars loot" is written, then the "loot()" method will choose what to do next
                    loot(args, sender);
                    break;
                case "cages":
                    //if "/skywars cages" is written, then the "cages()" method will choose what to do next
                    cages(args, sender);
                    break;
                case "resetmap":
                    //if "/skywars resetmap" is written, then the "resetMap()" method will choose what to do next
                    resetMap(args, sender);
                    break;
                case "startgame":
                    //if "/skywars startgame" is written, then the "startgame()" method will choose what to do next
                    startGame(args, sender);
                    break;
                case "config":
                    //if "/skywars config" is written, the "config()" method will choose what to do next
                    config(args, sender);
                    break;
                default:
                    sender.sendMessage(ChatColor.GOLD + "Type \"/skywars help\" for a list of commands!");
                    break;
            }
        }

        return true;
    }



    // run when "/skywars help" is sent
    private void help(String[] args, CommandSender sender) {
        String message = "";

        if(args.length > 1) {
            String arg1 = args[1].toLowerCase();
            switch(arg1) {
                case "islands":
                    message = "" +
                            headerLine +
                            ChatColor.GOLD + "                Skywars Islands Help\n" +
                            headerLine +
                            ChatColor.GOLD + "- /skywars islands info\n" +
                            ChatColor.GRAY + "   - Lists # of islands and the # of chests on each island.\n" +
                            ChatColor.GOLD + "- /skywars islands setNumber\n" +
                            ChatColor.GRAY + "   - Sets the number of islands the plugin is expecting.";
                    break;
                case "chests":
                    message = "" +
                            headerLine +
                            ChatColor.GOLD + "                Skywars Chests Help\n" +
                            headerLine +
                            ChatColor.GOLD + "- Island #0 is the middle island.\n" +
                            ChatColor.GOLD + "- /skywars chests info <island>\n" +
                            ChatColor.GRAY + "   - Lists number of chests, & coords of each chest on the given island.\n" +
                            ChatColor.GOLD + "- /skywars chests clear <island>\n" +
                            ChatColor.GRAY + "   - Erases locations of all chests on the specified island.\n" +
                            ChatColor.GOLD + "- /skywars chests add <island>\n" +
                            ChatColor.GRAY + "   - Sets the location of the specified island & chest number to the player's current location.";
                    break;
                case "loot":
                    message = "" +
                            headerLine +
                            ChatColor.GOLD + "                 Skywars Loot Help\n" +
                            headerLine +
                            ChatColor.GOLD + "- /skywars loot spawnislands\n" +
                            ChatColor.GRAY + "   - Opens an inventory view to edit the spawn island's loot table.\n" +
                            ChatColor.GOLD + "- /skywars loot mid\n" +
                            ChatColor.GRAY + "   - Opens an inventory view to edit the middle island's loot table.\n";
                    break;
                case "cages":
                    message = "" +
                            headerLine +
                            ChatColor.GOLD + "                  Skywars Cages Help\n" +
                            headerLine +
                            ChatColor.GOLD + "- /skywars cages info\n" +
                            ChatColor.GRAY + "   - Lists coordinates of all the cages\n" +
                            ChatColor.GOLD + "- /skywars cages setlocation <island>\n" +
                            ChatColor.GRAY + "   - Sets the cage location of the given island to the player's current location";
                    break;
                case "resetmap":
                    message = ChatColor.GOLD + "Resets the game world to be the same as the template world";
                    break;
                case "startgame":
                    message = ChatColor.GOLD + "Starts a game of Skywars with all the players in the game world.";
                    break;
                case "config":
                    message =
                            headerLine +
                            ChatColor.GOLD + "                   Skywars Config Help\n" +
                            headerLine +
                            ChatColor.GOLD + "- /skywars config lobbyworld <info/set>\n" +
                            ChatColor.GRAY + "   - Check/set the world that is designated as the \"lobby\" for the Skywars plugin.\n" +
                            ChatColor.GOLD + "- /skywars config templateworld <info/set>\n" +
                            ChatColor.GRAY + "   - Check/set the world that is designated as the template, that is copied for each skywars game.";
                    break;
                default:
                    message = defaultHelpMessage;
                    break;
            }
        } else {
            message = defaultHelpMessage;
        }

        sender.sendMessage(message);
    }

    // run when "/skywars islands" is written
    private void islands(String[] args, CommandSender sender) {
        String returnMessage = "";

        if(args.length > 1) {
            String arg1 = args[1].toLowerCase();
            int oldNumber = plugin.getChestInfo().getInt("numberOfIslands");
            switch(arg1) {
                case "info":
                    returnMessage = ChatColor.GOLD + "SkywarsPlugin is currently set to expect " + oldNumber + " islands.";
                    break;
                case "setnumber":
                    if(args.length > 2) {
                        if(isParsable(args[2])) {
                            int newNumber = Integer.parseInt(args[2]);
                            plugin.getChestInfo().set("numberOfIslands", newNumber);
                            saveChestInfo();
                            returnMessage = ChatColor.GOLD + "Number of islands has been changed from " + oldNumber + " to " + newNumber;
                        } else {
                            returnMessage = ChatColor.RED + "Please enter a valid number!";
                        }
                    } else {
                        returnMessage = ChatColor.RED + "Usage: /skywars islands setnumber <new number of islands>";
                    }
                    break;
                default:
                    returnMessage = ChatColor.RED + "Usage: /skywars islands <info/setnumber>";
                    break;
            }
        } else {
            returnMessage = ChatColor.RED + "Usage: /skywars islands <info/setnumber>";
        }

        sender.sendMessage(returnMessage);
    }

    // run when "/skywars chests" is sent
    private void chests(String[] args, CommandSender sender) {
        String returnMessage = "";
        FileConfiguration chestInfo = plugin.getChestInfo();

        if(args.length > 1) {
            String arg1 = args[1].toLowerCase();

            switch(arg1) {
                case "info":
                    if(args.length > 2) {
                        if(isParsable(args[2])) {
                            int arg2 = Integer.parseInt(args[2]);
                            if(arg2 <= chestInfo.getInt("numberOfIslands")) {

                                int numberOfChests = howManyChests(arg2);

                                returnMessage = ChatColor.GOLD + "Chest info for island " + arg2 + ":\n";
                                if(numberOfChests == 0) {
                                    returnMessage += "N/A, No chests are specified for island " + arg2;
                                }
                                for(int i = 1; i <= numberOfChests; i++) {
                                    int x = chestInfo.getInt("island_" + arg2 + "_" + i + ".x");
                                    int y = chestInfo.getInt("island_" + arg2 + "_" + i + ".y");
                                    int z = chestInfo.getInt("island_" + arg2 + "_" + i + ".z");
                                    returnMessage += "Chest " + i + "'s coordinates are (" + x + ", " + y + ", " + z + ")\n";
                                }

                            } else {
                                returnMessage = ChatColor.RED + "Given number, " + arg2 + ", is out of range.\n" +
                                        "Use \"/skywars islands info\" to check the number of islands expected.";
                            }
                        }
                    } else {
                        returnMessage = headerLine;
                        returnMessage += ChatColor.GOLD + "                   Chest Info\n";
                        returnMessage += "Use \"/skywars chests info <island #>\" for specific details.\n";
                        returnMessage += headerLine;

                        int numberOfIslands = chestInfo.getInt("numberOfIslands");
                        int chestsPerIsland = chestInfo.getInt("chestsOnSpawnIslands");

                        for(int i = 0; i <= numberOfIslands; i++) {
                            int numberOfChests = howManyChests(i);
                            if(i == 0) {
                                returnMessage += ChatColor.GOLD + "The middle island (Island #0) has " + numberOfChests + " chest locations set.\n";
                            } else {
                                returnMessage += ChatColor.GOLD + "Island #" + i + " has " + numberOfChests + "/" + chestsPerIsland + " chest locations set.\n";
                            }
                        }

                    }
                    break;
                case "clear":
                    if(args.length > 2) {
                        if(isParsable(args[2])) {
                            int arg2 = Integer.parseInt(args[2]);
                            int chestsOnIsland = howManyChests(arg2);

                            while(chestsOnIsland > 0) {
                                chestInfo.set("island_" + arg2 + "_" + chestsOnIsland, null);
                                saveChestInfo();
                                chestsOnIsland--;
                            }
                            returnMessage = ChatColor.GOLD + "Chest locations cleared for island #" + arg2 + "!";
                        } else {
                            returnMessage = ChatColor.RED + "Please enter a valid number!";
                        }
                    } else {
                        returnMessage = ChatColor.RED + "Usage: /skywars chests clear <island #>";
                    }
                    break;
                case "add":
                    if(sender instanceof Player) {
                        if(args.length > 2) {
                            if(isParsable(args[2])) {
                                int arg2 = Integer.parseInt(args[2]);
                                if(arg2 <= chestInfo.getInt("numberOfIslands")) {

                                    int chestNumber = howManyChests(arg2) + 1;

                                    if(chestNumber <= chestInfo.getInt("chestsOnSpawnIslands") || arg2 == 0) {

                                        Location loc = ((Player) sender).getLocation();

                                        chestInfo.set("island_" + arg2 + "_" + chestNumber + ".x", loc.getBlockX());
                                        chestInfo.set("island_" + arg2 + "_" + chestNumber + ".y", loc.getBlockY());
                                        chestInfo.set("island_" + arg2 + "_" + chestNumber + ".z", loc.getBlockZ());

                                        saveChestInfo();

                                        returnMessage = ChatColor.GOLD + "Island " + arg2 + ", Chest " + chestNumber + ", has been set to " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
                                    } else {
                                        returnMessage = ChatColor.RED + "There are already enough chests! Use \"/skywars chests clear <island #>\" to clear chest locations for the given island.";
                                    }
                                } else {
                                    returnMessage = ChatColor.RED + "Given number, " + arg2 + ", is out of range.\n" +
                                            "Use \"/skywars islands info\" to check the number of islands expected.";
                                }
                            } else {
                                returnMessage = ChatColor.RED + "Please enter a valid number!";
                            }
                        } else {
                            returnMessage = ChatColor.RED + "Usage: /skywars chests add <island #>";
                        }
                    } else {
                        returnMessage = ChatColor.RED + "You must be a player to send this command!";
                    }
                    break;
                case "number":
                    if(args.length > 2) {
                        int oldNumber = chestInfo.getInt("chestsOnSpawnIslands");

                        if(args[2].equals("info")) {
                            returnMessage = ChatColor.GOLD + "SkywarsPlugin is expecting " + oldNumber + " chests on each spawn island.";
                        } else if(args[2].equals("set")) {
                            if(isParsable(args[3])) {
                                int arg3 = Integer.parseInt(args[3]);
                                chestInfo.set("chestsOnSpawnIslands", arg3);
                                saveChestInfo();
                                returnMessage = ChatColor.GOLD + "Chests on each spawn island has been changed from " + oldNumber + " to " + arg3;
                            } else {
                                returnMessage = ChatColor.RED + "Usage: /skywars chests number set <# of chests on each spawn island>";
                            }
                        } else {
                            returnMessage = ChatColor.RED + "Usage: /skywars chests number <info/set>";
                        }
                    } else {
                        returnMessage = ChatColor.RED + "Usage: /skywars chests number <info/set>";
                    }
                    break;
                default:
                    returnMessage = ChatColor.RED + "Usage: /skywars chests <info/clear/add/number>";
                    break;
            }
        } else {
            returnMessage = ChatColor.RED + "Usage: /skywars chests <info/clear/add/number>";
        }

        sender.sendMessage(returnMessage);
    }

    //run when "/skywars loot" is sent
    private void loot(String[] args, CommandSender sender) {
        String returnMessage = "";

        if(args.length > 1) {
            String arg1 = args[1].toLowerCase();

            switch(arg1) {
                case "island":
                    returnMessage = "Placeholder for # of islands, and # of set chests on each island";
                    break;
                case "mid":
                    returnMessage = "Placeholder for setting # of islands";
                    break;
                default:
                    returnMessage = ChatColor.RED + "Usage: /skywars loot <island/mid>";
                    break;
            }
        } else {
            returnMessage = ChatColor.RED + "Usage: /skywars loot <island/mid>";
        }

        sender.sendMessage(returnMessage);
    }

    //run when "/skywars cages" is sent
    private void cages(String[] args, CommandSender sender) {
        String returnMessage = "";

        if(args.length > 1) {
            String arg1 = args[1].toLowerCase();

            switch(arg1) {
                case "info":
                    returnMessage = "Placeholder for displaying coords of each cage on each island";
                    break;
                case "set":
                    if(args.length > 2) {
                        returnMessage = "Placeholder for setting coords of a given island's cage";
                    } else {
                        returnMessage = ChatColor.RED + "Usage: /skywars cages set <island #>";
                    }
                    break;
                default:
                    returnMessage = ChatColor.RED + "Usage: /skywars cages <info/set>";
                    break;
            }
        } else {
            returnMessage = ChatColor.RED + "Usage: /skywars cages <info/set>";
        }

        sender.sendMessage(returnMessage);
    }

    //run when "/skywars resetmap" is sent
    private void resetMap(String[] args, CommandSender sender) {
        String returnMessage = "Placeholder for kicking players from world, and resetting the map";

        sender.sendMessage(returnMessage);
    }

    //run when "/skywars startgame" is sent
    private void startGame(String[] args, CommandSender sender) {
        String returnMessage = "Placeholder for starting up a game of skywars";

        sender.sendMessage(returnMessage);
    }

    //run when "/skywars config" is sent
    private void config(String[] args, CommandSender sender) {
        String returnMessage = "";

        if(args.length > 1) {
            String arg1 = args[1].toLowerCase();

            switch(arg1) {
                //skywars lobby
                case "lobbyworld":
                    if(args.length > 2) {
                        String arg2 = args[2].toLowerCase();
                        String currentLobby = plugin.getConfig().getString("lobby_world");
                        if(arg2.equals("info")) {
                            returnMessage = ChatColor.GOLD + "Lobby world is currently set to \"" + currentLobby + "\"";
                        } else if(arg2.equals("set")) {
                            if(args.length > 3) {
                                plugin.getConfig().set("lobby_world", args[3]);
                                plugin.saveConfig();
                                returnMessage = ChatColor.GOLD + "Lobby world has been changed from \"" + currentLobby + "\" to \"" + args[3] + "\"";
                            } else {
                                returnMessage = ChatColor.RED + "Usage: /skywars config lobbyworld set <worldname>";
                            }
                        } else {
                            returnMessage = ChatColor.RED + "Usage: /skywars config lobbyworld <info/set>";
                        }
                    } else {
                        returnMessage = ChatColor.RED + "Usage: /skywars config lobbyworld <info/set>";
                    }
                    break;
                case "templateworld":
                    if(args.length > 2) {
                        String arg2 = args[2].toLowerCase();
                        String currentLobby = plugin.getConfig().getString("template_world");
                        if(arg2.equals("info")) {
                            returnMessage = ChatColor.GOLD + "Template world is currently set to \"" + currentLobby + "\"";
                        } else if(arg2.equals("set")) {
                            if(args.length > 3) {
                                plugin.getConfig().set("template_world", args[3]);
                                plugin.saveConfig();
                                returnMessage = ChatColor.GOLD + "Template world has been changed from \"" + currentLobby + "\" to \"" + args[3] + "\"";
                            } else {
                                returnMessage = ChatColor.RED + "Usage: /skywars config templateworld set <worldname>";
                            }
                        } else {
                            returnMessage = ChatColor.RED + "Usage: /skywars config templateworld <info/set>";
                        }
                    } else {
                        returnMessage = ChatColor.RED + "Usage: /skywars config templateworld <info/set>";
                    }
                    break;
                default:
                    returnMessage = ChatColor.RED + "Usage: /skywars config <lobbyworld/templateworld>";
                    break;
            }
        } else {
            returnMessage = ChatColor.RED + "Usage: /skywars config <lobbyworld/templateworld>";
        }

        sender.sendMessage(returnMessage);
    }


    // method to check if a text string is an integer
    public static boolean isParsable(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    //finds out how many chests are set for the given island
    public int howManyChests(int island) {
        int counter = 0;

        while(plugin.getChestInfo().contains("island_" + island + "_" + (counter + 1))) {
            counter++;
        }

        return counter;
    }

    //save the chestInfo file
    public void saveChestInfo() {
        try {
            plugin.getChestInfo().save(plugin.getChestInfoFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
