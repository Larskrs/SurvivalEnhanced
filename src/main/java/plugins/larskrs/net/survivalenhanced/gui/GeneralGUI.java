package plugins.larskrs.net.survivalenhanced.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.joml.Math;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.List;

public abstract class GeneralGUI {

    private int rows = 3;
    private int columns = 9;
    private Player holder;

    public Player getPlayer () {
        return holder;
    }

    private String id;

    public GeneralGUI (String id, int rows) {
        this.rows = rows;
        this.id = id;
    }

    public void OpenGUI (Player p) {
        this.holder = p;
        onRenderGUI(p);
        GUIManagar.getInstance().LinkGUI(p, this);
        Messanger.InfoConsole("Opened gui");
    }

    public abstract void onRenderGUI (Player p);


    public void setRows (int rows) {
        this.rows = Math.clamp(1, 6, rows);
    }


    public String GetID() {
        return id;
    }

    public abstract void onItemClick (ItemStack item, Player p, InventoryAction action);

    public void InteractItem(ItemStack currentItem, Player p, InventoryAction action) {
        onItemClick(currentItem, p, action);
    }
}
