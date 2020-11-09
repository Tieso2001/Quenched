package net.tieso2001.quenched.hydration;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.registries.ForgeRegistries;
import net.tieso2001.quenched.Quenched;

import java.util.Map;

public class HydrationStatsManager extends JsonReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    private Map<ItemStack, HydrationStat> itemHydrationStats = ImmutableMap.of();
    private Map<Fluid, HydrationStat> fluidHydrationStats = ImmutableMap.of();

    public HydrationStatsManager() {
        super(GSON, "quenched");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        Map<ItemStack, HydrationStat> itemMap = Maps.newHashMap();
        Map<Fluid, HydrationStat> fluidMap = Maps.newHashMap();
        objectIn.forEach((resourceLocation, jsonElement) -> {
            try {
                HydrationStat stat = deserializeHydrationItem(jsonElement);
                if (stat == null) {
                    Quenched.LOGGER.warn(String.format("Failed to deserialize hydration stat \"%s\"! Skipping...", resourceLocation));
                    return;
                }
                if (stat.getType().isItemType()) {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(stat.getId()));
                    if (item == null) {
                        Quenched.LOGGER.warn(String.format("Could not find item corresponding to hydration stat \"%s\"! Skipping...", resourceLocation));
                        return;
                    }
                    ItemStack stack = new ItemStack(item);
                    if (stat.hasTag()) {
                        CompoundNBT tag = Util.make(new CompoundNBT(), (nbt) -> nbt.putString(stat.getTagName(), stat.getTagValue()));
                        stack.setTag(tag);
                    }
                    itemMap.put(stack, stat);
                }
                else if (stat.getType().isFluidType()) {
                    Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(stat.getId()));
                    if (fluid == null) {
                        Quenched.LOGGER.warn(String.format("Could not find fluid corresponding to hydration stat \"%s\"! Skipping...", resourceLocation));
                        return;
                    }
                    fluidMap.put(fluid, stat);
                }
            } catch (IllegalArgumentException | JsonParseException ignored) {
                Quenched.LOGGER.warn(String.format("Failed to parse \"%s\" as a hydration stat! Skipping...", resourceLocation));
            }
        });
        itemHydrationStats = itemMap;
        fluidHydrationStats = fluidMap;
    }

    private static HydrationStat deserializeHydrationItem(JsonElement jsonElement) {
        JsonObject json = (JsonObject) jsonElement;

        String typeId = JSONUtils.getString(json, "type");
        HydrationStatType type = getType(typeId);
        if (type == null) {
            return null;
        }

        String id = JSONUtils.getString(json, "id");

        String tagName = null;
        String tagValue = null;
        if (json.has("tag")) {
            JsonObject tagJson = JSONUtils.getJsonObject(json, "tag");
            tagName = JSONUtils.getString(tagJson, "tagname");
            tagValue = JSONUtils.getString(tagJson, "value");
        }

        int hydration = JSONUtils.getInt(json, "hydration");
        float hydrationSaturation = JSONUtils.getFloat(json, "hydrationSaturation");

        return new HydrationStat(type, id, tagName, tagValue, hydration, hydrationSaturation);
    }

    private static HydrationStatType getType(String type) {
        switch (type) {
            case (Quenched.MOD_ID + ":" + "hydration_stat_item"):
                return HydrationStatType.ITEM;
            case (Quenched.MOD_ID + ":" + "hydration_stat_fluid"):
                return HydrationStatType.FLUID;
            default:
                return null;
        }
    }

    public Map<ItemStack, HydrationStat> getAllItemHydrationStats() {
        return this.itemHydrationStats;
    }

    public Map<Fluid, HydrationStat> getAllFluidHydrationStats() {
        return this.fluidHydrationStats;
    }

    public boolean hasHydrationStat(ItemStack stack) {
        ItemStack key = getKey(stack);
        return key != null;
    }

    public HydrationStat getHydrationStat(ItemStack stack) {
        ItemStack key = getKey(stack);
        return getAllItemHydrationStats().get(key);
    }

    private ItemStack getKey(ItemStack stack) {
        for (ItemStack key : getAllItemHydrationStats().keySet()) {
            HydrationStat stat = getAllItemHydrationStats().get(key);
            if (key.isItemEqual(stack)) {
                if (key.hasTag() && stack.hasTag()) { // key has no tag but has capNBT
                    if (stack.getTag().getString(stat.getTagName()).equals(stat.getTagValue())) {
                        return key;
                    }
                } else if (!key.hasTag()) {
                    return key;
                }
            }
        }
        return null;
    }

    public boolean hasHydrationStat(Fluid fluid) {
        Fluid key = getKey(fluid);
        return key != null;
    }

    public HydrationStat getHydrationStat(Fluid fluid) {
        Fluid key = getKey(fluid);
        return getAllFluidHydrationStats().get(key);
    }

    private Fluid getKey(Fluid fluid) {
        for (Fluid key : getAllFluidHydrationStats().keySet()) {
            if (key.isEquivalentTo(fluid)) {
                return key;
            }
        }
        return null;
    }
}
