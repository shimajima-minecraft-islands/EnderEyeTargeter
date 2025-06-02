package io.mewb.enderEyeTargeter;

import io.mewb.enderEyeTargeter.listeners.EnderEyeListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EnderEyeTargeter extends JavaPlugin {

    // This variable will store the custom stronghold location loaded from the config.
    private Location strongholdLocation;

    /**
     * Called when the plugin is enabled.
     * This is where we load the configuration and register our event listener.
     */
    @Override
    public void onEnable() {
        // Create the default config.yml file if it doesn't already exist.
        // This ensures the user has a template to modify.
        saveDefaultConfig();

        // Load the stronghold location from the config.yml file.
        loadConfig();

        // Register the EnderEyeListener class to listen for events.
        // 'this' refers to the current plugin instance, which is needed by the listener.
        getServer().getPluginManager().registerEvents(new EnderEyeListener(this), this);

        // Log messages to the server console to confirm the plugin has started.
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

        // Retrieve the world name and coordinates from the config.
        // Default values are provided in case the config entries are missing.
        String worldName = config.getString("stronghold_location.world", "world");
        double x = config.getDouble("stronghold_location.x", 0.0);
        double y = config.getDouble("stronghold_location.y", 64.0);
        double z = config.getDouble("stronghold_location.z", 0.0);

        // Attempt to get the World object based on the configured world name.
        World world = Bukkit.getWorld(worldName);

        // Check if the specified world exists and is loaded.
        if (world == null) {
            getLogger().warning("World '" + worldName + "' specified in config.yml not found or not loaded!");
            getLogger().warning("Ender Eye redirection might not work correctly in the intended world.");

            // As a fallback, try to use the first loaded world on the server.
            if (!Bukkit.getWorlds().isEmpty()) {
                world = Bukkit.getWorlds().get(0);
                getLogger().warning("Using default loaded world: " + world.getName() + " as a fallback.");
            } else {
                // If no worlds are loaded at all, log a severe error.
                getLogger().severe("No worlds loaded on the server! Ender Eye redirection will not work.");
                this.strongholdLocation = null; // Set to null to indicate an invalid location
                return;
            }
        }
        // Create the Location object using the retrieved world and coordinates.
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