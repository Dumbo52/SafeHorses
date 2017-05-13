package com.michaelelin.SafeHorses;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.v1_11_R1.GenericAttributes;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A facade for horses registered by the SafeHorses plugin. Does useful stuff.
 */
public class SafeHorse {

    private SafeHorsesPlugin plugin;
    private AbstractHorse instance;
    private Player owner;

    /**
     * Creates a new {@code SafeHorse} instance.
     *
     * @param plugin the SafeHorses plugin
     * @param instance the horse entity
     * @param owner the player owning the horse
     */
    public SafeHorse(SafeHorsesPlugin plugin, AbstractHorse instance, Player owner) {
        SafeHorse.registerHorse(plugin, instance, owner);
        this.plugin = plugin;
        this.instance = instance;
        this.owner = owner;
    }

    /**
     * Gets the horse entity instance.
     *
     * @return the horse entity
     */
    public AbstractHorse getInstance() {
        return instance;
    }

    /**
     * Removes the horse entity from the game.
     */
    public void remove() {
        instance.remove();
    }

    /**
     * Gets the horse's name.
     *
     * @return the horse's name
     */
    public String getName() {
        return instance.getCustomName();
    }

    /**
     * Sets the horse's name.
     *
     * @param name the horse's new name
     */
    public void setName(String name) {
        instance.setCustomName(name);
    }

    /**
     * Gets the horse's variant.
     *
     * @return the horse's variant
     */
    public Horse.Variant getVariant() {
        return VariantMap.getVariant(instance.getType());
    }

    /**
     * Sets the horse's variant. Ever since the 1.11 update, this requires
     * spawning a new entity since different horse variants no longer have the
     * same entity type.
     *
     * @param variant the horse's new variant
     * @return {@code true} if the horse's variant was changed
     */
    public boolean setVariant(Horse.Variant variant) {
        if (variant != getVariant()) {
            Location location = instance.getLocation();

            String name = getName();
            int age = getAge();
            double speed = getSpeed();
            double jumpStrength = getJumpStrength();

            instance.remove();
            AbstractHorse newInstance = (AbstractHorse) location.getWorld().spawnEntity(location, VariantMap.getEntityType(variant));
            plugin.getRegistry().transferSafeHorse(this, newInstance);
            instance = newInstance;

            registerHorse(plugin, instance, owner);

            setName(name);
            setAge(age);
            setSpeed(speed);
            setJumpStrength(jumpStrength);

            return true;
        }
        return false;
    }

    /**
     * Gets the horse's color.
     *
     * @return the horse's color
     */
    public Enum getColor() {
        if (instance.getType() == EntityType.HORSE) {
            return ((Horse) instance).getColor();
        } else if (instance.getType() == EntityType.LLAMA) {
            return ((Llama) instance).getColor();
        } else {
            return null;
        }
    }

    /**
     * Sets the horse's color.
     *
     * @param color the horse's new color
     * @return {@code true} if the color could be set for the horse's variant
     */
    public boolean setColor(Enum color) {
        return color != null && setColor(color.ordinal());
    }

    /**
     * Sets the horse's color.
     *
     * @param color the ordinal of the horse variant's corresponding color enum
     * @return {@code true} if the color could be set for the horse's variant
     */
    public boolean setColor(int color) {
        if (instance.getType() == EntityType.HORSE) {
            ((Horse) instance).setColor(Horse.Color.values()[color]);
            return true;
        } else if (instance.getType() == EntityType.LLAMA) {
            ((Llama) instance).setColor(Llama.Color.values()[color]);
            return true;
        }
        return false;
    }

    /**
     * Gets the horse's style.
     *
     * @return the horse's style
     */
    public Enum getStyle() {
        if (instance.getType() == EntityType.HORSE) {
            return ((Horse) instance).getStyle();
        } else {
            return null;
        }
    }

    /**
     * Sets the horse's style.
     *
     * @param style the horse's new style
     * @return {@code true} if the style could be set for the horse's variant
     */
    public boolean setStyle(Enum style) {
        return style != null && setStyle(style.ordinal());
    }

    /**
     * Sets the horse's style.
     *
     * @param style the ordinal of the horse variant's corresponding style enum
     * @return {@code true} if the style could be set for the horse's variant
     */
    public boolean setStyle(int style) {
        if (instance.getType() == EntityType.HORSE) {
            ((Horse) instance).setStyle(Horse.Style.values()[style]);
            return true;
        }
        return false;
    }

    /**
     * Gets the horse's inventory as an {@code ItemStack} array
     *
     * @return the horse's inventory
     */
    public ItemStack[] getInventory() {
        return instance.getInventory().getContents();
    }

    /**
     * Sets the horse's inventroy as an {@code ItemStack} array
     *
     * @param contents the horse's new inventory
     */
    public void setInventory(ItemStack[] contents) {
        instance.getInventory().setContents(contents);
    }

    /**
     * Gets the horse's age
     *
     * @return the horse's age
     */
    public int getAge() {
        return instance.getAge();
    }

    /**
     * Sets the horse's age
     *
     * @param age the horse's new age
     */
    public void setAge(int age) {
        instance.setAge(age);
    }

    /**
     * Gets the horse's speed
     *
     * @return the horse's speed
     */
    public double getSpeed() {
        return ((CraftAbstractHorse) instance).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).b();
    }

    /**
     * Sets the horse's speed
     *
     * @param speed the horse's new speed
     */
    public void setSpeed(double speed) {
        ((CraftAbstractHorse) instance).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED)
                .setValue(speed);
    }

    /**
     * Gets the horse's jump strength
     *
     * @return the horse's jump strength
     */
    public double getJumpStrength() {
        return instance.getJumpStrength();
    }

    /**
     * Sets the horse's jump strength
     *
     * @param jumpStrength the horse's new jump strength
     */
    public void setJumpStrength(double jumpStrength) {
        instance.setJumpStrength(jumpStrength);
    }

    /**
     * Creates a new {@code SafeHoresBean} instance for the horse.
     *
     * @return the {@code SafeHorseBean}
     */
    public SafeHorseBean toBean() {
        SafeHorseBean bean = new SafeHorseBean();
        bean.setOwner(instance.getOwner().getUniqueId().toString());
        bean.setName(getName());
        bean.setVariant(ordinalOrZero(getVariant()));
        bean.setColor(ordinalOrZero(getColor()));
        bean.setStyle(ordinalOrZero(getStyle()));
        bean.setInventory(serializeInventory(getInventory()));
        bean.setAge(getAge());
        bean.setSpeed((int) (getSpeed() * 10000));
        bean.setJump((int) (getJumpStrength() * 10000));
        return bean;
    }

    /**
     * Applies attributes to the horse from a {@code SafeHorseBean} instance.}
     *
     * @param bean the {@code SafeHorseBean}
     */
    public void applyBean(SafeHorseBean bean) {
        setName(bean.getName());
        setVariant(Horse.Variant.values()[bean.getVariant()]);
        setColor(bean.getColor());
        setStyle(bean.getStyle());
        setInventory(deserializeInventory(bean.getInventory()));
        setAge(bean.getAge());
        setSpeed(bean.getSpeed() * 0.0001);
        setJumpStrength(bean.getJump() * 0.0001);
    }

    /**
     * Returns the enum's ordinal, or 0 if it is {@code null}.
     *
     * @param e the enum
     * @return the ordinal
     */
    private static int ordinalOrZero(Enum e) {
        if (e == null) {
            return 0;
        } else {
            return e.ordinal();
        }
    }

    /**
     * Registers a horse and sets some default attributes based on the plugin's
     * configuration.
     *
     * @param plugin the SafeHorses plugin
     * @param instance the horse entity
     * @param player the player to register the horse to
     */
    private static void registerHorse(SafeHorsesPlugin plugin, AbstractHorse instance, Player player) {
        instance.setOwner(player);
        instance.setAgeLock(plugin.getConfiguration().getLockAge());
        instance.setCustomNameVisible(plugin.getConfiguration().getVisibleNames());
        instance.setBreed(false);
        instance.setCustomName(player.getName() + "'s Horse");
        instance.setMaxHealth(1);
    }

    /**
     * Serializes an inventory from an {@code ItemStack} array to a JSON
     * string.
     *
     * @param inventory the inventory
     * @return the serialized JSON string
     */
    private static String serializeInventory(ItemStack[] inventory) {
        Gson gson = new Gson();
        List<Map<String, Object>> serializable = new ArrayList<>();
        for (ItemStack stack : inventory) {
            serializable.add(stack == null ? null : stack.serialize());
        }
        return gson.toJson(serializable);
    }

    /**
     * Deserializes an inventory from a JSON string to an {@code ItemStack}
     * array.
     *
     * @param ser the serialized JSON string
     * @return the inventory
     */
    private static ItemStack[] deserializeInventory(String ser) {
        Gson gson = new Gson();
        Type invType = new TypeToken<List<Map<String, Object>>>() {}.getType();
        List<Map<String, Object>> deserialized  = gson.fromJson(ser, invType);
        ItemStack[] inventory = new ItemStack[deserialized.size()];
        for (int i = 0; i < inventory.length; i++) {
            Map<String, Object> stack = deserialized.get(i);
            inventory[i] = stack == null ? null : ItemStack.deserialize(deserialized.get(i));
        }
        return inventory;
    }

}
