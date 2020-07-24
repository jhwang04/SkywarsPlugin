package com.justinhwang.skywarsplugin.commands;

import com.justinhwang.skywarsplugin.SkywarsPlugin;
import org.bukkit.*;
//import org.bukkit.block.data.type.Chest;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SkywarsCommand implements CommandExecutor {
    private Inventory templateInv = Bukkit.createInventory(null, 54, "Loot");

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


        //filling in the bottom slots of the templateInv inventory view
        ItemStack grayGlass = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = grayGlass.getItemMeta();

        glassMeta.setDisplayName(ChatColor.GRAY + "Cannot use these slots!");
        //List<String> lore = Arrays.asList(ChatColor.GRAY + "Cannot use these slots!");
        //glassMeta.setLore(lore);
        grayGlass.setItemMeta(glassMeta);

        for(int i = 45; i < 54; i++) {
            templateInv.setItem(i, grayGlass);
        }

        ItemStack confirmSword = new ItemStack(Material.NETHERITE_SWORD, 1);
        ItemMeta swordMeta = confirmSword.getItemMeta();
        swordMeta.setDisplayName(ChatColor.GREEN + "Confirm changes!");
        confirmSword.setItemMeta(swordMeta);

        ItemStack eraseAll = new ItemStack(Material.BRICK);
        ItemMeta eraserMeta = eraseAll.getItemMeta();
        eraserMeta.setDisplayName(ChatColor.DARK_RED + "Clear all items!");
        eraseAll.setItemMeta(eraserMeta);

        ItemStack revert = new ItemStack(Material.BARRIER);
        ItemMeta revertMeta = revert.getItemMeta();
        revertMeta.setDisplayName(ChatColor.RED + "Exit and revert all changes!");
        revert.setItemMeta(revertMeta);

        templateInv.setItem(48, eraseAll);
        templateInv.setItem(49, revert);
        templateInv.setItem(50, confirmSword);
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
                            ChatColor.GRAY + "   - Sets the location of the specified island & chest number to the player's current location.\n" +
                            ChatColor.GOLD + "- /skywars chests number <info/set>\n" +
                            ChatColor.GRAY + "   - Sets the number of chests that SkywarsPlugin is expecting on each spawn island.";
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
                            plugin.saveChestInfo();
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
                        } else {
                            returnMessage = ChatColor.RED + "Please enter a valid number!";
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
                                String addedLine = "Island #" + i + " has " + numberOfChests + "/" + chestsPerIsland + " chest locations set.\n";
                                if(numberOfChests == chestsPerIsland) {
                                    returnMessage += ChatColor.GOLD + addedLine;
                                } else {
                                    returnMessage += ChatColor.AQUA + addedLine;
                                }

                            }
                        }

                    }
                    break;
                case "clear":
                    if(args.length > 2) {
                        if(isParsable(args[2])) {
                            int arg2 = Integer.parseInt(args[2]);
                            int chestsOnIsland = howManyChests(arg2);
                            int i = chestsOnIsland;

                            while(chestsOnIsland > 0) {
                                chestInfo.set("island_" + arg2 + "_" + i, null);
                                plugin.saveChestInfo();
                                i--;
                            }
                            returnMessage = ChatColor.GOLD + "" + chestsOnIsland + " chest locations cleared for island #" + arg2 + "!";
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
                                if(arg2 <= chestInfo.getInt("numberOfIslands") && arg2 > 0) {

                                    int chestNumber = howManyChests(arg2) + 1;

                                    if(chestNumber <= chestInfo.getInt("chestsOnSpawnIslands") || arg2 == 0) {

                                        Location loc = ((Player) sender).getLocation();

                                        chestInfo.set("island_" + arg2 + "_" + chestNumber + ".x", loc.getBlockX());
                                        chestInfo.set("island_" + arg2 + "_" + chestNumber + ".y", loc.getBlockY());
                                        chestInfo.set("island_" + arg2 + "_" + chestNumber + ".z", loc.getBlockZ());

                                        plugin.saveChestInfo();

                                        returnMessage = ChatColor.GOLD + "Island " + arg2 + ", Chest " + chestNumber + ", has been set to (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")";
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
                                plugin.saveChestInfo();
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

                    if(sender instanceof  Player) {
                        Inventory inv = Bukkit.createInventory(null, 54, "Island Loot");

                        inv.setContents(templateInv.getContents());

                        plugin.setPlayerConfiguringLoot((Player) sender);

                        for(int i = 0; i < 45; i++) {
                            ItemStack newItem = plugin.getLoot().getItemStack("islandloot." + i);
                            inv.setItem(i, newItem);
                        }

                        ((Player) sender).openInventory(inv);

                        returnMessage = ChatColor.GOLD + "Editing island loot...";
                    } else {
                        returnMessage = ChatColor.RED + "You must be a player to use this command!";
                    }

                    break;
                case "mid":

                    if(sender instanceof  Player) {

                        Inventory inv = Bukkit.createInventory(null, 54, "Middle Island Loot");

                        inv.setContents(templateInv.getContents());

                        plugin.setPlayerConfiguringLoot((Player) sender);

                        for(int i = 0; i < 45; i++) {
                            ItemStack newItem = plugin.getLoot().getItemStack("midloot." + i);
                            inv.setItem(i, newItem);
                        }

                        ((Player) sender).openInventory(inv);

                        returnMessage = ChatColor.GOLD + "Editing middle loot...";
                    } else {
                        returnMessage = ChatColor.RED + "You must be a player to use this command!";
                    }

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
        FileConfiguration chestInfo = plugin.getChestInfo();

        if(args.length > 1) {
            String arg1 = args[1].toLowerCase();

            switch(arg1) {
                case "info":

                    returnMessage = headerLine;
                    returnMessage += ChatColor.GOLD + "                   Cage Info\n";
                    returnMessage += headerLine;

                    int numberOfIslands = chestInfo.getInt("numberOfIslands");
                    for(int i = 1; i <= numberOfIslands; i++) {
                        if(chestInfo.contains("cage_" + i + ".")) {
                            int x = chestInfo.getInt("cage_" + i + ".x");
                            int y = chestInfo.getInt("cage_" + i + ".y");
                            int z = chestInfo.getInt("cage_" + i + ".z");

                            returnMessage += ChatColor.GOLD + "Island #" + i + " has its cage set to (" + x + ", " + y + ", " + z + ")\n";
                        } else {
                            returnMessage += ChatColor.AQUA + "Island #" + i + " does not have a cage location set!\n";
                        }
                    }
                    break;
                case "set":
                    if(args.length > 2) {
                        if (isParsable(args[2])) {
                            int arg2 = Integer.parseInt(args[2]);
                            if (arg2 <= chestInfo.getInt("numberOfIslands") && arg2 > 0) {
                                Location loc = ((Player) sender).getLocation();

                                int x = loc.getBlockX();
                                int y = loc.getBlockY();
                                int z = loc.getBlockZ();

                                chestInfo.set("cage_" + arg2 + ".x", x);
                                chestInfo.set("cage_" + arg2 + ".y", y);
                                chestInfo.set("cage_" + arg2 + ".z", z);

                                plugin.saveChestInfo();

                                returnMessage = ChatColor.GOLD + "The cage location for island #" + arg2 + " has been set to (" + x + ", " + y + ", " + z + ")";
                            } else {
                                returnMessage = ChatColor.RED + "The island you specified, " + arg2 + ", is out of range. Use \"/skywars islands\" for more details.";
                            }
                        } else {
                            returnMessage = ChatColor.RED + "Please enter a valid number!";
                        }
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
        String returnMessage = "placeholder";

        if(!areAllChestsSet()) {
            returnMessage = ChatColor.RED + "You need to set all the chest locations for the islands!\nUse \"/skywars chests info\" to see what's missing!";
        } else {
            int numberOfIslands = plugin.getChestInfo().getInt("numberOfIslands");

            ItemStack[] islandLoot = new ItemStack[0];
            ItemStack[] midLoot = new ItemStack[0];

            //creating the list of items that can be selected
            for(int i = 0; i < 45; i++) {
                ItemStack newIslandItem = plugin.getLoot().getItemStack("islandloot." + i);
                if(newIslandItem != null) {
                    islandLoot = addItem(islandLoot, newIslandItem);
                }

                ItemStack newMidItem = plugin.getLoot().getItemStack("midloot." + i);
                if(newMidItem != null) {
                    midLoot = addItem(midLoot, newMidItem);
                }
            }

            // REMEMBER TO CHANGE THIS TO BE THE GAME WORLD INSTEAD OF THE TEMPLATE WORLD! VERY IMPORTANT!
            String worldName = plugin.getConfig().getString("template_world");
            World world = Bukkit.getWorld(worldName);

            if(world == null) {
                sender.sendMessage(ChatColor.RED + "You need a valid template world! Set it by using \"/skywars config templateworld\"");
            }



            //for each island...
            for(int i = 0; i <= numberOfIslands; i++) {

                int numberOfChests = howManyChests(i);
                ItemStack[] variableLoot;
                if(i == 0) {
                    variableLoot = midLoot;
                } else {
                    variableLoot = islandLoot;
                }

                //for every item to add to a chest...
                for(int j = 0; j < 27; j++) {
                    Chest[] chests = new Chest[0];

                    //for every chest in the length...
                    for(int c = 1; c <= numberOfChests; c++) {
                        int chestX = plugin.getChestInfo().getInt("island_" + i + "_" + c + ".x");
                        int chestY = plugin.getChestInfo().getInt("island_" + i + "_" + c + ".y");
                        int chestZ = plugin.getChestInfo().getInt("island_" + i + "_" + c + ".z");

                        if(world.getBlockAt(chestX, chestY, chestZ).getType() != Material.CHEST) {
                            world.getBlockAt(chestX, chestY, chestZ).setType(Material.CHEST);
                        }

                        //Chest newChest = (Chest) world.getBlockAt(chestX, chestY, chestZ).getBlockData();
                        Chest newChest = (Chest) world.getBlockAt(chestX, chestY, chestZ).getState();
                        newChest.getInventory().clear();

                        chests = addChest(chests, newChest);
                    }

                    //will not override a non-empty slot
                    boolean isValidItemSlot = false;
                    while(isValidItemSlot == false) {

                        int chosenChest = (int) (Math.random() * numberOfChests);
                        int chosenSlot = (int) (Math.random() * 27);
                        int chosenItem = 0;

                        chosenItem = (int) (Math.random() * variableLoot.length);

                        //if slot is empty
                        if(chests[chosenChest].getInventory().getItem(chosenSlot) == null || variableLoot.length == 0) {
                            Bukkit.getLogger().info("Good! " + chests[chosenChest].getInventory().getItem(chosenSlot) + ", slot + " + chosenSlot);

                            if(variableLoot.length != 0) {
                                chests[chosenChest].getInventory().setItem(chosenSlot, variableLoot[chosenItem]);
                            }

                            isValidItemSlot = true;
                        } else {
                            Bukkit.getLogger().info("Was not valid!");
                        }
                    }

                }
            }

        }

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

    // are all chests placed
    public boolean areAllChestsSet() {
        boolean chestsSet = true;

        for(int i = 1; i <= plugin.getChestInfo().getInt("numberOfIslands"); i++) {
            if(howManyChests(i) != plugin.getChestInfo().getInt("chestsOnSpawnIslands")) {
                chestsSet = false;
            }
        }

        return chestsSet;
    }

    //adds element to an array
    public ItemStack[] addItem(ItemStack[] old, ItemStack x) {
        int oldNum = old.length;
        ItemStack[] newArray = new ItemStack[oldNum + 1];

        for(int i = 0; i < oldNum; i++) {
            newArray[i] = old[i];
        }
        newArray[oldNum] = x;

        return newArray;
    }

    //adds a chest to an array
    public Chest[] addChest(Chest[] old, Chest x) {
        int oldNum = old.length;
        Chest[] newArray = new Chest[oldNum + 1];

        for(int i = 0; i < oldNum; i++) {
            newArray[i] = old[i];
        }
        newArray[oldNum] = x;

        return newArray;
    }


}
