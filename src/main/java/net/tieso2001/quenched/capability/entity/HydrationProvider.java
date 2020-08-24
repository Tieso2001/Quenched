package net.tieso2001.quenched.capability.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HydrationProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(IHydration.class)
    public static Capability<IHydration> PLAYER_HYDRATION = null;

    private final LazyOptional<IHydration> instance;

    public HydrationProvider() {
        instance = LazyOptional.of(Hydration::new);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == PLAYER_HYDRATION ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) PLAYER_HYDRATION.getStorage().writeNBT(PLAYER_HYDRATION, this.instance.orElseThrow(
                () -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        PLAYER_HYDRATION.getStorage().readNBT(PLAYER_HYDRATION, this.instance.orElseThrow(
                () -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}
