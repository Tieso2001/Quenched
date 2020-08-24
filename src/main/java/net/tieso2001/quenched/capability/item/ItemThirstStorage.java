package net.tieso2001.quenched.capability.item;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ItemThirstStorage implements Capability.IStorage<IItemThirst> {

    private static final String THIRST_TAG = "thirst";


    @Override
    public INBT writeNBT(Capability<IItemThirst> capability, IItemThirst instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt(THIRST_TAG, instance.getThirst());
        return tag;
    }

    @Override
    public void readNBT(Capability<IItemThirst> capability, IItemThirst instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setThirst(tag.getInt(THIRST_TAG));
    }
}
