package plugins.larskrs.net.survivalenhanced.quest;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.general.Module;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import javax.annotation.RegEx;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class QuestModule extends Module implements Listener {


    public QuestModule() {
        super("QuestModule");
    }

    @Override
    public boolean onLoadModule() {
        String token = SurvivalEnhanced.getInstance().getConfig().getString("chat-gpt-token");

        new QuestCommand();
        Bukkit.getPluginManager().registerEvents(this, SurvivalEnhanced.getInstance());

        return false;
    }

    @Override
    public boolean onReloadModule() {
        return false;
    }

    @Override
    public boolean onUnloadModule() {
        return false;
    }

    @EventHandler
    public void onChat (AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();



    }


}
