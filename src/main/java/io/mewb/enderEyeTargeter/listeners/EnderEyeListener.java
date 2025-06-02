package io.mewb.enderEyeTargeter.listeners;

import io.mewb.enderEyeTargeter.EnderEyeTargeter;
import org.bukkit.Location;
import org.bukkit.entity.EnderSignal;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EnderEyeListener implements Listener {

    // A reference to the main plugin class to access its configuration.
    private final EnderEyeTargeter plugin;

    /**
     * Constructor for the listener.
     * @param plugin The main plugin instance.
     */
    public EnderEyeListener(EnderEyeTargeter plugin) {
        this.plugin = plugin;
    }

    /**
     * This method is called whenever an entity is spawned in the world.
     * We use @EventHandler to tell Paper that this method should handle events.
     * @param event The EntitySpawnEvent that occurred.
     */
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        // Check if the entity that just spawned is an instance of an EnderSignal (thrown Ender Eye).
        if (event.getEntity() instanceof EnderSignal) {
            // Cast the generic Entity object to an EnderSignal specific object.
            EnderSignal eye = (EnderSignal) event.getEntity();

            // Get the custom stronghold location that was loaded by our main plugin class.
            Location customTarget = plugin.getStrongholdLocation();

            // Ensure the custom target location is valid (i.e., not null and has a valid world).
            // This check prevents potential NullPointerExceptions if the config was misconfigured.
            if (customTarget != null && customTarget.getWorld() != null) {
                // Set the EnderSignal's target location to our custom stronghold location.
                eye.setTargetLocation(customTarget);
                // Set the despawn timer to a very high value to prevent premature despawning,
                // as suggested by the SpigotMC thread for more reliable redirection.
                eye.setDespawnTimer(Integer.MAX_VALUE); // Keep the eye from despawning too quickly

                // Log that an Ender Eye has been successfully redirected.
                plugin.getLogger().info("Redirected an Ender Eye to custom stronghold at: " + customTarget.toString());
            } else {
                // If the custom target is invalid, log a warning.
                plugin.getLogger().warning("Failed to redirect Ender Eye: Custom stronghold location is invalid or world not found in config.yml.");
            }
        }
    }
}