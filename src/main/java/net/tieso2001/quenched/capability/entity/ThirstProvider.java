package net.tieso2001.quenched.capability.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ThirstProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(IThirst.class)
    public static Capability<IThirst> THIRST = null;

    private final LazyOptional<IThirst> instance;

    public ThirstProvider() {
        instance = LazyOptional.of(Thirst::new);
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
