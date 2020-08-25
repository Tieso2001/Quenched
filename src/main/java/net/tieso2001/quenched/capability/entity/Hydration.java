package net.tieso2001.quenched.capability.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
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
        this.hydration = MAX_HYDRATION;
        this.hydrationSaturation = 5.0F;
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
        this.hydrationSaturation = value;
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
                cap.setHydrationSaturation(Math.max(cap.getHydrationSaturation() - 1.0F, 0.0F));
            } else if (difficulty != Difficulty.PEACEFUL) {
                cap.setHydration(cap.getHydration() - 1);
            }
        }

        boolean flag = player.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);
        if (flag && cap.getHydrationSaturation() > 0.0F && player.shouldHeal() && cap.getHydration() >= MAX_HYDRATION) {
            cap.setHydrationTimer(cap.getHydrationTimer() + 1);
            if (cap.getHydrationTimer() >= 10) {
                float f = Math.min(cap.getHydrationSaturation(), 6.0F);
                player.heal(f / 6.0F);
                cap.setHydrationExhaustion(cap.getHydrationExhaustion() + f);
                cap.setHydrationTimer(0);
            }
        } else if (flag && cap.getHydration() >= 18 && player.shouldHeal()) {
            cap.setHydrationTimer(cap.getHydrationTimer() + 1);
            if (cap.getHydrationTimer() >= 80) {
                player.heal(1.0F);
                cap.setHydrationExhaustion(cap.getHydrationExhaustion() + 6.0F);
                cap.setHydrationTimer(0);
            }
        } else if (cap.getHydration() <= 0) {
            cap.setHydrationTimer(cap.getHydrationTimer() + 1);
            if (cap.getHydrationTimer() >= 80) {
                if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
                    player.attackEntityFrom(DEHYDRATION, 1.0F);
                }

                cap.setHydrationTimer(0);
            }
        } else {
            cap.setHydrationTimer(0);
        }
    }
}
