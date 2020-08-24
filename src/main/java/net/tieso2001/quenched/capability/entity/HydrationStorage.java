package net.tieso2001.quenched.capability.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class HydrationStorage implements Capability.IStorage<IHydration> {

    private static final String PLAYER_HYDRATION_TAG = "playerHydration";

    @Override
    public INBT writeNBT(Capability<IHydration> capability, IHydration instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt(PLAYER_HYDRATION_TAG, instance.getHydration());
        return tag;
    }

    @Override
    public void readNBT(Capability<IHydration> capability, IHydration instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setHydration(tag.getInt(PLAYER_HYDRATION_TAG));
    }
}
