package net.tieso2001.quenched.capability.item;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemHydrationProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(IItemHydration.class)
    public static Capability<IItemHydration> ITEM_HYDRATION = null;

    private final LazyOptional<IItemHydration> instance;

    public ItemHydrationProvider() {
        instance = LazyOptional.of(ItemHydration::new);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == ITEM_HYDRATION ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) ITEM_HYDRATION.getStorage().writeNBT(ITEM_HYDRATION, this.instance.orElseThrow(
                () -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ITEM_HYDRATION.getStorage().readNBT(ITEM_HYDRATION, this.instance.orElseThrow(
                () -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}
