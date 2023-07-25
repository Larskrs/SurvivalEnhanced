package plugins.larskrs.net.survivalenhanced.quest;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugins.larskrs.net.survivalenhanced.general.Command;

import java.util.List;

public class QuestCommand extends Command {
    public QuestCommand () {
        super("quest",
                "survivalenhanced.command.quest",
                "Interact with the quest system",
                "quest", new String[]{});
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
