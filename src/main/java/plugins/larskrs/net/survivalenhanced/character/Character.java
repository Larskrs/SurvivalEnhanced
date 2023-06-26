package plugins.larskrs.net.survivalenhanced.character;

import plugins.larskrs.net.survivalenhanced.race.Race;

import java.util.UUID;

public class Character {

    private UUID player;
    private String raceID;
    private String name;

    public Character (UUID player) {

    }
    public String getRaceID () {
        return raceID;
    }
    public String getName () {
        return name;
    }
    public void SetName(String name) {
        this.name = name;
    }
    public void SetRace (Race race) {
        this.raceID = race.getID();
    }

}
