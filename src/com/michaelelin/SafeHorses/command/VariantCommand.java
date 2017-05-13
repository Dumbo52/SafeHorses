package com.michaelelin.SafeHorses.command;

import com.michaelelin.SafeHorses.HorseRegistry;
import com.michaelelin.SafeHorses.SafeHorsesPlugin;
import com.michaelelin.SafeHorses.exception.InsufficientPermissionsException;
import com.michaelelin.SafeHorses.exception.SafeHorsesException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The <strong>/horse variant</strong> command.
 */
public class VariantCommand extends SafeHorsesCommand {

    private static final String variantsString = "{{" + StringUtils.join(Horse.Variant.values(), "}}, {{") + "}}";

    public VariantCommand(SafeHorsesPlugin plugin) {
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
            if (args.size() == 1) {
                try {
                    registry.getSafeHorse(player)
                            .setVariant(Horse.Variant.valueOf(args.get(0).toUpperCase()));
                    return "Horse variant set to {{" + args.get(0).toUpperCase() + "}}.";
                } catch (IllegalArgumentException e) {
                    return "Invalid horse variant. Valid variants: " + variantsString + ".";
                }
            } else {
                return "Your current horse's variant is {{" + registry.getSafeHorse(player).getVariant() +
                        "}}.\nTo change your horse's variant, use {{/horse variant <variant>}}. Valid variants: " +
                        variantsString + ".";
            }
        } else {
            return "You don't have a horse currently spawned. Try {{/horse spawn}}.";
        }
    }

    @Override
    public String getName() {
        return "variant";
    }

    @Override
    public String getDescription() {
        return "sets your horse to the specified variant";
    }

    @Override
    public String getUsage(Player player) {
        return player.hasPermission("safehorses.appearance") ? "<variant>" : null;
    }

}
