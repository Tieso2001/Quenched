package net.tieso2001.quenched.entity.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;

public class CustomFoodStats extends FoodStats {

    private boolean foodHealthRegen = true;

    public CustomFoodStats(int foodLevel, float foodSaturationLevel, float foodExhaustionLevel, int foodTimer, int prevFoodLevel) {
        super();
        this.foodLevel = foodLevel;
        this.foodSaturationLevel = foodSaturationLevel;
        this.foodExhaustionLevel = foodExhaustionLevel;
        this.foodTimer = foodTimer;
        this.prevFoodLevel = prevFoodLevel;
    }

    @Override
    public void tick(PlayerEntity player) {
        Difficulty difficulty = player.world.getDifficulty();
        this.prevFoodLevel = this.foodLevel;
        if (this.foodExhaustionLevel > 4.0F) {
            this.foodExhaustionLevel -= 4.0F;
            if (this.foodSaturationLevel > 0.0F) {
                this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0F, 0.0F);
            } else if (difficulty != Difficulty.PEACEFUL) {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }

        boolean flag = player.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION) && this.getFoodHealthRegen();
        if (flag && this.foodSaturationLevel > 0.0F && player.shouldHeal() && this.foodLevel >= 20) {
            ++this.foodTimer;
            if (this.foodTimer >= 10) {
                float f = Math.min(this.foodSaturationLevel, 6.0F);
                player.heal(f / 6.0F);
                this.addExhaustion(f);
                this.foodTimer = 0;
            }
        } else if (flag && this.foodLevel >= 18 && player.shouldHeal()) {
            ++this.foodTimer;
            if (this.foodTimer >= 80) {
                player.heal(1.0F);
                this.addExhaustion(6.0F);
                this.foodTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            ++this.foodTimer;
            if (this.foodTimer >= 80) {
                if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
                    player.attackEntityFrom(DamageSource.STARVE, 1.0F);
                }

                this.foodTimer = 0;
            }
        } else {
            this.foodTimer = 0;
        }

    }

    public boolean getFoodHealthRegen() {
        return this.foodHealthRegen;
    }

    public void setFoodHealthRegen(boolean foodHealthRegen) {
        this.foodHealthRegen = foodHealthRegen;
    }
}
