package plugins.larskrs.net.survivalenhanced.motd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugins.larskrs.net.survivalenhanced.general.Command;

import java.util.List;

public class MotdCommand extends Command {

    public MotdCommand () {

        super(
                "motd",
                "survivalenhanced.command.motd",
                "Message of the day",
                "motd"
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        MotdMenu menu = new MotdMenu(args[00]);
        menu.OpenGUI(player);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
