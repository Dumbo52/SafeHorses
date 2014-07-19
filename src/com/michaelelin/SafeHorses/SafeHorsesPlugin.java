package com.michaelelin.SafeHorses;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SafeHorsesPlugin extends JavaPlugin {

    public boolean KEEP_STATE;
    public boolean LOCK_AGE;
    public boolean VISIBLE_NAMES;

    public HorseRegistry horseRegistry;

    public static final Logger log = Logger.getLogger("Minecraft");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (command.getName().equalsIgnoreCase("horse") || command.getName().equalsIgnoreCase("safehorses")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length > 0) {

                    if (args[0].equalsIgnoreCase("spawn")) {
                        if (player.hasPermission("safehorses.spawn")) {
                            if (horseRegistry.hasSafeHorse(player)) {
                                horseRegistry.getSafeHorse(player).teleport(player);
                                player.playSound(player.getLocation(), Sound.HORSE_GALLOP, 1, 1);
                            }
                            else {
                                horseRegistry.registerSafeHorse(player, player.getWorld().spawn(player.getLocation(), Horse.class));
                            }
                        }
                        else {
                            message(player, "You don't have permission to run that command.");
                        }
                        return true;
                    }

                    if (args[0].equalsIgnoreCase("despawn")) {
                        if (args.length > 1 && player.hasPermission("safehorses.other.despawn")) {
                            Player target = getServer().getPlayer(args[1]);
                            String tName = target == null ? args[1] : target.getName();
                            if (target != null && horseRegistry.removeSafeHorse(target, false)) {
                                message(player, tName + "'s horse despawned.");
                            }
                            else {
                                message(player, tName + " doesn't have a horse to despawn.");
                            }
                        }
                        else {
                            if (horseRegistry.removeSafeHorse(player, false)) {
                                message(player, "Horse despawned.");
                            }
                            else {
                                message(player, "You don't have a horse to despawn.");
                            }
                        }
                        return true;
                    }

                    if (args[0].equalsIgnoreCase("clear")) {
                        if (args.length > 1 && player.hasPermission("safehorses.other.clear")) {
                            Player target = getServer().getPlayer(args[1]);
                            String tName = target == null ? args[1] : target.getName();
                            if (target == null) {
                                getDatabase().delete(getDatabase().find(SafeHorseBean.class).where().eq("owner", tName).query().findList());
                            }
                            else {
                                horseRegistry.removeSafeHorse(target, true);
                            }
                            message(player, tName + "'s horse data cleared.");
                        }
                        else {
                            horseRegistry.removeSafeHorse(player, true);
                            message(player, "Horse data cleared.");
                        }
                        return true;
                    }

                    if (args[0].equalsIgnoreCase("call") || args[0].equalsIgnoreCase("tp")) {
                        if (args.length > 1 && player.hasPermission("safehorses.other.call")) {
                            Player target = getServer().getPlayer(args[1]);
                            String tName = target == null ? args[1] : target.getName();
                            if (target != null && horseRegistry.hasSafeHorse(target)) {
                                horseRegistry.getSafeHorse(target).teleport(player);
                            }
                            else {
                                message(player, tName + " does not have a horse currently spawned.");
                            }
                        }
                        else {
                            if (horseRegistry.hasSafeHorse(player)) {
                                horseRegistry.getSafeHorse(player).teleport(player);
                                player.playSound(player.getLocation(), Sound.HORSE_GALLOP, 1, 1);
                            }
                            else {
                                horseRegistry.registerSafeHorse(player, player.getWorld().spawn(player.getLocation(), Horse.class));
                            }
                        }
                        return true;
                    }
                    
                    if (args[0].equalsIgnoreCase("neigh")) {
                        if (player.hasPermission("safehorses.neigh")) {
                            if (horseRegistry.hasSafeHorse(player)) {
                                Horse horse = horseRegistry.getSafeHorse(player);
                                Sound sound;
                                switch (horse.getVariant()) {
                                case DONKEY:
                                    sound = Sound.DONKEY_DEATH;
                                    break;
                                case UNDEAD_HORSE:
                                    sound = Sound.HORSE_ZOMBIE_DEATH;
                                    break;
                                case SKELETON_HORSE:
                                    sound = Sound.HORSE_SKELETON_DEATH;
                                    break;
                                default:
                                    sound = Sound.HORSE_DEATH;
                                    break;
                                }
                                horse.getWorld().playSound(horse.getLocation(), sound, 1, (float) Math.random() * 0.4F + 0.4F);
                            }
                            else {
                                message(player, "You don't have a horse currently spawned. Try " + ChatColor.LIGHT_PURPLE + "/horse spawn" + ChatColor.GREEN + ".");
                            }
                        }
                        else {
                            message(player, "You don't have permission to run that command.");
                        }
                        return true;
                    }

                    if (args[0].equalsIgnoreCase("variant")) {
                        if (player.hasPermission("safehorses.appearance")) {
                            if (horseRegistry.hasSafeHorse(player)) {
                                if (args.length >= 2) {
                                    try {
                                        horseRegistry.getSafeHorse(player).setVariant(Variant.valueOf(args[1].toUpperCase()));
                                    } catch (IllegalArgumentException e) {
                                        message(player, "Invalid horse variant. Valid variants: " + ChatColor.LIGHT_PURPLE + StringUtils.join(Variant.values(), ChatColor.GREEN + ", " + ChatColor.LIGHT_PURPLE));
                                    }
                                }
                                else {
                                    message(player, "Your current horse's variant is " + ChatColor.LIGHT_PURPLE + horseRegistry.getSafeHorse(player).getVariant() + ChatColor.GREEN + ".");
                                    message(player, "To change your horse's variant, use " + ChatColor.LIGHT_PURPLE + "/horse variant <variant>" + ChatColor.GREEN + ".");
                                }
                            }
                            else {
                                message(player, "You don't have a horse currently spawned. Try " + ChatColor.LIGHT_PURPLE + "/horse spawn" + ChatColor.GREEN + ".");
                            }
                        }
                        else {
                            message(player, "You don't have permission to run that command.");
                        }
                        return true;
                    }
                    
                    if (args[0].equalsIgnoreCase("color")) {
                        if (player.hasPermission("safehorses.appearance")) {
                            if (horseRegistry.hasSafeHorse(player)) {
                                if (args.length >= 2) {
                                    try {
                                        horseRegistry.getSafeHorse(player).setColor(Color.valueOf(args[1].toUpperCase()));
                                    } catch (IllegalArgumentException e) {
                                        message(player, "Invalid horse color. Valid colors: " + ChatColor.LIGHT_PURPLE + StringUtils.join(Color.values(), ChatColor.GREEN + ", " + ChatColor.LIGHT_PURPLE));
                                    }
                                }
                                else {
                                    message(player, "Your current horse's color is " + ChatColor.LIGHT_PURPLE + horseRegistry.getSafeHorse(player).getColor() + ChatColor.GREEN + ".");
                                    message(player, "To change your horse's color, use " + ChatColor.LIGHT_PURPLE + "/horse color <color>" + ChatColor.GREEN + ".");
                                }
                            }
                            else {
                                message(player, "You don't have a horse currently spawned. Try " + ChatColor.LIGHT_PURPLE + "/horse spawn" + ChatColor.GREEN + ".");
                            }
                        }
                        else {
                            message(player, "You don't have permission to run that command.");
                        }
                        return true;
                    }
                    
                    if (args[0].equalsIgnoreCase("style")) {
                        if (player.hasPermission("safehorses.appearance")) {
                            if (horseRegistry.hasSafeHorse(player)) {
                                if (args.length >= 2) {
                                    try {
                                        horseRegistry.getSafeHorse(player).setStyle(Style.valueOf(args[1].toUpperCase()));
                                    } catch (IllegalArgumentException e) {
                                        message(player, "Invalid horse style. Valid styles: " + ChatColor.LIGHT_PURPLE + StringUtils.join(Style.values(), ChatColor.GREEN + ", " + ChatColor.LIGHT_PURPLE));
                                    }
                                }
                                else {
                                    message(player, "Your current horse's style is " + ChatColor.LIGHT_PURPLE + horseRegistry.getSafeHorse(player).getStyle() + ChatColor.GREEN + ".");
                                    message(player, "To change your horse's style, use " + ChatColor.LIGHT_PURPLE + "/horse style <style>" + ChatColor.GREEN + ".");
                                }
                            }
                            else {
                                message(player, "You don't have a horse currently spawned. Try " + ChatColor.LIGHT_PURPLE + "/horse spawn" + ChatColor.GREEN + ".");
                            }
                        }
                        else {
                            message(player, "You don't have permission to run that command.");
                        }
                        return true;
                    }
                    
                    if (args[0].equalsIgnoreCase("help")) {
                        sender.sendMessage(ChatColor.GREEN.toString() + ChatColor.UNDERLINE.toString() + "SafeHorses Commands");
                        sender.sendMessage(ChatColor.LIGHT_PURPLE + "/horse spawn" + ChatColor.RESET + " - spawns your horse at your location.");
                        sender.sendMessage(ChatColor.LIGHT_PURPLE + "/horse despawn" + ChatColor.RESET + " - saves and despawns your current horse.");
                        sender.sendMessage(ChatColor.LIGHT_PURPLE + "/horse clear" + ChatColor.RESET + " - clears your horse's data.");
                        sender.sendMessage(ChatColor.LIGHT_PURPLE + "/horse call" + ChatColor.RESET + " - teleports your horse to your location.");
                        sender.sendMessage(ChatColor.LIGHT_PURPLE + "/horse neigh" + ChatColor.RESET + " - makes your horse neigh (or something to that effect).");
                        sender.sendMessage(ChatColor.LIGHT_PURPLE + "/horse variant [variant]" + ChatColor.RESET + " - sets your horse to the specified variant.");
                        sender.sendMessage(ChatColor.LIGHT_PURPLE + "/horse color [color]" + ChatColor.RESET + " - sets your horse to the specified color.");
                        sender.sendMessage(ChatColor.LIGHT_PURPLE + "/horse style [style]" + ChatColor.RESET + " - sets your horse to the specified style.");
                        return true;
                    }
                    
                }
                message(sender, "Unknown command. Type " + ChatColor.LIGHT_PURPLE + "/horse help" + ChatColor.GREEN + " for help.");
            }
            else {
                message(sender, "You must be a player to run this command.");
            }
            return true;
        }

        return false;
    }

    public void message(CommandSender sender, String msg) {
        sender.sendMessage(ChatColor.GREEN + "SafeHorses: " + msg);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        KEEP_STATE = getConfig().getBoolean("keep-state");
        LOCK_AGE = getConfig().getBoolean("lock-age");
        VISIBLE_NAMES = getConfig().getBoolean("visible-names");
        getServer().getPluginManager().registerEvents(new SafeHorsesListener(this), this);
        horseRegistry = new HorseRegistry(this);
        setupDatabase();
        log.info("[" + getDescription().getName() + "] " + getDescription().getVersion() + " enabled.");
    }

    @Override
    public void onDisable() {
        horseRegistry.removeAllHorses();
        log.info("[" + getDescription().getName() + "] " + getDescription().getVersion() + " disabled.");
    }

    public void setupDatabase() {
        try {
            getDatabase().find(SafeHorseBean.class).findRowCount();
        } catch (PersistenceException e) {
            log.info("Installing " + getDescription().getName() + " database.");
            installDDL();
        }
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(SafeHorseBean.class);
        return list;
    }

}
