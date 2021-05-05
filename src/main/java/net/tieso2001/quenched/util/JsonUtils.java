package net.tieso2001.quenched.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.JSONUtils;

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
                        throw new JsonSyntaxException("Invalid nbt tag: " + commandsyntaxexception.getMessage());
                    }
                }
            }
        } else {
            if (ingredientElement.getAsJsonObject().has("nbt")) {
                for (ItemStack stack : ingredient.getMatchingStacks()) {
                    try {
                        addNBTtoStack(stack, ingredientElement);
                    } catch (CommandSyntaxException commandsyntaxexception) {
                        throw new JsonSyntaxException("Invalid nbt tag: " + commandsyntaxexception.getMessage());
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
}
