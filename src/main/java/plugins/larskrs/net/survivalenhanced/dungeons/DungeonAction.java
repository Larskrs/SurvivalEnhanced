package plugins.larskrs.net.survivalenhanced.dungeons;

public abstract class DungeonAction {

    private String name;


    public String getName () {
        return name;
    }

    public abstract void onActive ();

}
