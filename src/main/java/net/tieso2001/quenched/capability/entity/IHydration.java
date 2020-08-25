package net.tieso2001.quenched.capability.entity;

public interface IHydration {

    void setHydration(int value);
    int getHydration();

    void setHydrationSaturation(float value);
    float getHydrationSaturation();

    void setHydrationExhaustion(float value);
    float getHydrationExhaustion();

    void setHydrationTimer(int value);
    int getHydrationTimer();
}
