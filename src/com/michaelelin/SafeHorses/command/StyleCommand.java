package com.michaelelin.SafeHorses.command;

import com.michaelelin.SafeHorses.HorseRegistry;
import com.michaelelin.SafeHorses.SafeHorse;
import com.michaelelin.SafeHorses.SafeHorsesPlugin;
import com.michaelelin.SafeHorses.exception.InsufficientPermissionsException;
import com.michaelelin.SafeHorses.exception.SafeHorsesException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The <strong>/horse style</strong> command.
 */
public class StyleCommand extends SafeHorsesCommand {

    private String stylesString = "{{" + StringUtils.join(Horse.Style.values(), "}}, {{") + "}}";

    public StyleCommand(SafeHorsesPlugin plugin) {
        super(plugin);
    }

    @Override
    public String execute(Player player, List<String> args) throws SafeHorsesException {
        if (!player.hasPermission("safehorses.appearance")) {
            throw new InsufficientPermissionsException();
        }

        if (args.size() > 1) {
            throw new SafeHorsesException(null, true);
        }

        HorseRegistry registry = plugin.getRegistry();

        if (registry.hasSafeHorse(player)) {
            SafeHorse horse = registry.getSafeHorse(player);

            if (horse.getStyle() == null) {
                return "Style is not supported for horses with variant {{" + horse.getVariant() + "}}.";
            }

            if (args.size() == 1) {
                try {
                    horse.setStyle(Horse.Style.valueOf(args.get(0).toUpperCase()));
                    return "Horse style set to {{" + horse.getStyle() + "}}.";
                } catch (IllegalArgumentException e) {
                    return "Invalid horse style. Valid styles: " + stylesString + ".";
                }
            } else {
                return "Your current horse's style is {{" + horse.getStyle() +
                        "}}.\nTo change your horse's style, use {{/horse style <style>}}. Valid styles: " +
                        stylesString + ".";
            }
        } else {
            return "You don't have a horse currently spawned. Try {{/horse spawn}}.";
        }
    }

    @Override
    public String getName() {
        return "style";
    }

    @Override
    public String getDescription() {
        return "sets your horse to the specified style";
    }

    @Override
    public String getUsage(Player player) {
        return player.hasPermission("safehorses.appearance") ? "<style>" : null;
    }
}
