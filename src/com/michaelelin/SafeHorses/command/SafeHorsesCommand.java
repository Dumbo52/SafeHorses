package com.michaelelin.SafeHorses.command;

import com.michaelelin.SafeHorses.SafeHorsesPlugin;
import com.michaelelin.SafeHorses.exception.SafeHorsesException;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * A command for the SafeHorses plugin.
 */
public abstract class SafeHorsesCommand {

    /**
     * The plugin instance
     */
    protected SafeHorsesPlugin plugin;

    public SafeHorsesCommand(SafeHorsesPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Executes the command by the given player with a list of arguments.
     *
     * @param player the player running the command
     * @param args the arguments to the command
     * @return the message to send to the player upon completion of the command execution
     * @throws SafeHorsesException if the command execution encounters an error
     */
    public abstract String execute(Player player, List<String> args) throws SafeHorsesException;

    /**
     * Returns the name of the command.
     *
     * @return the command's name
     */
    public abstract String getName();

    /**
     * Returns a list of strings that are aliases of this command.
     *
     * @return the command's aliases
     */
    public String[] getAliases() {
        return new String[]{};
    }

    /**
     * Returns a description of the command.
     *
     * @return the command's description
     */
    public abstract String getDescription();

    /**
     * Returns a string containing the arguments of the command available to the given player, or {@code null} if
     * the player does not have access to the command.
     *
     * @param player the player
     * @return the usage string following the command name
     */
    public String getUsage(Player player) {
        return "";
    }

}
