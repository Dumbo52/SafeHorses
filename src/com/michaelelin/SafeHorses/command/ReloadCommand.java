package com.michaelelin.SafeHorses.command;

import com.michaelelin.SafeHorses.SafeHorsesPlugin;
import com.michaelelin.SafeHorses.exception.InsufficientPermissionsException;
import com.michaelelin.SafeHorses.exception.SafeHorsesException;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The <strong>/horse reload</strong> command.
 */
public class ReloadCommand extends SafeHorsesCommand {

    public ReloadCommand(SafeHorsesPlugin plugin) {
        super(plugin);
    }

    @Override
    public String execute(Player player, List<String> args) throws SafeHorsesException {
        if (!player.hasPermission("safehorses.reload")) {
            throw new InsufficientPermissionsException();
        }

        plugin.reloadConfiguration();
        return "Configuration reloaded.";
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "reloads the SafeHorses configuration";
    }

    @Override
    public String getUsage(Player player) {
        if (player.hasPermission("safehorses.reload")) {
            return "";
        } else {
            return null;
        }
    }

}
