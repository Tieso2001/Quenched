package net.tieso2001.quenched.capability.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Difficulty;
import net.tieso2001.quenched.network.PacketHandler;
import net.tieso2001.quenched.network.packet.HydrationPacket;

public class Hydration implements IHydration {

    public static final int MAX_HYDRATION = 20;

    private static final DamageSource DEHYDRATION = new DamageSource("dehydration").setDamageBypassesArmor().setDamageIsAbsolute();

    private int hydration;
    private float hydrationSaturation;
    private float hydrationExhaustion;
    private int hydrationTimer;

    public Hydration() {
        this.setHydration(MAX_HYDRATION);
        this.setHydrationSaturation(5.0F);
    }

    @Override
    public void addStats(int hydration, float saturation) {
        this.setHydration(this.getHydration() + hydration);
        this.setHydrationSaturation(this.getHydrationSaturation() + saturation);
    }

    @Override
    public void setHydration(int value) {
        this.hydration = Math.min(Math.max(value, 0), MAX_HYDRATION);
    }

    @Override
    public int getHydration() {
        return this.hydration;
    }

    @Override
    public void setHydrationSaturation(float value) {
        this.hydrationSaturation = Math.max(Math.min(value, (float) this.hydration), 0.0F);
    }

    @Override
    public float getHydrationSaturation() {
        return hydrationSaturation;
    }

    @Override
    public void setHydrationExhaustion(float value) {
        this.hydrationExhaustion = value;
    }

    @Override
    public float getHydrationExhaustion() {
        return hydrationExhaustion;
    }

    @Override
    public void addHydrationExhaustion(float value) {
        this.setHydrationExhaustion(this.getHydrationExhaustion() + value);
    }

    @Override
    public void setHydrationTimer(int value) {
        this.hydrationTimer = value;
    }

    @Override
    public int getHydrationTimer() {
        return hydrationTimer;
    }

    public static IHydration getFromPlayer(PlayerEntity player) {
        return player.getCapability(HydrationProvider.PLAYER_HYDRATION, null).orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));
    }

    public static void updateClient(ServerPlayerEntity player, IHydration cap) {
        PacketHandler.sendTo(player, new HydrationPacket(player.getEntityId(), (CompoundNBT) HydrationProvider.PLAYER_HYDRATION.writeNBT(cap, null)));
    }

    public static void tick(PlayerEntity player) {
        Difficulty difficulty = player.world.getDifficulty();
        IHydration cap = Hydration.getFromPlayer(player);
        if (cap.getHydrationExhaustion() > 4.0F) {
            cap.setHydrationExhaustion(cap.getHydrationExhaustion() - 4.0F);
            if (cap.getHydrationSaturation() > 0.0F) {
                cap.setHydrationSaturation(cap.getHydrationSaturation() - 1.0F);
            } else if (difficulty != Difficulty.PEACEFUL) {
                cap.setHydration(cap.getHydration() - 1);
            }
        }

        if (cap.getHydration() <= 6) {
            player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 5, 0, false, false));
            player.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 5, 0, false, false));
            player.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 5, 0, false, false));
            if (cap.getHydration() <= 0) {
                cap.setHydrationTimer(cap.getHydrationTimer() + 1);
                if (cap.getHydrationTimer() >= 80) {
                    if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
                        player.attackEntityFrom(DEHYDRATION, 1.0F);
                    }
                    cap.setHydrationTimer(0);
                }
            }
        } else {
            cap.setHydrationTimer(0);
        }
    }
}
