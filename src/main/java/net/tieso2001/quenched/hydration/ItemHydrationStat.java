package net.tieso2001.quenched.hydration;

public class ItemHydrationStat {

    private final String id;
    private final String tagName;
    private final String tagValue;
    private final int hydration;
    private final float hydrationSaturation;

    public ItemHydrationStat(String id, String tagName, String tagValue, int hydration, float hydrationSaturation) {
        this.id = id;
        this.tagName = tagName;
        this.tagValue = tagValue;
        this.hydration = hydration;
        this.hydrationSaturation = hydrationSaturation;
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
