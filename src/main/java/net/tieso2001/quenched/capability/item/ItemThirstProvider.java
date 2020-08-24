package net.tieso2001.quenched.capability.item;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemThirstProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(IItemThirst.class)
    public static Capability<IItemThirst> THIRST = null;

    private final LazyOptional<IItemThirst> instance;

    public ItemThirstProvider() {
        instance = LazyOptional.of(ItemThirst::new);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == THIRST ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) THIRST.getStorage().writeNBT(THIRST, this.instance.orElseThrow(
                () -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        THIRST.getStorage().readNBT(THIRST, this.instance.orElseThrow(
                () -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}
