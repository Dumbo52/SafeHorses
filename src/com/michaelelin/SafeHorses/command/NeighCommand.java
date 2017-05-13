package com.michaelelin.SafeHorses.command;

import com.michaelelin.SafeHorses.HorseRegistry;
import com.michaelelin.SafeHorses.SafeHorse;
import com.michaelelin.SafeHorses.SafeHorsesPlugin;
import com.michaelelin.SafeHorses.exception.InsufficientPermissionsException;
import com.michaelelin.SafeHorses.exception.SafeHorsesException;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

/**
 * The <strong>/horse neigh</strong> command.
 */
public class NeighCommand extends SafeHorsesCommand {

    private Random random;

    public NeighCommand(SafeHorsesPlugin plugin) {
        super(plugin);
        random = new Random();
    }

    @Override
    public String execute(Player player, List<String> args) throws SafeHorsesException {
        if (!player.hasPermission("safehorses.neigh")) {
            throw new InsufficientPermissionsException();
        }

        HorseRegistry registry = plugin.getRegistry();

        if (registry.hasSafeHorse(player)) {
            SafeHorse horse = registry.getSafeHorse(player);
            List<Sound> sounds = plugin.getConfiguration().getSounds(horse.getVariant());
            if (sounds != null && !sounds.isEmpty()) {
                Sound sound = sounds.get(random.nextInt(sounds.size()));
                horse.getInstance().getWorld()
                        .playSound(horse.getInstance().getLocation(), sound, 1, (float) Math.random() * 0.4F + 0.4F);
            }
        } else {
            return "You don't have a horse currently spawned. Try " + ChatColor.LIGHT_PURPLE + "/horse spawn" +
                    ChatColor.GREEN + ".";
        }
        return "";
    }

    @Override
    public String getName() {
        return "neigh";
    }

    @Override
    public String getDescription() {
        return "makes your horse neigh (or something to that effect)";
    }

    @Override
    public String getUsage(Player player) {
        return player.hasPermission("safehorses.neigh") ? "" : null;
    }

}
