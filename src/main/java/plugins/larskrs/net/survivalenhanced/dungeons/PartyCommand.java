package plugins.larskrs.net.survivalenhanced.dungeons;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugins.larskrs.net.survivalenhanced.general.Command;

import java.util.List;
import java.util.UUID;

public class PartyCommand extends Command {
    public PartyCommand () {

        super(
                "party",
                "survivalenhanced.command.party",
                "manage your parties",
                "party",
                (new String[]{"p"})

        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }

        Player player = (Player) sender;

        if (args.length > 0 && args[0].equalsIgnoreCase("leave")) {
            if (!PartyManager.HasParty(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Hey! You are not in a party, thus you can not leave.");
                return;
            }
            PartyManager.RemovePlayer(player);
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("create")) {

            if (PartyManager.HasParty(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Hey! You can not create a party whilst being in one.");
                return;
            }

            PartyManager.CreateParty(player);
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("list")) {

            if (!PartyManager.HasParty(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Hey! You are not in a party.");
                return;
            }

            for (UUID uuid : PartyManager.GetParty(player.getUniqueId()).GetMembers()
                 ) {
                Player p = Bukkit.getPlayer(uuid);
                player.sendMessage(ChatColor.YELLOW + " - " + p.getName() + p.isOnline());
            }
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("join")) {

            if (PartyManager.HasParty(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Hey! You are already in a party.");
                return;
            }

            if (args.length > 1) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Invalid Usage: could not find " + args[1]);
                    return;
                }
                if (!PartyManager.HasParty(target.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + target.getName() + " is not in a party!");
                    return;
                }
                Party party = PartyManager.GetParty(target.getUniqueId());
                PartyManager.AddMember(party, player);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
