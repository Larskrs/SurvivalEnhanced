package plugins.larskrs.net.survivalenhanced.gui;

import org.bukkit.inventory.ItemStack;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.ArrayList;
import java.util.List;

public class GUIPageUtil {



    public static List<ItemStack> GetPageItems (List<ItemStack> items, int spaces, int page) {

        // if we are on page 0 then we will remove no items
        //      0 * 27 = 0    0 rows removed
        //      1 * 27 = 27   3 rows removed
        int upperBound = page * spaces;
        int lowerBound = upperBound - spaces;

        List<ItemStack> newItems = new ArrayList<>();
        for (int i = lowerBound; i < upperBound; i++) {
            try {
                newItems . add(items.get(i));
            } catch (IndexOutOfBoundsException x) {
                break;
            }
        }

        return newItems;
    }

    public static boolean isPageValid (List<ItemStack> items, int page, int spaces) {
        if (page <= 0) { return false; }

        int upperBound = page * spaces;
        int lowerBound = upperBound - spaces;

        return items.size() > lowerBound;
    }

    public static long pageAmount (List<ItemStack> items, int spaces) {

        long x = (items.size() / spaces);
        long round = Math.round(x + 0.4);
        Messanger.InfoConsole(x + " | " + spaces + " | " + round);
        return round + 1;

    }



}
