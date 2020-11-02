package net.tieso2001.quenched.hydration;

public class HydrationItem {

    private String item;
    private int hydration;
    private int hydrationSaturation;

    public String getItem() {
        return item;
    }

    public int getHydration() {
        return hydration;
    }

    public int getHydrationSaturation() {
        return hydrationSaturation;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setHydration(int hydration) {
        this.hydration = hydration;
    }

    public void setHydrationSaturation(int hydrationSaturation) {
        this.hydrationSaturation = hydrationSaturation;
    }

    public String toString() {
        return String.format("item:%s,hydration:%s,hydrationSaturation:%s", item, hydration, hydrationSaturation);
    }
}
