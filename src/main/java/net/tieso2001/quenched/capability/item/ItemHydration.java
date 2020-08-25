package net.tieso2001.quenched.capability.item;

import net.minecraft.item.ItemStack;

public class ItemHydration implements IItemHydration {

    private int hydration;
    private float hydrationSaturation;

    @Override
    public void setHydration(int value) {
        this.hydration = value;
    }

    @Override
    public int getHydration() {
        return this.hydration;
    }

    @Override
    public void setHydrationSaturation(float value) {
        this.hydrationSaturation = value;
    }

    @Override
    public float getHydrationSaturation() {
        return hydrationSaturation;
    }

    public static IItemHydration getFromItem(ItemStack itemStack){
        return itemStack.getCapability(ItemHydrationProvider.ITEM_HYDRATION, null).orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));
    }
}
