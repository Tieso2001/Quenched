package net.tieso2001.quenched.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.tieso2001.quenched.capability.entity.Hydration;
import net.tieso2001.quenched.capability.entity.IHydration;
import net.tieso2001.quenched.init.ModEffects;

public class DehydrationEffect extends Effect {

    public DehydrationEffect() {
        super(EffectType.HARMFUL, 14796361);
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        if (!entityLivingBaseIn.world.isRemote) {
            if (this == ModEffects.DEHYDRATION.get()) {
                if (entityLivingBaseIn instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) entityLivingBaseIn;
                    IHydration cap = Hydration.getFromPlayer(player);
                    cap.addHydrationExhaustion(0.005F * (float) (amplifier + 1));
                }
            } else {
                super.performEffect(entityLivingBaseIn, amplifier);
            }
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        if (this == ModEffects.DEHYDRATION.get()) {
            return true;
        }
        return super.isReady(duration, amplifier);
    }
}
