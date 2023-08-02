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
        }
    }

    public abstract boolean onLoadModule ();

    protected String getId() {
        return id;
    }
}
