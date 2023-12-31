package de.glowman554.farmworldscompanion;

import de.glowman554.farmworldscompanion.listener.PlayerJoinListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

public final class FarmWorldsCompanion extends JavaPlugin {
    private static FarmWorldsCompanion instance;

    public FarmWorldsCompanion() {
        instance = this;
    }

    private final FileConfiguration config = getConfig();

    private MySQLDatabaseConnection database;

    private String rtpCommand;
    private String waterCommand;

    private void loadWorldConfig() {
        for (WorldId id : WorldId.values()) {
            id.setTeleportCommands(config.getList(id.toString() + ".commands").toArray(String[]::new));
        }

        rtpCommand = config.getString("RTP.command");
        waterCommand = config.getString("WATER.command");
    }

    @Override
    public void onLoad() {
        config.addDefault("database.url", "changeme");
        config.addDefault("database.database", "changeme");
        config.addDefault("database.username", "changeme");
        config.addDefault("database.password", "changeme");

        config.addDefault("STONE.commands", new String[] {"say STONE 1", "say STONE 2", "say STONE 3", "say STONE 4", "say STONE 5", "say STONE 6", "say STONE 7", "say STONE 8", "say STONE 9"});
        config.addDefault("FIRE.commands", new String[] {"say FIRE 1", "say FIRE 2", "say FIRE 3", "say FIRE 4", "say FIRE 5"});
        config.addDefault("WATER.command", "say WATER");
        config.addDefault("RTP.command", "/rtp world world");

        config.options().copyDefaults(true);
        saveConfig();
        reloadConfig();

        getLogger().log(Level.INFO, "Opening database connection");

        try {
            database = new MySQLDatabaseConnection(config.getString("database.url"), config.getString("database.database"), config.getString("database.username"), config.getString("database.password"));
        } catch (ClassNotFoundException | SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        loadWorldConfig();
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @Override
    public void onDisable() {
        database.close();
    }

    public static FarmWorldsCompanion getInstance() {
        return instance;
    }

    public MySQLDatabaseConnection getDatabase() {
        return database;
    }

    public String getRtpCommand() {
        return rtpCommand;
    }

    public String getWaterCommand() {
        return waterCommand;
    }
}
