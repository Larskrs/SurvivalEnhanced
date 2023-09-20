package plugins.larskrs.net.survivalenhanced.bounty;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import plugins.larskrs.net.survivalenhanced.general.Command;

import java.util.List;

public class BountyCommand extends Command {
    public BountyCommand () {
        super(
                "bounty",
                "survivalenhanced.command.bounty",
                "Bounties command",
                "bounty"
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) { return; }
        Player p = (Player) sender;

        if (args.length > 0 && args[0].equalsIgnoreCase("create")) {
            CreateBountyArg(p, args);
        }

    }

    private void CreateBountyArg (Player issuer, String[] args) {

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }

    private void giveMapItem (Player target, Player p) {
        ItemStack i = new ItemStack(Material.FILLED_MAP, 1);
        MapMeta m = (MapMeta) i.getItemMeta();
        MapView view = Bukkit.createMap(p.getWorld());
        view.getRenderers().clear();
        view.addRenderer(new BountyMapRenderer(target.getName()));
        i.setDurability((short) view.getId());
        m.setMapView(view);
        i.setItemMeta(m);

        p.getInventory().addItem(i);
    }
}


