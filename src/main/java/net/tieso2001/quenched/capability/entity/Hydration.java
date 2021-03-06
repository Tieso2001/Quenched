package net.tieso2001.quenched.capability.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraftforge.registries.ForgeRegistries;
import net.tieso2001.quenched.entity.player.CustomFoodStats;
import net.tieso2001.quenched.config.Config;
import net.tieso2001.quenched.network.PacketHandler;
import net.tieso2001.quenched.network.packet.HydrationPacket;

import java.util.List;
import java.util.Objects;

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
        PacketHandler.sendToClient(player, new HydrationPacket(player.getEntityId(), (CompoundNBT) Objects.requireNonNull(HydrationProvider.PLAYER_HYDRATION.writeNBT(cap, null))));
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
            if (player.getFoodStats() instanceof CustomFoodStats) {
                CustomFoodStats foodStats = (CustomFoodStats) player.getFoodStats();
                foodStats.setFoodHealthRegen(false);
            }
            if (Config.enableDehydratedEffects.get()) {
                List<String> effectsIds = Config.dehydratedEffectsList.get();
                if (effectsIds.size() > 0) {
                    for (String effectId : effectsIds) {
                        Effect effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(effectId));
                        if (effect != null) {
                            reapplyEffect(player, effect);
                        }
                    }
                }
            }
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
            if (player.getFoodStats() instanceof CustomFoodStats) {
                CustomFoodStats foodStats = (CustomFoodStats) player.getFoodStats();
                foodStats.setFoodHealthRegen(true);
            }
            cap.setHydrationTimer(0);
        }
    }

    private static void reapplyEffect(PlayerEntity player, Effect effect) {
        EffectInstance effectInstance = player.getActivePotionEffect(effect);
        if (effectInstance != null) {
            if (effectInstance.getDuration() <= 20) {
                player.addPotionEffect(new EffectInstance(effect, 80, 0, false, false));
            }
        } else {
            player.addPotionEffect(new EffectInstance(effect, 80, 0, false, false));
        }
    }
}
