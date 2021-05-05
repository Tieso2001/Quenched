package net.tieso2001.quenched.util;

import com.google.gson.JsonElement;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.JSONUtils;
import net.tieso2001.quenched.Quenched;

public class JsonUtils {

    public static Ingredient deserializeIngredientWithNBT(JsonElement ingredientElement) {
        Ingredient ingredient = Ingredient.deserialize(ingredientElement);
        if (ingredientElement.isJsonArray()) {
            for (int i = 0; i < ingredientElement.getAsJsonArray().size(); i++) {
                JsonElement element = ingredientElement.getAsJsonArray().get(i);
                if (element.getAsJsonObject().has("nbt")) {
                    ItemStack stack = ingredient.getMatchingStacks()[i];
                    try {
                        addNBTtoStack(stack, element);
                    } catch (CommandSyntaxException commandsyntaxexception) {
                        Quenched.LOGGER.error("Failed to add nbt tag to ingredient {}:" + commandsyntaxexception.getMessage(), stack.getItem().getRegistryName());
                    }
                }
            }
        } else {
            if (ingredientElement.getAsJsonObject().has("nbt")) {
                for (ItemStack stack : ingredient.getMatchingStacks()) {
                    try {
                        addNBTtoStack(stack, ingredientElement);
                    } catch (CommandSyntaxException commandsyntaxexception) {
                        Quenched.LOGGER.error("Failed to add nbt tag to ingredient {}:" + commandsyntaxexception.getMessage(), stack.getItem().getRegistryName());
                    }
                }
            }
        }
        return ingredient;
    }

    public static ItemStack addNBTtoStack(ItemStack stack, JsonElement element) throws CommandSyntaxException {
        CompoundNBT compoundnbt = JsonToNBT.getTagFromJson(JSONUtils.getString(element.getAsJsonObject().get("nbt"), "nbt"));
        stack.setTag(compoundnbt);
        return stack;
    }

    public static JsonElement serializeIngredientWithNBT(Ingredient ingredient) {
        JsonElement ingredientElement = ingredient.serialize();
        if (ingredientElement.isJsonArray()) {
            for (int i = 0; i < ingredientElement.getAsJsonArray().size(); i++) {
                JsonElement element = ingredientElement.getAsJsonArray().get(i);
                ItemStack stack = ingredient.getMatchingStacks()[i];
                if (stack.getTag() != null) {
                    element.getAsJsonObject().addProperty("nbt", stack.getTag().getString());
                }
            }
        }
        return ingredientElement;
    }
}
