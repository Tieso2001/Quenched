package net.tieso2001.quenched.capability.item;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ItemHydrationStorage implements Capability.IStorage<IItemHydration> {

    private static final String ITEM_HYDRATION_TAG = "itemHydration";
    private static final String ITEM_HYDRATION_SATURATION_TAG = "itemHydrationSaturation";

    @Override
    public INBT writeNBT(Capability<IItemHydration> capability, IItemHydration instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt(ITEM_HYDRATION_TAG, instance.getHydration());
        tag.putFloat(ITEM_HYDRATION_SATURATION_TAG, instance.getHydrationSaturation());
        return tag;
    }

    @Override
    public void readNBT(Capability<IItemHydration> capability, IItemHydration instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setHydration(tag.getInt(ITEM_HYDRATION_TAG));
        instance.setHydrationSaturation(tag.getFloat(ITEM_HYDRATION_SATURATION_TAG));
    }
}
