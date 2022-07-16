# SkywarsPlugin
Spigot plugin for configurable skywars games, allowing for custom maps and randomized chest loot.

# Instructions for use

This plugin does not rely on Multiverse or other world-manager plugins.
As of 7/16/2022, the jar is not included in the git repository. To build it yourself, open it as an IntelliJ project and run "./gradlew clean build" from the project folder. It will show up in ./build/libs directory.

1) Add your template world to the server directory (You can create your own world or download a world from online).
2) Set your lobbyworld and templateworld with /skywars config <lobbyworld/templateworld>
3) Reload the plugins or restart your server.
4) Use "/skywars template" to teleport to your template world, and build a cage somewhere in the world (I recommend building it out of glass high above the center of the map) and /setworldspawn inside that cage.
5) Use "/skywars islands setnumber" to set the number of islands
6) Use "/skywars islands info" to verify that the number of islands is properly set.
7) Fly into each cage and use "/skywars cages set <island #>" to set the location of that island's cage. You can use "/skywars cages info" to check the coordinates of each cage.
8) Stand inside of each chest's location (break the chest and stand in the empty spot) and use "/skywars chests add <island #>"  to add a chest at that location. Remember to re-place the chest after you've set it's location.
9) Use "/skywars chests clear <island #> to clear all chest locations for that island, and "/skywars chests info" to check that the coordinates are correct.

Note: Island #0 is the center island.

10) Use "/skywars loot <mid/island>" to open the GUI to set the possible loot. Unfortunately, this plugin does not yet have the cability of setting weighted odds for each item, so if you want an item to be more likely, you can add multiple versions of the same item into the GUI. Remember to save and exit!
11) Once everything is saved, use "/lobby" to teleport to your lobby world. Use "/skywars resetmap" to reload the game world.
12) Use "/playskywars" to teleport to the game world. You should see a scoreboard on the right side of your screen, and a "waiting..." icon. Once 2 or more people join, a countdown should start.
13) Have fun!
