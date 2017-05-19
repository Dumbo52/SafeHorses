package com.michaelelin.SafeHorses;

import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

/**
 * The event listener for the SafeHorses plugin.
 */
public class SafeHorsesListener implements Listener {

    private SafeHorsesPlugin plugin;

    /**
     * Creates a new {@code SafeHorsesListener} instance.
     *
     * @param plugin the SafeHorses plugin instance
     */
    public SafeHorsesListener(SafeHorsesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof AbstractHorse && plugin.getRegistry().isSafeHorse((AbstractHorse) event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof AbstractHorse) {
            SafeHorse horse = plugin.getRegistry().getSafeHorse((AbstractHorse) event.getRightClicked());
            if (horse != null) {
                if (horse.getOwner() != event.getPlayer()) {
                    if (event.getPlayer().hasPermission("safehorses.info")) {
                        plugin.message(event.getPlayer(), "This horse belongs to {{" + horse.getOwner().getName() + "}}.");
                    } else {
                        plugin.message(event.getPlayer(), "That horse isn't yours!");
                    }
                    event.setCancelled(true);
                } else if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.NAME_TAG) {
                    if (!event.getPlayer().hasPermission("safehorses.rename")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.getRegistry().hasSafeHorse(event.getPlayer())) {
            plugin.getRegistry().removeSafeHorse(event.getPlayer(), false);
        }
    }
    
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        for (Entity e : event.getChunk().getEntities()) {
            if (e instanceof AbstractHorse) {
                // Clean up "stray" horses that were created by the plugin
                // but somehow weren't removed
                AbstractHorse horse = (AbstractHorse) e;
                if (horse.getMaxHealth() == 1 && !plugin.getRegistry().isSafeHorse(horse)) {
                    horse.remove();
                }
            }
        }
    }
    
    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        for (Entity e : event.getChunk().getEntities()) {
            if (e instanceof AbstractHorse) {
                AbstractHorse horse = (AbstractHorse) e;
                if (plugin.getRegistry().isSafeHorse(horse)) {
                    plugin.getRegistry().removeSafeHorse((Player) horse.getOwner(), false);
                }
            }
        }
    }
    
}
