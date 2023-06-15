package plugins.larskrs.net.survivalenhanced.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.ArrayList;
import java.util.List;

public class DynamicContentGUI {

    private List<ItemStack> items;
    private Inventory inv;
    private int spaces;
    private int rows;
    private int startingIndex = 0;
    private String title;

    public DynamicContentGUI (int rows, String title) {
        this.title = title;
        this.rows = rows;
        this.items = new ArrayList<>();
        CalcSpaces();

    }

    public void CalcSpaces () {

        spaces = rows * 9;


        startingIndex = 9;
        spaces = spaces - (2 * 9);

    }


    public void RegisterItemStack (ItemStack item) {
        items.add(item);
    }

    public void EmptySpaces () {
        for (int i = 0; i < spaces; i++) {
            int index = startingIndex + i;
            inv.setItem(index, new ItemStack(Material.AIR));
        }
    }

    public void OpenGUI (Player player, int page) {



        List<ItemStack> pageItems = GUIPageUtil.GetPageItems(items, spaces, page);

        inv = Bukkit.createInventory(null, rows * 9, title + " ("+page+"/"+( (items.size()/spaces) + 1 )+") ");

        FillBorders();
        EmptySpaces();

        for (int i = 0; i < pageItems.size(); i++) {
            ItemStack item = pageItems.get(i);

            int index = startingIndex + i;
            inv.setItem(index, item);
        }

        NavItems(page);

        player.openInventory(inv);
    }

    private void NavItems(int page) {
        if (GUIPageUtil.isPageValid(items, page + 1, spaces)) {
            ItemStack rightItem = new ItemStack(Material.ARROW);
            ItemMeta  rightMeta = rightItem.getItemMeta();

            rightMeta.setDisplayName(ChatColor.AQUA + "Next Page");
            rightMeta.setLocalizedName((page + 1) + "");

            rightItem.setItemMeta(rightMeta);
            inv.setItem(spaces + 9 + 8, rightItem);

        }
        if (GUIPageUtil.isPageValid(items, page - 1, spaces)) {
            ItemStack leftItem = new ItemStack(Material.ARROW);
            ItemMeta  leftMeta = leftItem.getItemMeta();

            leftMeta.setDisplayName(ChatColor.AQUA + "Last Page");
            leftMeta.setLocalizedName((page - 1) + "");

            leftItem.setItemMeta(leftMeta);
            inv.setItem(spaces + 9, leftItem);

        }
    }

    private void FillBorders() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < 9; j++) {
                    inv.setItem(i*9 + j, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
                }
            }
    }

}
