package com.michaelelin.SafeHorses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

/**
 * A registry of {@code SafeHorse}s for online players.
 */
public class HorseRegistry {

    /**
     * Maps player UUIDs to {@code SafeHorse}s
     */
    private Map<UUID, SafeHorse> registry = new HashMap<UUID, SafeHorse>();

    /**
     * Maps horse entity UUIDs to {@code SafeHorse}s
     */
    private Map<UUID, SafeHorse> horses = new HashMap<UUID, SafeHorse>();

    private SafeHorsesPlugin plugin;

    /**
     * Creates a new {@code HorseRegistry}.
     *
     * @param plugin the SafeHorses plugin
     */
    public HorseRegistry(SafeHorsesPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets the {@code SafeHorse} instance from the given player, or
     * {@code null} if none exists.
     *
     * @param player the player
     * @return the {@code SafeHorse} instance
     */
    public SafeHorse getSafeHorse(Player player) {
        return registry.get(player.getUniqueId());
    }

    /**
     * Gets the {@code SafeHorse} instance from the given horse entity, or
     * {@code null} if none exists..
     *
     * @param instance the horse entity
     * @return the {@code SafeHorse} instance
     */
    public SafeHorse getSafeHorse(AbstractHorse instance) {
        return horses.get(instance.getUniqueId());
    }

    /**
     * Determines whether the given player currently has a horse spawned.
     *
     * @param player the player
     * @return {@code true} if the player has a horse
     */
    public boolean hasSafeHorse(Player player) {
        return registry.containsKey(player.getUniqueId());
    }

    /**
     * Determines whether the given horse entity is registered as a {@code SafeHorse}.
     *
     * @param instance the horse entity
     * @return {@code true} if the horse is registered
     */
    public boolean isSafeHorse(AbstractHorse instance) {
        return horses.containsKey(instance.getUniqueId());
    }

    /**
     * Registeres a horse entity to a player as a new {@code SafeHorse}.
     *
     * @param player the player
     * @param instance the horse entity
     * @return the {@code SafeHorse}
     */
    public SafeHorse registerSafeHorse(Player player, AbstractHorse instance) {
        SafeHorse horse = new SafeHorse(plugin, instance, player);
        registry.put(player.getUniqueId(), horse);
        horses.put(instance.getUniqueId(), horse);

        if (plugin.getConfiguration().getKeepState()) {
            List<SafeHorseBean> matches = plugin.getDatabase().find(SafeHorseBean.class).where()
                    .eq("owner", player.getUniqueId().toString()).query().findList();
            if (!matches.isEmpty()) {
                horse.applyBean(matches.get(0));
            }
        }

        return horse;
    }

    /**
     * Updates the registry to assign a new horse entity to a
     * {@code SafeHorse}.
     *
     * @param fromHorse the {@code SafeHorse} instance
     * @param toHorse the new horse entity
     */
    public void transferSafeHorse(SafeHorse fromHorse, AbstractHorse toHorse) {
        horses.remove(fromHorse.getInstance().getUniqueId());
        horses.put(toHorse.getUniqueId(), fromHorse);
    }

    /**
     * Removes a {@code SafeHorse} from the registry, optionally clearing its
     * data.
     *
     * @param player the player owning the horse to remove
     * @param clear whether to clear the horse's data
     * @return {@code true} if the horse was removed
     */
    public boolean removeSafeHorse(Player player, boolean clear) {
        return removeSafeHorse(player.getUniqueId(), clear);
    }

    /**
     * Removes a {@code SafeHorse} from the registry, optionally clearing its
     * data.
     *
     * @param player the UUID of the player owning the horse to remove
     * @param clear whether to clear the horse's data
     * @return {@code true} if the horse was removed
     */
    public boolean removeSafeHorse(UUID player, boolean clear) {
        SafeHorse horse = registry.remove(player);
        if (horse != null || clear) {
            if (plugin.getConfiguration().getKeepState()) {
                plugin.getDatabase().delete(plugin.getDatabase().find(SafeHorseBean.class).where()
                        .eq("owner", player.toString()).query().findList());
                if (!clear && horse != null) {
                    plugin.getDatabase().save(horse.toBean());
                }
            }
            if (horse != null) {
                horse.remove();
                horses.remove(horse.getInstance().getUniqueId());
            }
            return true;
        }
        return false;
    }

    /**
     * Removes all horses in the registry. Called when the server goes down.
     */
    public void removeAllHorses() {
        for (UUID p : registry.keySet()) {
            removeSafeHorse(p, false);
        }
    }

}
