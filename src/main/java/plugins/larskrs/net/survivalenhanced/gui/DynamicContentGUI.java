package plugins.larskrs.net.survivalenhanced.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plugins.larskrs.net.survivalenhanced.prefix.PrefixMenu;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.ArrayList;
import java.util.List;

public abstract class DynamicContentGUI extends GeneralGUI {

    private List<ItemStack> items;
    private Inventory inv;
    private int spaces;
    private int rows;
    private int startingIndex = 0;
    private String title;
    private int page;

    public DynamicContentGUI (String id, int page, int rows, String title) {
        super(
            id,
            rows
        );
        this.title = title;
        this.rows = rows;
        this.items = new ArrayList<>();
        this.page = page;
        CalcSpaces();
    }

    public int getPage () {
        return page;
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

    @Override
    public void onRenderGUI (Player p) {

        items.clear(); // Important to prevent accidental additions on move page.
        onItemsRender();
        float pages = GUIPageUtil.pageAmount(items, spaces);
        inv = Bukkit.createInventory(null, rows * 9, title + " ("+page+"/"+ (pages + 1) +") ");
        renderPageItems();


        p.openInventory(inv);
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
    public abstract void onItemsRender ();
    public abstract void onToolsRender ();

    public void renderPageItems () {

        List<ItemStack> pageItems = GUIPageUtil.GetPageItems(items, spaces, page);

        FillBorders();
        EmptySpaces();

        for (int i = 0; i < pageItems.size(); i++) {
            ItemStack item = pageItems.get(i);

            int index = startingIndex + i;
            inv.setItem(index, item);
        }

        NavItems(page);
        onToolsRender();
    }

    @Override
    public void onItemClick (int slotId, ItemStack item, Player p, InventoryAction action, InventoryType type) {

        ItemMeta meta = item.getItemMeta();

        if (meta.getDisplayName().equals(ChatColor.AQUA + "Next Page") || meta.getDisplayName().equals(ChatColor.AQUA + "Last Page")) {
            int page = Integer.parseInt(meta.getLocalizedName());

            this.page = page;
            OpenGUI(p);
            return;
        }

        onPageItemClick(slotId, item, p, action, type);
    }

    public abstract void onPageItemClick (int slotId, ItemStack item, Player p, InventoryAction action, InventoryType type);
    @Override
    public void onRenderUpdate() {
        getInventory().clear();
        RefreshItems();
        renderPageItems();
    }
    public void RefreshItems () {
        items.clear();
        onItemsRender();
    }
    @Override
    public Inventory getInventory () {
        return this.inv;
    }
    public List<ItemStack> getItems () {
        return items;
    }
    public void SetItems(List<ItemStack> items) {
        this.items = items;
    }

    public void SetPage(int page) {
        this.page = page;
    }
}
