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
            .registerTypeAdapter(HydrationFluidInfo.class, new HydrationFluidInfo.Serializer())
            .create();

    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation HYDRATION_ITEM_TYPE = new ResourceLocation(Quenched.MOD_ID, "hydration_item");
    private static final ResourceLocation HYDRATION_FLUID_TYPE = new ResourceLocation(Quenched.MOD_ID, "hydration_fluid");

    private List<HydrationItemInfo> hydrationItemInfoList = ImmutableList.of();
    private List<HydrationFluidInfo> hydrationFluidInfoList = ImmutableList.of();

    public HydrationInfoManager() {
        super(GSON, "hydration");
    }

    public List<HydrationItemInfo> getHydrationItemList() {
        return hydrationItemInfoList;
    }

    public List<HydrationFluidInfo> getHydrationFluidList() {
        return hydrationFluidInfoList;
    }

    public HydrationItemInfo getHydrationFromStack(ItemStack stack) {
        for (HydrationItemInfo hydrationItemInfo : hydrationItemInfoList) {
            if (StackUtils.testWithNBT(hydrationItemInfo.getIngredient(), stack)) {
                return hydrationItemInfo;
            }
        }
        return null;
    }

    public HydrationFluidInfo getHydrationFromFluid(Fluid fluid) {
        for (HydrationFluidInfo hydrationFluidInfo : hydrationFluidInfoList) {
            if (hydrationFluidInfo.getFluid().isEquivalentTo(fluid)) {
                return hydrationFluidInfo;
            }
        }
        return null;
    }

    public boolean hasHydration(ItemStack stack) {
        return getHydrationFromStack(stack) != null;
    }

    public boolean hasHydration(Fluid fluid) {
        return getHydrationFromFluid(fluid) != null;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        List<HydrationItemInfo> itemList = new ArrayList<>();
        List<HydrationFluidInfo> fluidList = new ArrayList<>();
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            try {
                JsonObject object = entry.getValue().getAsJsonObject();
                if (object.has("type")) {
                    if (object.get("type").getAsString().equals(HYDRATION_ITEM_TYPE.toString())) {
                        HydrationItemInfo hydrationItemInfo = GSON.fromJson(entry.getValue(), HydrationItemInfo.class);
                        itemList.add(hydrationItemInfo);
                    } else if (object.get("type").getAsString().equals(HYDRATION_FLUID_TYPE.toString())) {
                        HydrationFluidInfo hydrationFluidInfo = GSON.fromJson(entry.getValue(), HydrationFluidInfo.class);
                        fluidList.add(hydrationFluidInfo);
                    }
                }
            } catch (IllegalArgumentException | JsonParseException exception) {
                LOGGER.error("Parsing error loading hydration info conversion {}", resourceLocation, exception);
            }
        }
        hydrationItemInfoList = ImmutableList.copyOf(itemList);
        hydrationFluidInfoList = ImmutableList.copyOf(fluidList);
        LOGGER.info("Loaded {} hydration info data", hydrationItemInfoList.size() + hydrationFluidInfoList.size());
    }
}
