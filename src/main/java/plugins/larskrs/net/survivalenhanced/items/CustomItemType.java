package plugins.larskrs.net.survivalenhanced.items;

public enum CustomItemType {
    HAT ("Hat"),
    WEAPON ("Weapon"),
    TOOL ("Tool"),
    ;

    public final String name;

    private CustomItemType (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
