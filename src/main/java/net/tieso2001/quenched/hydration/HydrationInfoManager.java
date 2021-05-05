package net.tieso2001.quenched.hydration;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.tieso2001.quenched.Quenched;
import net.tieso2001.quenched.util.StackUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HydrationInfoManager extends JsonReloadListener {

    private static final Gson GSON = (new GsonBuilder())
            .registerTypeAdapter(HydrationItemInfo.class, new HydrationItemInfo.Serializer())
            .create();

    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation HYDRATION_ITEM_TYPE = new ResourceLocation(Quenched.MOD_ID, "hydration_item");

    private List<HydrationItemInfo> hydrationItemInfoList = ImmutableList.of();

    public HydrationInfoManager() {
        super(GSON, "hydration");
    }

    public List<HydrationItemInfo> getHydrationItemList() {
        return hydrationItemInfoList;
    }

    public HydrationItemInfo getHydration(ItemStack stack) {
        for (HydrationItemInfo hydrationItemInfo : hydrationItemInfoList) {
            if (StackUtils.testWithNBT(hydrationItemInfo.getIngredient(), stack)) {
                return hydrationItemInfo;
            }
        }
        return null;
    }

    public boolean hasHydration(ItemStack stack) {
        return getHydration(stack) != null;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        List<HydrationItemInfo> itemList = new ArrayList<>();
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            try {
                JsonObject object = entry.getValue().getAsJsonObject();
                if (object.has("type")) {
                    if (object.get("type").getAsString().equals(HYDRATION_ITEM_TYPE.toString())) {
                        HydrationItemInfo hydrationItemInfo = GSON.fromJson(entry.getValue(), HydrationItemInfo.class);
                        itemList.add(hydrationItemInfo);
                    }
                }
            } catch (IllegalArgumentException | JsonParseException exception) {
                LOGGER.error("Parsing error loading hydration info {}", resourceLocation, exception);
            }
        }
        hydrationItemInfoList = ImmutableList.copyOf(itemList);
        LOGGER.info("Loaded {} hydration info data", hydrationItemInfoList.size());
    }
}
