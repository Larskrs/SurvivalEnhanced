package plugins.larskrs.net.survivalenhanced.world;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import plugins.larskrs.net.survivalenhanced.general.Command;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WorldCommand extends Command {

    public WorldCommand () {
        super(
                "world",
                "survivalenhanced.command.world",
                "World management command.",
                "world",
                (new String[]{"worlds"})

        );
    }


    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Invalid Usage: Here are your valid arguments.");
            sender.sendMessage(ChatColor.YELLOW + " - /world load <world_name>" );
            sender.sendMessage(ChatColor.YELLOW + " - /world unload <world_name>" );
            sender.sendMessage(ChatColor.YELLOW + " - /world tp <world_name>" );
            sender.sendMessage(ChatColor.YELLOW + " - /world create <world_name> <world_type> <generator>" );
            return;
        }

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "load": LoadWorld(sender, args); return;
                case "unload": UnLoadWorld(sender, args); return;
                case "create": CreateWorld(sender, args); return;
                case "tp": TeleportWorld(sender, args); return;
            }
        }
    }

    private void TeleportWorld(CommandSender sender, String[] args) {

            Player target = null;

            if (!(sender instanceof Player)) {
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Invalid Usage: /world tp " + args[1] + " <player>");
                    return;
                }
            }

            if (args.length > 2) {
                target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid Usage: There is not player with the name of: " + args[1]);
                    return;
                }
            } else {
                target = (Player) sender;
            }

        if (args.length == 1) {
            sender.sendMessage(ChatColor.RED + "Invalid Usage: You have to pick a world to teleport to. /world tp <world_name>");
            sender.sendMessage(ChatColor.YELLOW + " Examples:");
            for (World world : Bukkit.getWorlds()
                 ) {
                sender.sendMessage(ChatColor.YELLOW + " - /world tp " + world.getName());
            }
            return;
        }

        World world = null;
        if (args.length >= 2) {
            world = Bukkit.getWorld(args[1]);
            if (world == null) {
                sender.sendMessage(ChatColor.RED + "Invalid Usage: Could not find a loaded world: " + args[1]);
                return;
            }
        }

        target.teleport(world.getSpawnLocation());
        sender.sendMessage(ChatColor.GREEN + "You teleported " +
                (sender instanceof Player
                        ? "yourself"
                        : target.getName()
                ) + " to " + world.getName() + "!");

    }

    private void CreateWorld(CommandSender sender, String[] args) {
    }

    private void UnLoadWorld(CommandSender sender, String[] args) {
    }

    private void LoadWorld(CommandSender sender, String[] args) {
    }

    public boolean isWorldFile (File file) {
        if (!file.isDirectory()) {
            return false;
        }
        List<File> children = Arrays.stream(file.listFiles()).collect(Collectors.toList());
        return children.stream().map(File::getName).collect(Collectors.toList()).contains("level.dat");
    }

    public List<File> getUnloadedWorldFiles () {

        List<File> files = new ArrayList<>();

        for (File w : Bukkit.getWorldContainer().listFiles()
             ) {

            if (!isWorldFile(w)) {
                continue;
            }

            if (Bukkit.getWorlds().stream().map(world -> world.getName()).collect(Collectors.toList()).contains(w.getName())) {
                continue;
            }

            files.add(w);
        }
        return files;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {

        List<String> options = new ArrayList<>();

        if (args.length == 1) {
            options.add("list");
            options.add("load");
            options.add("unload");
            options.add("create");
            options.add("tp");
            return StringUtil.copyPartialMatches(args[0], options, new ArrayList<>());
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("tp")) {
            for (World w : Bukkit.getWorlds()) {
                options.add(w.getName());
            }
            return StringUtil.copyPartialMatches(args[1], options, new ArrayList<>());
        }
        if (args.length == 2 && (args[0].equalsIgnoreCase("load"))) {
            for (File f : getUnloadedWorldFiles()
                 ) {
                options.add(f.getName());
            }
            return StringUtil.copyPartialMatches(args[1], options, new ArrayList<>());
        }
        if (args.length == 2 && (args[0].equalsIgnoreCase("tp"))) {
            return StringUtil.copyPartialMatches(args[1], Bukkit.getWorlds().stream().map(world -> world.getName()).collect(Collectors.toList()), new ArrayList<>());
        }
        if (args.length == 2 && (args[0].equalsIgnoreCase("unload"))) {
            return StringUtil.copyPartialMatches(args[1], Bukkit.getWorlds().stream().map(world -> world.getName()).collect(Collectors.toList()), new ArrayList<>());
        }

        return null;
    }
}
