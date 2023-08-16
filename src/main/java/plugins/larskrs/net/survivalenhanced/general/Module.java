package plugins.larskrs.net.survivalenhanced.general;

import org.bukkit.ChatColor;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.Set;
import java.util.logging.Level;

public abstract class Module {

    private String id;
    private boolean enabled = false;

    public Module (String _id) {
        id = _id;
    }

    public void SetEnabled (boolean state) {
        this.enabled = state;
    }
    public boolean isEnabled () {
        return this.enabled;
    }
    public void Load() {

        SetEnabled(!onLoadModule());
        if (isEnabled()) {
            // Loaded successfully.
            Messanger.Console( ChatColor.GREEN + "Successfully loaded module: " + ChatColor.YELLOW + id);
        } else {
            // Failed to load
            Messanger.Console(ChatColor.RED + "Failed to load module: " + ChatColor.YELLOW + id);
        }
    }

    public abstract boolean onLoadModule ();

    public abstract boolean onReloadModule ();
    public abstract boolean onUnloadModule ();

    protected String getId() {
        return id;
    }

    public void Unload() {
        SetEnabled(!onUnloadModule());
        if (!isEnabled()) {
            // Loaded successfully.
            Messanger.Console( ChatColor.GREEN + "Successfully unloaded module: " + ChatColor.YELLOW + id);
        } else {
            // Failed to unload
            Messanger.Console( ChatColor.RED + "Failed to unloaded module: " + ChatColor.YELLOW + id);
        }
    }
    public void Reload () {
        SetEnabled(!onReloadModule());
        if (!isEnabled()) {
            // Loaded successfully.
            Messanger.Console( ChatColor.GREEN + "Successfully reloaded module: " + ChatColor.YELLOW + id);
        } else {
            // Failed to unload
            Messanger.Console( ChatColor.RED + "Failed to reload module: " + ChatColor.YELLOW + id);
        }
    }
}
