package plugins.larskrs.net.survivalenhanced.dependencies;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.plugin.RegisteredServiceProvider;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import static org.bukkit.Bukkit.getServer;

public class VaultDependency {


    private SurvivalEnhanced se;
    private static Chat chat = null;

    public void Setup(SurvivalEnhanced se) {

        this.se = se;

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            Messanger.InfoConsole("Vault was not found, some features will not work.");
            return;
        }

        SetupChat();
    }
    private boolean SetupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    public static Chat GetChat () {
        return chat;
    }
}
