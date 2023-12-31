package de.glowman554.farmworldscompanion;

public enum WorldId {
    STONE,
    FIRE,
    ;

    private String[] teleportCommands;



    public String[] getTeleportCommands()
    {
        return teleportCommands;
    }

    public void setTeleportCommands(String[] teleportCommands)
    {
        this.teleportCommands = teleportCommands;
    }
}
