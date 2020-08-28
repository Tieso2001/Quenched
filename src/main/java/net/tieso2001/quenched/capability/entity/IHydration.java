package net.tieso2001.quenched.capability.entity;

public interface IHydration {

    void addStats(int hydration, float hydrationSaturation);

    void setHydration(int value);
    int getHydration();

    void setHydrationSaturation(float value);
    float getHydrationSaturation();

    void setHydrationExhaustion(float value);
    float getHydrationExhaustion();
    void addHydrationExhaustion(float value);

    void setHydrationTimer(int value);
    int getHydrationTimer();
}
