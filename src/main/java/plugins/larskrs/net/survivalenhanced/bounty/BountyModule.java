package plugins.larskrs.net.survivalenhanced.bounty;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import plugins.larskrs.net.survivalenhanced.SurvivalEnhanced;
import plugins.larskrs.net.survivalenhanced.database.Database;
import plugins.larskrs.net.survivalenhanced.general.Errors;
import plugins.larskrs.net.survivalenhanced.general.Module;
import plugins.larskrs.net.survivalenhanced.location.LocationChange;
import plugins.larskrs.net.survivalenhanced.location.LocationTools;
import plugins.larskrs.net.survivalenhanced.location.StoredLocation;
import plugins.larskrs.net.survivalenhanced.tools.InventoryTool;
import plugins.larskrs.net.survivalenhanced.tools.Messanger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class BountyModule extends Module {

    String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS bounties (" +
            "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
            "`target` varchar(36) NOT NULL," +
            "`issuer` varchar(36) NOT NULL," +
            "`location` varchar(128)," +
            "`type` int(11)," +
            "`created_at` DATETIME DEFAULT CURRENT_TIMESTAMP," +
            "`deadline_at` DATETIME DEFAULT CURRENT_TIMESTAMP" +
            ");";

    public BountyModule() {
        super("BountyModule");
    }

    private void SetupTable() {
        Database db = SurvivalEnhanced.getDatabase();
        Connection conn = db.getSQLConnection();

        try {
            Statement s = conn.createStatement();
            s.executeUpdate(SQLiteCreateTokensTable);
            s.close();

            Messanger.Console("[!] Attempting to create bounty table.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onLoadModule() {

        SetupTable();

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


    public static void StoreBounty (UUID target, UUID issuer, ItemStack itemStack, BountyType type, Timestamp created, Timestamp deadline) {

        Database db = SurvivalEnhanced.getDatabase();
        Connection conn = db.getSQLConnection();
        PreparedStatement ps = null;

        try {
            conn = SurvivalEnhanced.getDatabase().getSQLConnection();
            ps = conn.prepareStatement("INSERT INTO " + "bounties" + " (target,issuer,reward,type,created_at,dungeon_at) VALUES(?,?,?,?,?,?,?)"); // IMPORTANT. In SQLite class, We made 3 colums. player, Kills, Total.
            ps.setString(1, target.toString());
            ps.setString(2, issuer.toString());

            ps.setString(3, InventoryTool.itemStackToBase64(itemStack));

            ps.setInt(4, type.ordinal());
            ps.setTimestamp( 6,created);
            ps.setTimestamp( 7,deadline);
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
                SurvivalEnhanced.getInstance().getLogger().log(Level.FINEST, "Added a new bounty... " + Bukkit.getPlayer(target).getName() + " reward: " + itemStack.getType().name() + " x" + itemStack.getAmount());
            } catch (SQLException ex) {
                SurvivalEnhanced.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return;
    }

    public static Bounty[] GetAllBounties () {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = SurvivalEnhanced.getDatabase().getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + "locations;");

            rs = ps.executeQuery();

            List<Bounty> bountyList = new ArrayList<>();
            while(rs.next()){
                Bounty bounty = new Bounty(
                        rs.getInt("id"),
                        UUID.fromString(rs.getString("target")),
                        UUID.fromString(rs.getString("issuer")),
                        InventoryTool.itemStackFromBase64(rs.getString("reward")),
                        BountyType.values()[rs.getInt("type")],
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("dungeon_at")
                );
                bountyList.add(bounty);
            }

            return bountyList.toArray(new Bounty[0]);
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
}
