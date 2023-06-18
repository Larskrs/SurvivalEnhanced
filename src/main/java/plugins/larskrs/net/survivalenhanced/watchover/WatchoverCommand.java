package plugins.larskrs.net.survivalenhanced.watchover;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugins.larskrs.net.survivalenhanced.Command;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;

import java.util.List;

public class WatchoverCommand extends Command {

    public WatchoverCommand(SurvivalEnhanced survivalEnhanced) {
        super(
                "watchover",
                "survivalenhanced.command.watchover",
                "This command will let you manage your players",
                "prefix",
                (new String[]{"wo"})

        );
    }

    private void ShowPlayers (CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) { return ; }

        Player p = (Player) sender;

        new PlayerWatchoverMenu(1).OpenGUI(p);


    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ShowPlayers(sender, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
