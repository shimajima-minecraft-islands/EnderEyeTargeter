package io.mewb.enderEyeTargeter;

import io.mewb.enderEyeTargeter.listeners.EnderEyeListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EnderEyeTargeter extends JavaPlugin {


    private Location strongholdLocation;

    /**
     * Called when the plugin is enabled.
     * This is where we load the configuration and register our event listener.
     */
    @Override
    public void onEnable() {
        saveDefaultConfig();


        loadConfig();
        getServer().getPluginManager().registerEvents(new EnderEyeListener(this), this);

        getLogger().info("EnderEyeRedirector has been enabled!");
        if (strongholdLocation != null && strongholdLocation.getWorld() != null) {
            getLogger().info("Stronghold target location set to: " + strongholdLocation.getWorld().getName() + " " +
                    strongholdLocation.getX() + ", " + strongholdLocation.getY() + ", " + strongholdLocation.getZ());
        } else {
            getLogger().warning("Stronghold target location could not be loaded or is invalid. Check config.yml!");
        }
    }

    /**
     * Called when the plugin is disabled.
     * This is typically used for cleanup, though this plugin doesn't require much.
     */
    @Override
    public void onDisable() {
        getLogger().info("EnderEyeRedirector has been disabled!");
    }

    /**
     * Loads the stronghold coordinates from the plugin's config.yml file.
     */
    private void loadConfig() {
        FileConfiguration config = getConfig();


        String worldName = config.getString("stronghold_location.world", "world");
        double x = config.getDouble("stronghold_location.x", 0.0);
        double y = config.getDouble("stronghold_location.y", 64.0);
        double z = config.getDouble("stronghold_location.z", 0.0);

        World world = Bukkit.getWorld(worldName);


        if (world == null) {
            getLogger().warning("World '" + worldName + "' specified in config.yml not found or not loaded!");
            getLogger().warning("Ender Eye redirection might not work correctly in the intended world.");


            if (!Bukkit.getWorlds().isEmpty()) {
                world = Bukkit.getWorlds().get(0);
                getLogger().warning("Using default loaded world: " + world.getName() + " as a fallback.");
            } else {

                getLogger().severe("No worlds loaded on the server! Ender Eye redirection will not work.");
                this.strongholdLocation = null;
                return;
            }
        }

        this.strongholdLocation = new Location(world, x, y, z);
    }

    /**
     * Provides access to the loaded stronghold location for other classes (like the listener).
     * @return The Location object representing the custom stronghold target.
     */
    public Location getStrongholdLocation() {
        return strongholdLocation;
    }
}