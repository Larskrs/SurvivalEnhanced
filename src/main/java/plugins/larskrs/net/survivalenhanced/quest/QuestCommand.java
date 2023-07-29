package plugins.larskrs.net.survivalenhanced.quest;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugins.larskrs.net.survivalenhanced.general.Command;
import plugins.larskrs.net.survivalenhanced.location.LocationTools;

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

        String locationString = args[0];
        player.teleport(LocationTools.TranslateStringLocation(locationString));

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
