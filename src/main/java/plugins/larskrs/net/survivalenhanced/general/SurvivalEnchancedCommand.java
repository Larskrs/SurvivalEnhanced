package plugins.larskrs.net.survivalenhanced.general;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import plugins.larskrs.net.survivalenhanced.hats.HatItem;
import plugins.larskrs.net.survivalenhanced.hats.HatModule;

import java.util.ArrayList;
import java.util.List;

public class SurvivalEnchancedCommand extends Command {

    public SurvivalEnchancedCommand () {
        super (
                "survivalenhanced",
                "survivalenhanced.command.admin",
                "Manage the survival enhanced server.",
                "survivalenhanced",
                new String[]{"se", "survivale"}
        );
    }


    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            ReloadCommand(sender,args); return;
        }

    }

    private void ReloadCommand(CommandSender sender, String[] args) {
        if (args.length > 1 && args[1].equalsIgnoreCase("all")) {
            sender.sendMessage(ChatColor.YELLOW + "Reloading " + ChatColor.AQUA + "all " + ChatColor.YELLOW + "modules.");
            ModuleManager.ReloadModules();
            return;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {


        List<String> options = new ArrayList<>();

        if (args.length > 1 && args[0].equalsIgnoreCase("reload")) {
            for (Module m : ModuleManager.GetModules()
                 ) {
                if (m.isEnabled()) {
                    options.add(m.getId());
                }
            }
            options.add("all");

            return StringUtil.copyPartialMatches(args[1], options, new ArrayList<>());
        }

        return null;
    }
}
