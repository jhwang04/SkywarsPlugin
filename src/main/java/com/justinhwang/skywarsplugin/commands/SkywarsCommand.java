package com.justinhwang.skywarsplugin.commands;

import com.justinhwang.skywarsplugin.SkywarsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SkywarsCommand implements CommandExecutor {

    private SkywarsPlugin plugin;
    private String defaultHelpMessage = "" +
            ChatColor.GOLD + "Skywars Help\n" +
            "------------------------------------\n" +
            ChatColor.GREEN + "Type \"/skywars help <islands/chests/etc>\" for more detailed info.\n" +
            ChatColor.GOLD + "- /skywars islands\n" +
            ChatColor.GRAY + "   - Edit the number of player spawn islands on your map.\n" +
            ChatColor.GOLD + "- /skywars chests\n" +
            ChatColor.GRAY + "   - Edit the number and location of each chest on the map.\n" +
            ChatColor.GOLD + "- /skywars loot\n" +
            ChatColor.GRAY + "   - Edit the loot that can appear on spawn islands and at the middle island.\n" +
            ChatColor.GOLD + "- /skywars cages\n" +
            ChatColor.GRAY + "   - Edit the location of each cage on each island.\n" +
            ChatColor.GOLD + "- /skywars resetmap\n" +
            ChatColor.GRAY + "   - Will reset the specified world in config.yml to be the template world.\n" +
            ChatColor.GOLD + "- /skywars startgame\n" +
            ChatColor.GRAY + "   - Starts a new skywars game with the players on the game map.";


    public SkywarsCommand(SkywarsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length == 0) {
            //if just "/skywars" is written, with no args, it will send this message.
            sender.sendMessage(ChatColor.GOLD + "Type \"/skywars help\" for a list of commands!");
        } else if(args.length == 1) {
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
                            ChatColor.GREEN + "Skywars Islands Help" +
                            ChatColor.GOLD + "- /skywars islands info\n" +
                            ChatColor.GRAY + "   - Lists # of islands and the # of chests on each island.\n" +
                            ChatColor.GOLD + "- /skywars islands setNumber\n" +
                            ChatColor.GRAY + "   - Sets the number of islands the plugin is expecting.";
                    break;
                case "chests":
                    message = "" +
                            ChatColor.GREEN + "Skywars Chests Help" +
                            ChatColor.GOLD + "- Island #0 is the middle island.\n" +
                            ChatColor.GOLD + "- /skywars chests info <island>\n" +
                            ChatColor.GRAY + "   - Lists number of chests, & coords of each chest on the given island.\n" +
                            ChatColor.GOLD + "- /skywars chests clear <island>\n" +
                            ChatColor.GRAY + "   - Erases locations of all chests on the specified island.\n" +
                            ChatColor.GOLD + "- /skywars chests setlocation <island> <chestnumber>\n" +
                            ChatColor.GRAY + "   - Sets the location of the specified island & chest number to the player's current location.";
                    break;
                case "loot":
                    message = "" +
                            ChatColor.GREEN + "Skywars Loot Help" +
                            ChatColor.GOLD + "- /skywars loot spawnislands\n" +
                            ChatColor.GRAY + "   - Opens an inventory view to edit the spawn island's loot table.\n" +
                            ChatColor.GOLD + "- /skywars loot mid\n" +
                            ChatColor.GRAY + "   - Opens an inventory view to edit the middle island's loot table.\n";
                    break;
                case "cages":
                    message = "" +
                            ChatColor.GREEN + "Skywars Cages Help" +
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

            switch(arg1) {
                case "info":
                    returnMessage = "Placeholder for # of islands, and # of set chests on each island";
                    break;
                case "setnumber":
                    returnMessage = "Placeholder for setting # of islands";
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

        if(args.length > 1) {
            String arg1 = args[1].toLowerCase();

            switch(arg1) {
                case "info":
                    returnMessage = "Placeholder for # of chests, and coords of each, on specified island";
                    break;
                case "clear":
                    if(args.length > 2) {
                        String islandNumber = args[2];
                        returnMessage = "Placeholder for erasing all chest data on island #" + islandNumber;
                    } else {
                        returnMessage = ChatColor.RED + "Usage: /skywars chests clear <island #>";
                    }
                    break;
                case "add":
                    if(args.length > 2) {
                        String islandNumber = args[2];
                        returnMessage = "Placeholder for adding a chest to island #" + islandNumber;
                    } else {
                        returnMessage = ChatColor.RED + "Usage: /skywars chests add <island #>";
                    }
                    break;
                default:
                    returnMessage = ChatColor.RED + "Usage: /skywars chests <info/clear/add>";
                    break;
            }
        } else {
            returnMessage = ChatColor.RED + "Usage: /skywars chests <info/clear/add>";
        }

        sender.sendMessage(returnMessage);
    }


    private void loot(String[] args, CommandSender sender) {

    }


    private void cages(String[] args, CommandSender sender) {

    }


    private void resetMap(String[] args, CommandSender sender) {

    }


    private void startGame(String[] args, CommandSender sender) {

    }
}
