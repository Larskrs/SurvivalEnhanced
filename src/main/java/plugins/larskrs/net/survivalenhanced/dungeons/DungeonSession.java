package plugins.larskrs.net.survivalenhanced.dungeons;

public class DungeonSession {

    private Party party;
    private DungeonState state;
    private Dungeon dungeon;

    public DungeonSession (Party party) {
        this.party = party;

    }

    public void setState (DungeonState state) {
        this.state = state;
    }

    public void EndSession () {
        // TODO: logic to end session.
    }



}
