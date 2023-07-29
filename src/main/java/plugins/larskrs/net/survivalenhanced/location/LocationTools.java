package plugins.larskrs.net.survivalenhanced.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import plugins.larskrs.net.survivalenhanced.tools.NumberTools;

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

    public static String StringifyLocation (Location location) {

        double X = NumberTools.round(location.getX(), 2);
        double Y = NumberTools.round(location.getY(), 2);
        double Z = NumberTools.round(location.getZ(), 2);
        float Yaw = location.getYaw();
        float Pitch = location.getPitch();

        String stringified = location.getWorld().getName() + "," +
                X + "," +
                Y + "," +
                Z + "," +
                Yaw + "," +
                Pitch;

        return stringified;
    }

    public static Location TranslateStringLocation (String str) {
        String[] args = str.split(",");
        World world = Bukkit.getWorld(args[0]);
        if (world == null) { return null; }
        if (!NumberTools.isNumeric(args[1])) { return null; }
        Double X = Double.parseDouble(args[1]);

        if (!NumberTools.isNumeric(args[2])) { return null; }
        Double Y = Double.parseDouble(args[2]);

        if (!NumberTools.isNumeric(args[3])) { return null; }
        Double Z = Double.parseDouble(args[3]);

        if (!NumberTools.isNumeric(args[4])) { return null; }
        Float Yaw = Float.parseFloat(args[4]);

        if (!NumberTools.isNumeric(args[5])) { return null; }
        Float Pitch = Float.parseFloat(args[5]);

        return new Location(
                world,
                X,
                Y,
                Z,
                Yaw,
                Pitch
        );
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
