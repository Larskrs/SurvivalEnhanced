package plugins.larskrs.net.survivalenhanced.general;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Command extends BukkitCommand {


    boolean isEnabled = false;

    public void DisableCommand () {
        Messanger.Console(ChatColor.YELLOW + "Disabled command /" + getName());
        isEnabled = false;
    }

    public Command(String command, String permission, String description, String usage, String[] aliases) {
        super(command);

        this.setAliases(Arrays.asList(aliases));
        this.setDescription(description);
        this.setPermission(permission);
        this.setPermissionMessage(ChatColor.RED + " ☠ You do not have permission to use this command ☠ ");
        this.setUsage(usage);

        RegisterCommandPermission (permission, description);

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

        isEnabled = true;
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Loaded command: " + command);
    }

    private void RegisterCommandPermission(String permission, String description) {
        Set<Permission> permissions = Bukkit.getPluginManager().getPermissions();
        List<String> permissionStrings = permissions.stream().map(Permission::getName).collect(Collectors.toList());

        if (!permissionStrings.contains(permission)) {
            Bukkit.getPluginManager().addPermission(new Permission(permission, description));
            Messanger.InfoConsole("Registered new permission: " + permission);
        }
    }

    public Command(String command, String permission, String description, String usage) {
        super(command);

        this.setDescription(description);
        this.setPermission(permission);
        this.setPermissionMessage(ChatColor.RED + " ☠ You do not have permission to use this command ☠ ");
        this.setUsage(usage);

        RegisterCommandPermission (permission, description);

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

        isEnabled = true;
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Loaded command: " + command);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {

        if (!isEnabled) {
            sender.sendMessage(ChatColor.RED + "This command is disabled, or does not exist");
            return false;
        }
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