package plugins.larskrs.net.survivalenhanced.race;

import org.bukkit.configuration.file.YamlConfiguration;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;

import java.util.HashMap;

public class RaceManager {

    private static RaceManager instance;
    private HashMap<String, Race> races;
    private YamlConfiguration raceConfig;

    public void Setup () {
        instance = this;
        SurvivalEnhanced.GetFileManager().LoadFile("race.yml");
        this.races = new HashMap<>();
        this.raceConfig = SurvivalEnhanced.GetFileManager().GetYamlFromString("race.yml");
        SetDefaultConfigValues();

        if (!raceConfig.getBoolean("enabled")) {
            return;
        }

        LoadRaces();
    }

    public static RaceManager getInstance() {
        return instance;
    }


    public void LoadRaces () {

    }

    public void SetDefaultConfigValues () {



    }


}
