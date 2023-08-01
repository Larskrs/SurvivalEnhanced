package plugins.larskrs.net.survivalenhanced.location;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.database.Database;
import plugins.larskrs.net.survivalenhanced.database.SQLite;
import plugins.larskrs.net.survivalenhanced.general.Errors;
import plugins.larskrs.net.survivalenhanced.watchover.WatchoverModule;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class LocationManager {



    public static void StoreLocation(Location location, Player player, LocationChange changeReason) {

        Database db = SurvivalEnhanced.getDatabase();
        Connection conn = db.getSQLConnection();
        PreparedStatement ps = null;

        try {
            conn = SurvivalEnhanced.getDatabase().getSQLConnection();
            ps = conn.prepareStatement("INSERT INTO " + "locations" + " (player,location,change) VALUES(?,?,?)"); // IMPORTANT. In SQLite class, We made 3 colums. player, Kills, Total.
            ps.setString(1, player.getUniqueId().toString());                                             // YOU MUST put these into this line!! And depending on how many
            // colums you put (say you made 5) All 5 need to be in the brackets
            // Seperated with comma's (,) AND there needs to be the same amount of
            // question marks in the VALUES brackets. Right now i only have 3 colums
            // So VALUES (?,?,?) If you had 5 colums VALUES(?,?,?,?,?)

            ps.setString(2, LocationTools.StringifyLocation(location)); // This sets the value in the database. The colums go in order. Player is ID 1, kills is ID 2, Total would be 3 and so on. you can use
            // setInt, setString and so on. tokens and total are just variables sent in, You can manually send values in as well. p.setInt(2, 10) <-
            // This would set the players kills instantly to 10. Sorry about the variable names, It sets their kills to 10 i just have the variable called
            // Tokens from another plugin :/
            ps.setInt(3, changeReason.ordinal());
            ps.executeUpdate();

            return;
        } catch (SQLException ex) {
            SurvivalEnhanced.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
                SurvivalEnhanced.getInstance().getLogger().log(Level.FINEST, "Added a new saved location... " + LocationTools.StringifyLocation(location) + " - " + player.getName());
            } catch (SQLException ex) {
                SurvivalEnhanced.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return;
    }

    public static StoredLocation[] GetPlayerLocations (UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = SurvivalEnhanced.getDatabase().getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + "locations" + " WHERE player = '"+uuid+"';");

            rs = ps.executeQuery();

            List<StoredLocation> locationList = new ArrayList<>();
            while(rs.next()){
                if(rs.getString("player").equalsIgnoreCase(uuid.toString())){
                    StoredLocation stored = new StoredLocation(
                            rs.getInt("location_id"),
                            UUID.fromString(rs.getString("player")),
                            LocationTools.TranslateStringLocation(rs.getString("location")),
                            LocationChange.values()[rs.getInt("change")],
                            rs.getTimestamp("created_at")
                    );
                    locationList.add(stored);
                }
            }

            return locationList.toArray(new StoredLocation[0]);
        } catch (SQLException ex) {
            SurvivalEnhanced.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                SurvivalEnhanced.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return null;
    }

    public static StoredLocation[] GetAllStoredLocations () {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = SurvivalEnhanced.getDatabase().getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + "locations;");

            rs = ps.executeQuery();

            List<StoredLocation> locationList = new ArrayList<>();
            while(rs.next()){
                    StoredLocation stored = new StoredLocation(
                            rs.getInt("location_id"),
                            UUID.fromString(rs.getString("player")),
                            LocationTools.TranslateStringLocation(rs.getString("location")),
                            LocationChange.values()[rs.getInt("change")],
                            rs.getTimestamp("created_at")
                    );
                    locationList.add(stored);
            }

            return locationList.toArray(new StoredLocation[0]);
        } catch (SQLException ex) {
            SurvivalEnhanced.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                SurvivalEnhanced.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return null;
    }

    public void Setup() {
        SetupTable ();
    }

    String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS locations (" +
            "`location_id` INTEGER PRIMARY KEY AUTOINCREMENT," +
            "`player` varchar(36) NOT NULL," +
            "`location` varchar(128)," +
            "`change` int(11)," +
            "`created_at` DATETIME DEFAULT CURRENT_TIMESTAMP" +
            ");";

    private void SetupTable() {
        Database db = SurvivalEnhanced.getDatabase();
        Connection conn = db.getSQLConnection();

        try {
            Statement s = conn.createStatement();
            s.executeUpdate(SQLiteCreateTokensTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
