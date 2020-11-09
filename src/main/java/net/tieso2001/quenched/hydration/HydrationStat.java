package net.tieso2001.quenched.hydration;

import javax.annotation.Nullable;

public class HydrationStat {

    private final HydrationStatType type;
    private final String id;
    private final String tagName;
    private final String tagValue;
    private final int hydration;
    private final float hydrationSaturation;

    public HydrationStat(HydrationStatType type, String id, @Nullable String tagName, @Nullable String tagValue, int hydration, float hydrationSaturation) {
        this.type = type;
        this.id = id;
        this.tagName = tagName;
        this.tagValue = tagValue;
        this.hydration = hydration;
        this.hydrationSaturation = hydrationSaturation;
    }

    public HydrationStatType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public boolean hasTag() {
        return getTagName() != null && getTagValue() != null;
    }

    public String getTagName() {
        return tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    public int getHydration() {
        return hydration;
    }

    public float getHydrationSaturation() {
        return hydrationSaturation;
    }
}
