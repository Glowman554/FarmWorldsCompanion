package de.glowman554.farmworldscompanion.listener;

import de.glowman554.farmworldscompanion.FarmWorldsCompanion;
import de.glowman554.farmworldscompanion.MySQLDatabaseConnection;
import de.glowman554.farmworldscompanion.WorldId;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            MySQLDatabaseConnection.ScheduledTeleport teleport = FarmWorldsCompanion.getInstance().getDatabase().getPlayerScheduledTeleport(event.getPlayer().getUniqueId());
            if (teleport == null) {
                event.getPlayer().sendMessage("Farmwelten companion fehler!");
            } else {
                switch (teleport.id()) {
                    case "WATER":
                        event.getPlayer().performCommand(FarmWorldsCompanion.getInstance().getWaterCommand());
                        break;
                    case "RTP":
                        event.getPlayer().performCommand(FarmWorldsCompanion.getInstance().getRtpCommand());
                        break;
                    default:
                        event.getPlayer().performCommand(WorldId.valueOf(teleport.id()).getTeleportCommands()[teleport.level()]);
                        break;
                }

                FarmWorldsCompanion.getInstance().getDatabase().deletePlayerScheduleTeleport(event.getPlayer().getUniqueId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
