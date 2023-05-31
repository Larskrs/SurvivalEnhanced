package plugins.larskrs.net.survivalenhanced.stable;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class StableManager {

    public HashMap<String, Stable> stables;
    private YamlConfiguration stableConfig;

    public StableManager () {
        SurvivalEnhanced.GetFileManager().LoadFile("stable.yml");
        this.stables = new HashMap<>();
    }

    public Stable GetStable (UUID playerID) {
        for (Stable stb : stables.values()
             ) {
            if (stb.getOwner() == playerID) {
                return stb;
            }
        }
        return null;
    }
    public Stable GetStable (String name) {
        return stables.get(name);
    }

    public void RegisterStable (String name, UUID owner, Location location) {
        if (StableExists(name)) { return; }

        Stable stable = new Stable(name, owner, location);
        stables.put(name, stable);

    }


    public boolean StableExists (String stableName) {
        return stables.containsKey(stableName);
    }


    public void SetStableCenter(String name, Location location) {
        Stable stable = GetStable(name);
        if (stable == null) { return; }

        stable.setLocation(location);
    }
}
