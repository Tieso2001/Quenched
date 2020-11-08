package net.tieso2001.quenched.init;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

public class ModConfig {

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {
        public final ForgeConfigSpec.BooleanValue enableDehydratedEffects;
        public final ForgeConfigSpec.ConfigValue<List<String>> dehydratedEffectsList;

        public Common(ForgeConfigSpec.Builder builder) {
            enableDehydratedEffects = builder
                    .comment("Apply (negative) potion effects when dehydrated, default = true")
                    .define("enable_dehydrated_effects", true);
            dehydratedEffectsList = builder
                    .comment("List of potion effects which are applied when dehydrated, default = [\"minecraft:slowness\", \"minecraft:weakness\", \"minecraft:mining_fatigue\"]")
                    .define("dehydrated_effects_list", Arrays.asList("minecraft:slowness", "minecraft:weakness", "minecraft:mining_fatigue"));
        }
    }
}
