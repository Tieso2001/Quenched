package net.tieso2001.quenched.capability.item;

import net.minecraft.item.ItemStack;

public class ItemThirst implements IItemThirst {

    private int thirst;

    @Override
    public void setThirst(int value) {
        this.thirst = value;
    }

    @Override
    public int getThirst() {
        return this.thirst;
    }

    public static IItemThirst getFromItem(ItemStack itemStack){
        return itemStack.getCapability(ItemThirstProvider.THIRST, null).orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));
    }
}
