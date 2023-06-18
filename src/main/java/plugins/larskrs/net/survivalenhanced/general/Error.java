package plugins.larskrs.net.survivalenhanced.general;

import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;

import java.util.logging.Level;

public class Error {
    public static void execute(SurvivalEnhanced plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(SurvivalEnhanced plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}