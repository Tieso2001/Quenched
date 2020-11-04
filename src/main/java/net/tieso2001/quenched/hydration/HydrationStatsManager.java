package net.tieso2001.quenched.hydration;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class HydrationStatsManager extends JsonReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private Map<ResourceLocation, HydrationItem> hydrationItems = ImmutableMap.of();

    public HydrationStatsManager() {
        super(GSON, "quenched");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        Map<ResourceLocation, HydrationItem> map = Maps.newHashMap();
        objectIn.forEach((resourceLocation, jsonElement) -> {
            try {
                HydrationItem hydrationItem = read(jsonElement);
                if (hydrationItem == null) {
                    return;
                }
                map.put(resourceLocation, hydrationItem);
            } catch (IllegalArgumentException | JsonParseException ignored) {
            }
        });
        hydrationItems = map;
    }

    private HydrationItem read(JsonElement jsonElement) {
        String json = jsonElement.toString();
        return new Gson().fromJson(json, HydrationItem.class);
    }

    public Map<ResourceLocation, HydrationItem> getAllHydrationStats() {
        return this.hydrationItems;
    }
}
