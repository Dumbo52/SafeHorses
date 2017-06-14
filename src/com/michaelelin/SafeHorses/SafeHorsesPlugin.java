package com.michaelelin.SafeHorses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.PersistenceException;

import com.avaje.ebean.EbeanServer;
import nu.nerd.BukkitEbean.EbeanBuilder;
import nu.nerd.BukkitEbean.EbeanHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SafeHorsesPlugin extends JavaPlugin {

    private SafeHorsesConfiguration configuration;
    private HorseRegistry horseRegistry;
    private CommandDispatcher dispatcher;
    private EbeanServer db;

    private static final Pattern messagePattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
    private static final ChatColor primaryColor = ChatColor.GREEN;
    private static final ChatColor secondaryColor = ChatColor.LIGHT_PURPLE;
    private static final String highlightString = secondaryColor + "$1" + primaryColor;


    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (command.getName().equalsIgnoreCase("horse") || command.getName().equalsIgnoreCase("safehorses")) {
            if (sender instanceof Player) {
                dispatcher.execute((Player) sender, Arrays.asList(args));
            } else {
                message(sender, "You must be a player to run this command.");
            }
            return true;
        }

        return false;
    }

    public SafeHorsesConfiguration getConfiguration() {
        return configuration;
    }

    public void reloadConfiguration() {
        reloadConfig();
        configuration = new SafeHorsesConfiguration(this, getConfig());
    }

    public HorseRegistry getRegistry() {
        return horseRegistry;
    }

    public void message(CommandSender sender, String msg) {
        sender.sendMessage(primaryColor + messagePattern.matcher(msg).replaceAll(highlightString));
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new SafeHorsesListener(this), this);
        configuration = new SafeHorsesConfiguration(this, getConfig());
        horseRegistry = new HorseRegistry(this);
        dispatcher = new CommandDispatcher(this);

        setupDatabase();
        getLogger().info(getDescription().getName() + " " + getDescription().getVersion() + " enabled.");
    }

    @Override
    public void onDisable() {
        horseRegistry.removeAllHorses();
        getLogger().info(getDescription().getName() + " " + getDescription().getVersion() + " disabled.");
    }

    public EbeanServer getDatabase() {
        return db;
    }

    private void setupDatabase() {
        db = new EbeanBuilder(this).setClasses(getDatabaseClasses()).build();
        try {
            db.find(SafeHorseBean.class).findRowCount();
        } catch (PersistenceException e) {
            getLogger().info("Installing " + getDescription().getName() + " database.");

            EbeanHelper.installDDL(db);
        }
    }

    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(SafeHorseBean.class);
        return list;
    }

}
