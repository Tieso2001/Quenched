package net.tieso2001.quenched.hydration;

import com.google.gson.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;
import net.tieso2001.quenched.util.JsonUtils;

import java.lang.reflect.Type;

public class HydrationItemInfo {

    private final Ingredient ingredient;
    private final int hydration;
    private final float hydrationSaturation;

    public HydrationItemInfo(Ingredient ingredient, int hydration, float hydrationSaturation) {
        this.ingredient = ingredient;
        this.hydration = hydration;
        this.hydrationSaturation = hydrationSaturation;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getHydration() {
        return hydration;
    }

    public float getHydrationSaturation() {
        return hydrationSaturation;
    }

    public static class Serializer implements JsonDeserializer<HydrationItemInfo>, JsonSerializer<HydrationItemInfo> {

        @Override
        public HydrationItemInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            JsonElement ingredientElement = (JSONUtils.isJsonArray(object, "ingredient") ? JSONUtils.getJsonArray(object, "ingredient") : JSONUtils.getJsonObject(object, "ingredient"));
            Ingredient ingredient = JsonUtils.deserializeIngredientWithNBT(ingredientElement);
            int hydration = JSONUtils.getInt(object, "hydration", 0);
            float hydrationSaturation = JSONUtils.getFloat(object, "hydration_saturation", 0);
            return new HydrationItemInfo(ingredient, hydration, hydrationSaturation);
        }

        @Override
        public JsonElement serialize(HydrationItemInfo src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.add("ingredient", src.ingredient.serialize());
            object.addProperty("hydration", src.hydration);
            object.addProperty("hydration_saturation", src.hydrationSaturation);
            return object;
        }
    }
}
