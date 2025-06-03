package io.mewb.enderEyeTargeter.listeners;

import io.mewb.enderEyeTargeter.EnderEyeTargeter;
import org.bukkit.Location;
import org.bukkit.entity.EnderSignal;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EnderEyeListener implements Listener {


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

        if (event.getEntity() instanceof EnderSignal) {
            EnderSignal eye = (EnderSignal) event.getEntity();


            Location customTarget = plugin.getStrongholdLocation();


            if (customTarget != null && customTarget.getWorld() != null) {
                eye.setTargetLocation(customTarget);
                eye.setDespawnTimer(Integer.MAX_VALUE);

                // Log that an Ender Eye has been successfully redirected.
                //plugin.getLogger().info("Redirected an Ender Eye to custom stronghold at: " + customTarget.toString());
            } else {
                plugin.getLogger().warning("Failed to redirect Ender Eye: Custom stronghold location is invalid or world not found in config.yml.");
            }
        }
    }
}