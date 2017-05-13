package com.michaelelin.SafeHorses.command;

import com.michaelelin.SafeHorses.HorseRegistry;
import com.michaelelin.SafeHorses.SafeHorsesPlugin;
import com.michaelelin.SafeHorses.exception.InsufficientPermissionsException;
import com.michaelelin.SafeHorses.exception.SafeHorsesException;
import org.bukkit.Sound;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The <strong>/horse spawn</strong> command.
 */
public class SpawnCommand extends SafeHorsesCommand {

    public SpawnCommand(SafeHorsesPlugin plugin) {
        super(plugin);
    }

    @Override
    public String execute(Player player, List<String> args) throws SafeHorsesException {
        if (!player.hasPermission("safehorses.spawn")) {
            throw new InsufficientPermissionsException();
        }

        if (args.size() > 1 || (args.size() == 1 && !player.hasPermission("safehorses.other.spawn"))) {
            throw new SafeHorsesException(null, true);
        }


        Player target = player;
        if (args.size() > 0) {
            target = plugin.getServer().getPlayer(args.get(0));
        }

        if (target == null) {
            throw new SafeHorsesException("That player isn't online.", true);
        }

        HorseRegistry registry = plugin.getRegistry();

        if (registry.hasSafeHorse(target)) {
            registry.getSafeHorse(target).getInstance().teleport(player);
            player.playSound(player.getLocation(), Sound.ENTITY_HORSE_GALLOP, 1, 1);
        } else {
            registry.registerSafeHorse(target, player.getWorld().spawn(player.getLocation(), Horse.class));
        }

        return "Horse spawned.";
    }

    @Override
    public String getName() {
        return "spawn";
    }

    @Override
    public String[] getAliases() {
        return new String[]{ "call" };
    }

    @Override
    public String getDescription() {
        return "spawns your horse at your location";
    }

    @Override
    public String getUsage(Player player) {
        if (player.hasPermission("safehorses.other.spawn")) {
            return "[player]";
        } else if (player.hasPermission("safehorses.spawn")) {
            return "";
        } else {
            return null;
        }
    }

}
