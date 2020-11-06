package net.tieso2001.quenched.hydration;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.client.resources.JsonReloadListener;
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

    private Map<ItemStack, ItemHydrationStat> itemHydrationStats = ImmutableMap.of();

    public HydrationStatsManager() {
        super(GSON, "quenched");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        Map<ItemStack, ItemHydrationStat> map = Maps.newHashMap();
        objectIn.forEach((resourceLocation, jsonElement) -> {
            try {
                ItemHydrationStat stat = deserializeHydrationItem(jsonElement);
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(stat.getId()));
                if (item == null) {
                    Quenched.LOGGER.warn(String.format("Could not find item corresponding to hydration stat %s! Skipping...", resourceLocation));
                    return;
                }
                ItemStack stack = new ItemStack(item);
                if (stat.hasTag()) {
                    CompoundNBT tag = Util.make(new CompoundNBT(), (nbt) -> nbt.putString(stat.getTagName(), stat.getTagValue()));
                    stack.setTag(tag);
                }
                map.put(stack, stat);
            } catch (IllegalArgumentException | JsonParseException ignored) {
                Quenched.LOGGER.warn(String.format("Failed to parse %s as a hydration stat! Skipping...", resourceLocation));
            }
        });
        itemHydrationStats = map;
    }

    private static ItemHydrationStat deserializeHydrationItem(JsonElement jsonElement) {
        JsonObject json = (JsonObject) jsonElement;

        String id = JSONUtils.getString(json, "item");

        String tagName = null;
        String tagValue = null;
        if (json.has("tag")) {
            JsonObject tagJson = JSONUtils.getJsonObject(json, "tag");
            tagName = JSONUtils.getString(tagJson, "tagname");
            tagValue = JSONUtils.getString(tagJson, "value");
        }

        int hydration = JSONUtils.getInt(json, "hydration");
        float hydrationSaturation = JSONUtils.getFloat(json, "hydrationSaturation");

        return new ItemHydrationStat(id, tagName, tagValue, hydration, hydrationSaturation);
    }

    public Map<ItemStack, ItemHydrationStat> getAllItemHydrationStats() {
        return this.itemHydrationStats;
    }

    public boolean hasHydrationStat(ItemStack stack) {
        for (ItemStack key : getAllItemHydrationStats().keySet()) {
            ItemHydrationStat stat = getAllItemHydrationStats().get(key);
            if (key.isItemEqual(stack)) {
                if (key.hasTag() && stack.hasTag()) { // key has no tag but has capNBT
                    if (stack.getTag().getString(stat.getTagName()).equals(stat.getTagValue())) {
                        return true;
                    }
                } else if (!key.hasTag()) {
                    return true;
                }
            }
        }
        return false;
    }

    public ItemHydrationStat getItemHydrationStat(ItemStack stack) {
        for (ItemStack key : getAllItemHydrationStats().keySet()) {
            ItemHydrationStat stat = getAllItemHydrationStats().get(key);
            if (key.isItemEqual(stack)) {
                if (key.hasTag() && stack.hasTag()) {
                    if (stack.getTag().getString(stat.getTagName()).equals(stat.getTagValue())) {
                        return getAllItemHydrationStats().get(key);
                    }
                } else if (!key.hasTag()) {
                    return getAllItemHydrationStats().get(key);
                }
            }
        }
        return null;
    }
}
