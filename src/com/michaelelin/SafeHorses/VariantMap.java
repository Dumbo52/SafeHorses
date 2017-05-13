package com.michaelelin.SafeHorses;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;

/**
 * Maps between horse variants and entity types.
 */
public class VariantMap {

    private static BiMap<Horse.Variant, EntityType> variants;

    static {
        ImmutableBiMap.Builder<Horse.Variant, EntityType> builder = new ImmutableBiMap.Builder<>();
        builder.put(Horse.Variant.DONKEY, EntityType.DONKEY);
        builder.put(Horse.Variant.HORSE, EntityType.HORSE);
        builder.put(Horse.Variant.LLAMA, EntityType.LLAMA);
        builder.put(Horse.Variant.MULE, EntityType.MULE);
        builder.put(Horse.Variant.SKELETON_HORSE, EntityType.SKELETON_HORSE);
        builder.put(Horse.Variant.UNDEAD_HORSE, EntityType.ZOMBIE_HORSE);
        variants = builder.build();
    }

    /**
     * Gets the entity type from a given horse variant.
     *
     * @param variant the horse variant
     * @return the corresponding entity type
     */
    public static EntityType getEntityType(Horse.Variant variant) {
        return variants.get(variant);
    }

    /**
     * Gets the horse variant from a given entity type.
     *
     * @param type the entity type
     * @return the horse variant
     */
    public static Horse.Variant getVariant(EntityType type) {
        return variants.inverse().get(type);
    }
}
