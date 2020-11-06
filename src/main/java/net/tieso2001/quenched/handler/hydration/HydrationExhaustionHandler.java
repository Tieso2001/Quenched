package net.tieso2001.quenched.handler.hydration;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tieso2001.quenched.capability.entity.Hydration;
import net.tieso2001.quenched.capability.entity.IHydration;

@Mod.EventBusSubscriber
public class HydrationExhaustionHandler {

    private static double prevPosX;
    private static double prevPosY;
    private static double prevPosZ;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        World world = event.player.world;
        if (!world.isRemote) {
            PlayerEntity player = event.player;
            if (!player.abilities.disableDamage && event.phase == TickEvent.Phase.START && !player.isPassenger()) {

                IHydration cap = Hydration.getFromPlayer(player);

                double distance = calculateDistance(player.getPosX(), player.getPosY(), player.getPosZ());
                double horizontalDistance = calculateDistance(player.getPosX(), prevPosX, 0, 0, player.getPosZ(), prevPosZ);
                prevPosX = player.getPosX();
                prevPosY = player.getPosY();
                prevPosZ = player.getPosZ();

                if ((player.isSwimming() || player.areEyesInFluid(FluidTags.WATER)) && distance > 0) {
                    cap.addHydrationExhaustion((float) (0.01F * distance));
                } else if (player.isInWater() && horizontalDistance > 0) {
                    cap.addHydrationExhaustion((float) (0.01F * horizontalDistance));
                } else if (player.isOnGround() && player.isSprinting() && horizontalDistance > 0) {
                    cap.addHydrationExhaustion((float) (0.1F * horizontalDistance));
                }

                Hydration.tick(player);
                Hydration.updateClient((ServerPlayerEntity) event.player, Hydration.getFromPlayer(event.player));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        World world = event.getEntity().world;
        if (!world.isRemote) {
            if (event.getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) event.getEntity();
                if (!player.abilities.disableDamage) {
                    IHydration cap = Hydration.getFromPlayer(player);
                    if (player.isSprinting()) {
                        cap.addHydrationExhaustion(0.2F);
                    } else {
                        cap.addHydrationExhaustion(0.05F);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        World world = player.world;
        if (!world.isRemote) {
            if (!event.isCanceled()) {
                IHydration cap = Hydration.getFromPlayer(player);
                cap.addHydrationExhaustion(0.005F);
            }
        }
    }

    /* TODO
    @SubscribeEvent
    public static void onPlayerAttackTarget(AttackEntityEvent event) {
        World world = event.getEntity().world;
        if (!world.isRemote) {
            if (event.getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) event.getEntity();
                float damageAmount = (float) player.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();
                if (!player.abilities.disableDamage && event.getTarget().attackEntityFrom(DamageSource.causePlayerDamage(player), damageAmount)) {
                    IHydration cap = Hydration.getFromPlayer(player);
                    cap.addHydrationExhaustion(0.1F);
                }
            }
        }
    }
     */

    @SubscribeEvent
    public static void onPlayerDamage(LivingDamageEvent event) {
        World world = event.getEntity().world;
        if (!world.isRemote) {
            if (event.getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) event.getEntity();
                if (!player.abilities.disableDamage && event.getAmount() != 0.0F) {
                    IHydration cap = Hydration.getFromPlayer(player);
                    cap.addHydrationExhaustion(event.getSource().getHungerDamage());
                }
            }
        }
    }

    private static double calculateDistance(double posX, double posY, double posZ) {
        return calculateDistance(posX, prevPosX, posY, prevPosY, posZ, prevPosZ);
    }

    private static double calculateDistance(double posX, double prevPosX, double posY, double prevPosY, double posZ, double prevPosZ) {
        double dx = posX - prevPosX;
        double dy = posY - prevPosY;
        double dz = posZ - prevPosZ;
        return Math.round(MathHelper.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F) * 0.01F;
    }
}
