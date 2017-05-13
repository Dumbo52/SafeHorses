package com.michaelelin.SafeHorses.command;

import com.michaelelin.SafeHorses.HorseRegistry;
import com.michaelelin.SafeHorses.SafeHorsesPlugin;
import com.michaelelin.SafeHorses.exception.SafeHorsesException;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The <strong>/horse despawn</strong> command.
 */
public class DespawnCommand extends SafeHorsesCommand {

    public DespawnCommand(SafeHorsesPlugin plugin) {
        super(plugin);
    }

    @Override
    public String execute(Player player, List<String> args) throws SafeHorsesException {
        if (args.size() > 1) {
            throw new SafeHorsesException(null, true);
        }

        HorseRegistry registry = plugin.getRegistry();

        if (args.size() == 0) {
            if (registry.removeSafeHorse(player, false)) {
                return "Horse despawned.";
            } else {
                return "You don't have a horse to despawn.";
            }
        } else {
            if (!player.hasPermission("safehorses.other.despawn")) {
                throw new SafeHorsesException(null, true);
            }

            Player target = plugin.getServer().getPlayer(args.get(0));
            String tName = target == null ? args.get(0) : target.getName();

            if (target != null && registry.removeSafeHorse(target, false)) {
                return tName + "'s horse despawned.";
            } else {
                return tName + " doesn't have a horse to despawn.";
            }
        }
    }

    @Override
    public String getName() {
        return "despawn";
    }

    @Override
    public String getDescription() {
        return "saves and despawns your current horse";
    }

    @Override
    public String getUsage(Player player) {
        if (player.hasPermission("safehorses.other.despawn")) {
            return "[player]";
        } else if (player.hasPermission("safehorses.spawn")) {
            return "";
        } else {
            return null;
        }
    }

}
