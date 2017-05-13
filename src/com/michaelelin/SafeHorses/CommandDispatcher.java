package com.michaelelin.SafeHorses;

import com.michaelelin.SafeHorses.command.*;
import com.michaelelin.SafeHorses.exception.SafeHorsesException;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dispatches commands to be executed by their respective {@code SafeHorsesCommand}s.
 */
public class CommandDispatcher {

    private SafeHorsesPlugin plugin;
    private SafeHorsesCommand[] commands;
    private Map<String, SafeHorsesCommand> commandMap;

    /**
     * Creates a new {@code CommandDispatcher} for the plugin instance.
     *
     * @param plugin the SafeHorses plugin
     */
    public CommandDispatcher(SafeHorsesPlugin plugin) {
        this.plugin = plugin;
        commands = new SafeHorsesCommand[]{
                new SpawnCommand(plugin),
                new DespawnCommand(plugin),
                new ClearCommand(plugin),
                new VariantCommand(plugin),
                new ColorCommand(plugin),
                new StyleCommand(plugin),
                new NeighCommand(plugin),
                new ReloadCommand(plugin)
        };

        commandMap = new HashMap<>();
        for (SafeHorsesCommand command : commands) {
            commandMap.put(command.getName(), command);
            for (String alias : command.getAliases()) {
                commandMap.put(alias, command);
            }
        }
    }

    /**
     * Executes the {@code horse} command for the given player with the given list of arguments.
     *
     * @param player the player executing the command
     * @param args the arguments to the command
     */
    public void execute(Player player, List<String> args) {
        if (args.isEmpty()) {
            printHelp(player);
        } else {
            SafeHorsesCommand command = commandMap.get(args.get(0).toLowerCase());
            if (command == null) {
                printHelp(player);
            } else {
                try {
                    String msg = command.execute(player, args.subList(1, args.size()));
                    if (!msg.isEmpty()) {
                        plugin.message(player, msg);
                    }
                } catch (SafeHorsesException e) {
                    if (e.hasMessage()) {
                        plugin.message(player, e.getMessage());
                    }
                    if (e.isShowUsage()) {
                        plugin.message(player, "Usage: " + usageString(command, player));
                    }
                }
            }
        }
    }

    /**
     * Sends generic SafeHorses help text to the given player.
     *
     * @param player the player to send help to
     */
    private void printHelp(Player player) {
        plugin.message(player, "SafeHorses Commands");
        for (SafeHorsesCommand command : commands) {
            String usageArgs = command.getUsage(player);
            if (usageArgs == null) {
                continue;
            }

            StringBuilder str = new StringBuilder("{{/horse ");
            str.append(command.getName());

            if (!usageArgs.isEmpty()) {
                str.append(" ").append(usageArgs);
            }
            str.append("}} - ").append(command.getDescription());
            plugin.message(player, str.toString());
        }
    }

    /**
     * Returns the usage string for a command based on the given player's
     * permissions.
     *
     * @param command the command
     * @param player the player to check permissions for
     * @return the usage string
     */
    private String usageString(SafeHorsesCommand command, Player player) {
        StringBuilder usage = new StringBuilder("{{/horse ");
        usage.append(command.getName());

        String usageArgs = command.getUsage(player);
        if (!usageArgs.isEmpty()) {
            usage.append(" ").append(usageArgs);
        }
        usage.append("}}");
        return usage.toString();
    }


}
