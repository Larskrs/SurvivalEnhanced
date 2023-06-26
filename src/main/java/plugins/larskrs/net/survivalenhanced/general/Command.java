package plugins.larskrs.net.survivalenhanced.general;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public abstract class Command extends BukkitCommand {


    public Command(String command, String permission, String description, String usage, String[] aliases) {
        super(command);

        this.setAliases(Arrays.asList(aliases));
        this.setDescription(description);
        this.setPermission(permission);
        this.setPermissionMessage(ChatColor.RED + " ☠ You do not have permission to use this command ☠ ");
        this.setUsage(usage);


        try
        {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap map = (CommandMap) field.get(Bukkit.getServer());
            map.register(command, this);
            //field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Loaded command: " + command);
    }
    public Command(String command, String permission, String description, String usage) {
        super(command);

        this.setDescription(description);
        this.setPermission(permission);
        this.setPermissionMessage(ChatColor.RED + " ☠ You do not have permission to use this command ☠ ");
        this.setUsage(usage);

        try
        {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap map = (CommandMap) field.get(Bukkit.getServer());
            map.register(command, this);
            //field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Loaded command: " + command);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender.hasPermission(this.getPermission())) {
            execute(sender, args);
        } else {
            sender.sendMessage(this.getPermissionMessage());
        }
        return false;
    }

    public abstract void execute(CommandSender sender, String[] args);

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return onTabComplete(sender, args);
    }
    public abstract List<String> onTabComplete(CommandSender sender, String[] args);
}