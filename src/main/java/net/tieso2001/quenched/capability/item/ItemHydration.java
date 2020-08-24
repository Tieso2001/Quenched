package net.tieso2001.quenched.capability.item;

import net.minecraft.item.ItemStack;

public class ItemHydration implements IItemHydration {

    private int hydration;

    @Override
    public void setHydration(int value) {
        this.hydration = value;
    }

    @Override
    public int getHydration() {
        return this.hydration;
    }

    public static IItemHydration getFromItem(ItemStack itemStack){
        return itemStack.getCapability(ItemHydrationProvider.ITEM_HYDRATION, null).orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));
    }
}
