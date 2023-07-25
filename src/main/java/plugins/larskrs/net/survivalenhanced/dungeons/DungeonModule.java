package plugins.larskrs.net.survivalenhanced.dungeons;

import plugins.larskrs.net.survivalenhanced.general.Module;

import java.util.HashMap;

public class DungeonModule extends Module {

    public DungeonModule() {
        super("DungeonModule");
    }

    @Override
    public boolean onLoadModule() {


        new PartyManager();


        return false;
    }
}
