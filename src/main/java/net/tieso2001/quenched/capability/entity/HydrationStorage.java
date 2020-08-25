package net.tieso2001.quenched.capability.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class HydrationStorage implements Capability.IStorage<IHydration> {

    private static final String PLAYER_HYDRATION_TAG = "playerHydration";
    private static final String PLAYER_HYDRATION_SATURATION_TAG = "playerHydrationSaturation";
    private static final String PLAYER_HYDRATION_EXHAUSTION_TAG = "playerHydrationExhaustion";
    private static final String PLAYER_HYDRATION_TIMER_TAG = "playerHydrationTimer";


    @Override
    public INBT writeNBT(Capability<IHydration> capability, IHydration instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt(PLAYER_HYDRATION_TAG, instance.getHydration());
        tag.putFloat(PLAYER_HYDRATION_SATURATION_TAG, instance.getHydrationSaturation());
        tag.putFloat(PLAYER_HYDRATION_EXHAUSTION_TAG, instance.getHydrationExhaustion());
        tag.putInt(PLAYER_HYDRATION_TIMER_TAG, instance.getHydrationTimer());
        return tag;
    }

    @Override
    public void readNBT(Capability<IHydration> capability, IHydration instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setHydration(tag.getInt(PLAYER_HYDRATION_TAG));
        instance.setHydrationSaturation(tag.getFloat(PLAYER_HYDRATION_SATURATION_TAG));
        instance.setHydrationExhaustion(tag.getFloat(PLAYER_HYDRATION_EXHAUSTION_TAG));
        instance.setHydrationTimer(tag.getInt(PLAYER_HYDRATION_TIMER_TAG));
    }
}
