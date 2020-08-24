package net.tieso2001.quenched.capability.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ThirstStorage implements Capability.IStorage<IThirst> {

    private static final String THIRST_TAG = "thirst";

    @Override
    public INBT writeNBT(Capability<IThirst> capability, IThirst instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt(THIRST_TAG, instance.getThirst());
        return tag;
    }

    @Override
    public void readNBT(Capability<IThirst> capability, IThirst instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setThirst(tag.getInt(THIRST_TAG));
    }
}
