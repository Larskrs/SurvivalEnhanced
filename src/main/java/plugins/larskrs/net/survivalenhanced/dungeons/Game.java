package plugins.larskrs.net.survivalenhanced.dungeons;

public abstract class Game {

    private int minPartySize = 1;
    private String id;

    public Game (String id) {
        this.id = id;
    }
    public Game SetMinPartySize (int minPartySize) {
        this.minPartySize = minPartySize;
        return this;
    }



    public void Finish () {
        onInit();
    }

    public abstract void onInit ();

}
