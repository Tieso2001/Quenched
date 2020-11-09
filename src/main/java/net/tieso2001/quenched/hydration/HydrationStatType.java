package net.tieso2001.quenched.hydration;

public enum HydrationStatType {
    ITEM,
    FLUID;

    public boolean isItemType() {
        return this == ITEM;
    }

    public boolean isFluidType() {
        return this == FLUID;
    }
}
