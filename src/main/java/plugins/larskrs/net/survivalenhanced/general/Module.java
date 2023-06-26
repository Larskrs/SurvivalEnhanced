package plugins.larskrs.net.survivalenhanced.general;

import org.bukkit.ChatColor;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.util.logging.Level;

public abstract class Module {

    private String id;
    private boolean enabled = false;

    public Module (String id) {
        this.id = id;
    }

    public void Load() {

        enabled = !onLoadModule();

        if (enabled) {
            // Loaded successfully.
            Messanger.Console( ChatColor.GREEN + "Successfully loaded module: " + ChatColor.YELLOW + id);
        }
    }

    public abstract boolean onLoadModule ();

    protected String getId() {
        return id;
    }

    protected boolean isEnabled() {
        return enabled;
    }
}
