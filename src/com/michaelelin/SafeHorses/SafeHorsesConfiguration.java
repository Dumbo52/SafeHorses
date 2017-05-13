package com.michaelelin.SafeHorses;

import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Horse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The configuration object for SafeHorses.
 */
public class SafeHorsesConfiguration {

    private SafeHorsesPlugin plugin;

    private boolean keepState;
    private boolean lockAge;
    private boolean visibleNames;
    private Map<Horse.Variant, List<Sound>> soundMap;

    /**
     * Creates a new {@code SafeHorsesConfiguration} instance for the plugin.
     *
     * @param plugin the SafeHorses plugin instance
     * @param configuration the plugin's serialized configuration
     */
    public SafeHorsesConfiguration(SafeHorsesPlugin plugin, Configuration configuration) {
        this.plugin = plugin;
        keepState = configuration.getBoolean("horses.keep-state", true);
        lockAge = configuration.getBoolean("horses.lock-age", true);
        visibleNames = configuration.getBoolean("horses.visible-names", true);

        soundMap = new HashMap<>();
        ConfigurationSection sounds = configuration.getConfigurationSection("horses.sounds");
        for (String key : sounds.getKeys(false)) {
            Horse.Variant variant;
            try {
                variant = Horse.Variant.valueOf(key);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Unknown variant " + key + " in horses.sounds");
                continue;
            }

            List<Sound> variantSounds = new ArrayList<Sound>();
            for (String sound : sounds.getStringList(key)) {
                try {
                    variantSounds.add(Sound.valueOf(sound));
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Unknown sound " + sound + " in horses.sounds." + key);
                }
            }
            soundMap.put(variant, variantSounds);
        }
    }

    /**
     * Returns whether horses should keep their attributes after despawning
     * them.
     *
     * @return {@code true} if horses should keep their state
     */
    public boolean getKeepState() {
        return keepState;
    }

    /**
     * Returns whether horses' ages should be locked rather than growing up.
     *
     * @return {@code true} if horses' ages should be locked
     */
    public boolean getLockAge() {
        return lockAge;
    }

    /**
     * Returns whether horses' names should be visible without hovering over
     * them.
     *
     * @return {@code true} if horses' names should be visible
     */
    public boolean getVisibleNames() {
        return visibleNames;
    }

    /**
     * Returns the possible neighing sounds for a given horse variant.
     *
     * @param variant the horse variant
     * @return a list of sounds the horse can make
     */
    public List<Sound> getSounds(Horse.Variant variant) {
        return soundMap.get(variant);
    }

}
