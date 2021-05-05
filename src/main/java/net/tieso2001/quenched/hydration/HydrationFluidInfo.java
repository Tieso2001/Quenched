package net.tieso2001.quenched.hydration;

import com.google.gson.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Type;

public class HydrationFluidInfo {

    private final Fluid fluid;
    private final int hydration;
    private final float hydrationSaturation;

    public HydrationFluidInfo(Fluid fluid, int hydration, float hydrationSaturation) {
        this.fluid = fluid;
        this.hydration = hydration;
        this.hydrationSaturation = hydrationSaturation;
    }

    public Fluid getFluid() {
        return fluid;
    }
    public int getHydration() {
        return hydration;
    }

    public float getHydrationSaturation() {
        return hydrationSaturation;
    }

    public static class Serializer implements JsonDeserializer<HydrationFluidInfo>, JsonSerializer<HydrationFluidInfo> {

        @Override
        public HydrationFluidInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            ResourceLocation fluidResourceLocation = new ResourceLocation(JSONUtils.getString(object, "fluid"));
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidResourceLocation);
            int hydration = JSONUtils.getInt(object, "hydration", 0);
            float hydrationSaturation = JSONUtils.getFloat(object, "hydration_saturation", 0);
            return new HydrationFluidInfo(fluid, hydration, hydrationSaturation);
        }

        @Override
        public JsonElement serialize(HydrationFluidInfo src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("fluid", src.fluid.getRegistryName().toString());
            object.addProperty("hydration", src.hydration);
            object.addProperty("hydration_saturation", src.hydrationSaturation);
            return object;
        }
    }
}
