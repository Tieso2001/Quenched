package net.tieso2001.quenched.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class Config {

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue enableDehydratedEffects;
    public static ForgeConfigSpec.ConfigValue<List<String>> dehydratedEffectsList;
    public static ForgeConfigSpec.BooleanValue enableDirectFluidDrinking;

    static {
        ForgeConfigSpec.Builder builder = COMMON_BUILDER;

        enableDehydratedEffects = builder
                .comment("Apply (negative) potion effects when dehydrated, default = true")
                .define("enable_dehydrated_effects", true);
        dehydratedEffectsList = builder
                .comment("List of potion effects which are applied when dehydrated, default = [\"minecraft:slowness\", \"minecraft:weakness\", \"minecraft:mining_fatigue\"]")
                .define("dehydrated_effects_list", Arrays.asList("minecraft:slowness", "minecraft:weakness", "minecraft:mining_fatigue"));
        enableDirectFluidDrinking = builder
                .comment("Allows players to drink from fluid sources with shift + rightclick, default = true")
                .define("enable_direct_fluid_drinking", true);

        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
