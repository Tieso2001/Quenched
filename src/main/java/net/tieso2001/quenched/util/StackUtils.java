package net.tieso2001.quenched.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class StackUtils {

    public static boolean testWithNBT(Ingredient ingredient, ItemStack stack) {
        if (stack == null) {
            return false;
        } else {
            if (ingredient.getMatchingStacks().length == 0) {
                return stack.isEmpty();
            } else {
                for(ItemStack itemstack : ingredient.getMatchingStacks()) {
                    if (itemstack.getItem() == stack.getItem() && tagMatches(itemstack, stack)) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    public static boolean tagMatches(ItemStack stack1, ItemStack stack2) {
        for (Object tagKeyObject : stack1.getTag().keySet().toArray()) {
            String tagKey = (String) tagKeyObject;
            if (!stack2.getTag().contains(tagKey)) {
                return false;
            } else if (!stack1.getTag().get(tagKey).getString().equals(stack2.getTag().get(tagKey).getString())) {
                return false;
            }
        }
        return true;
    }
}
