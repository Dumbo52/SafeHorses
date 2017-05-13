package com.michaelelin.SafeHorses.command;

import com.avaje.ebean.EbeanServer;
import com.michaelelin.SafeHorses.HorseRegistry;
import com.michaelelin.SafeHorses.SafeHorseBean;
import com.michaelelin.SafeHorses.SafeHorsesPlugin;
import com.michaelelin.SafeHorses.exception.SafeHorsesException;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The <strong>horse clear</strong> command.
 */
public class ClearCommand extends SafeHorsesCommand {

    public ClearCommand(SafeHorsesPlugin plugin) {
        super(plugin);
    }

    @Override
    public String execute(Player player, List<String> args) throws SafeHorsesException {
        if (args.size() > 1) {
            throw new SafeHorsesException(null, true);
        }

        HorseRegistry registry = plugin.getRegistry();

        if (args.size() == 0) {
            registry.removeSafeHorse(player, true);
            return "Horse data cleared.";
        } else {
            if (!player.hasPermission("safehorses.other.clear")) {
                throw new SafeHorsesException(null, true);
            }

            EbeanServer database = plugin.getDatabase();

            Player target = plugin.getServer().getPlayer(args.get(0));

            String tName = target == null ? args.get(0) : target.getName();
            if (target == null) {
                database.delete(database.find(SafeHorseBean.class).where().eq("owner", tName).findFutureIds());
            } else {
                registry.removeSafeHorse(target, true);
            }
            return tName + "'s horse data cleared.";
        }
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "clears your horse's data";
    }

    @Override
    public String getUsage(Player player) {
        if (player.hasPermission("safehorses.other.clear")) {
            return "[player]";
        } else if (player.hasPermission("safehorses.spawn")) {
            return "";
        } else {
            return null;
        }
    }
}
