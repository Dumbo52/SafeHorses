package com.michaelelin.SafeHorses.command;

import com.michaelelin.SafeHorses.HorseRegistry;
import com.michaelelin.SafeHorses.SafeHorse;
import com.michaelelin.SafeHorses.SafeHorsesPlugin;
import com.michaelelin.SafeHorses.exception.InsufficientPermissionsException;
import com.michaelelin.SafeHorses.exception.SafeHorsesException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The <strong>/horse color</strong> command.
 */
public class ColorCommand extends SafeHorsesCommand {

    private static final String horseColorsString = "{{" + StringUtils.join(Horse.Color.values(), "}}, {{") + "}}";
    private static final String llamaColorsString = "{{" + StringUtils.join(Llama.Color.values(), "}}, {{") + "}}";

    public ColorCommand(SafeHorsesPlugin plugin) {
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

            if (horse.getColor() == null) {
                return "Color is not supported for horses with variant {{" + horse.getVariant() + "}}.";
            }

            if (args.size() == 0) {
                return "Your current horse's color is {{" + horse.getColor() +
                        "}}.\nTo change your horse's color, use {{/horse color <color>}}. Valid colors: " +
                        (horse.getVariant() == Horse.Variant.HORSE ? horseColorsString : llamaColorsString) + ".";
            } else {
                try {
                    if (horse.getVariant() == Horse.Variant.HORSE) {
                        horse.setColor(Horse.Color.valueOf(args.get(0).toUpperCase()));
                    } else if (horse.getVariant() == Horse.Variant.LLAMA) {
                        horse.setColor(Llama.Color.valueOf(args.get(0).toUpperCase()));
                    }
                    return "Horse color set to {{" + horse.getColor() + "}}.";
                } catch (IllegalArgumentException e) {
                    return "Invalid horse color. Valid color: " + (horse.getVariant() == Horse.Variant.HORSE ? horseColorsString : llamaColorsString) + ".";
                }
            }
        } else {
            return "You don't have a horse currently spawned. Try {{/horse spawn}}.";
        }
    }

    @Override
    public String getName() {
        return "color";
    }

    @Override
    public String getDescription() {
        return "sets your horse to the specified color";
    }

    @Override
    public String getUsage(Player player) {
        return player.hasPermission("safehorses.appearance") ? "<color>" : null;
    }
}
