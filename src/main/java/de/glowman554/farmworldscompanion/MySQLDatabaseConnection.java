package de.glowman554.farmworldscompanion;

import org.yaml.snakeyaml.util.UriEncoder;

import java.io.IOException;
import java.sql.*;
import java.util.UUID;
public class MySQLDatabaseConnection  {
    private Connection connect = null;

    public MySQLDatabaseConnection(String url, String database, String username, String password) throws ClassNotFoundException, SQLException, IOException {
        connect = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s&autoReconnect=true", url, database, username, UriEncoder.encode(password)));
    }

    public void close() {
        try {
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ScheduledTeleport getPlayerScheduledTeleport(UUID uuid) throws SQLException {
        PreparedStatement st = connect.prepareStatement("select worldId, worldLevel from scheduledTeleports where uuid = ?");
        st.setString(1, uuid.toString());

        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            int result = rs.getInt("worldLevel");
            ScheduledTeleport teleport = new ScheduledTeleport(rs.getString("worldId"), rs.getInt("worldLevel"));
            rs.close();
            st.close();
            return teleport;
        } else {
            rs.close();
            st.close();
            return null;
        }
    }

    public void deletePlayerScheduleTeleport(UUID uuid) throws SQLException {
        PreparedStatement st = connect.prepareStatement("delete from scheduledTeleports where uuid = ?");
        st.setString(1, uuid.toString());
        st.execute();
        st.close();
    }

    public record ScheduledTeleport(String id, int level) {

    }
}
