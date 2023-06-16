package plugins.larskrs.net.survivalenhanced.gui;

import org.bukkit.inventory.ItemStack;

public abstract class StaticContentGUI extends GeneralGUI {


    public StaticContentGUI(String id, int rows) {
        super(
           id,
           rows
        );
    }


    public abstract void onRenderItems();


    public abstract void onItemClick(ItemStack item);
}
