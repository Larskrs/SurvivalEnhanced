package plugins.larskrs.net.survivalenhanced.tools;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class LocationTools {

    public static Block getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }
    public static Location getRandomLocation(World origin, double minRadius, double maxRadius) {
        double x = RandomRange(minRadius, maxRadius);
        double z = RandomRange(minRadius, maxRadius);


        Location location = origin.getHighestBlockAt((int) x, (int) z).getLocation();
        location.add(.5D,1D,.5D);

        return location;
    }

    public static double RandomRange(double min, double max) {
        return Math.floor(Math.random() * (max - min + 1) + min);
    }


    public static int getDistanceToGround(Location origin){
        Location loc = origin.clone();
        double y = loc.getBlockY();
        int distance = 0;
        for (double i = y; i >= -64; i--){
            loc.setY(i);
            if(loc.getBlock().getType().isSolid())break;
            distance++;
        }
        return distance;
    }

}
