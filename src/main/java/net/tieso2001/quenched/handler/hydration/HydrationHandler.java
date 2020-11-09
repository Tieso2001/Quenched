package net.tieso2001.quenched.handler.hydration;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tieso2001.quenched.Quenched;
import net.tieso2001.quenched.capability.entity.Hydration;
import net.tieso2001.quenched.capability.entity.IHydration;
import net.tieso2001.quenched.hydration.ItemHydrationStat;
import net.tieso2001.quenched.init.ModConfig;
import net.tieso2001.quenched.network.PacketHandler;
import net.tieso2001.quenched.network.packet.DrinkFluidPacket;

@Mod.EventBusSubscriber
public class HydrationHandler {

    @SubscribeEvent
    public static void onItemUse(LivingEntityUseItemEvent.Finish event) {
        World world = event.getEntity().world;
        if (!world.isRemote) {
            if (event.getEntity() instanceof PlayerEntity) {

                PlayerEntity player = (PlayerEntity) event.getEntity();
                IHydration cap = Hydration.getFromPlayer(player);

                ItemStack stack = event.getItem();

                int hydration = 0;
                float hydrationSaturation = 0.0F;

                if (Quenched.getHydrationStatsManager().hasHydrationStat(stack)) {
                    ItemHydrationStat stat = Quenched.getHydrationStatsManager().getItemHydrationStat(stack);
                    hydration = stat.getHydration();
                    hydrationSaturation = stat.getHydrationSaturation();
                }

                cap.addStats(hydration, hydrationSaturation);
                Hydration.updateClient((ServerPlayerEntity) player, cap);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (ModConfig.COMMON.enableDirectFluidDrinking.get()) {
            int hydration = 2;
            float hydrationSaturation = 0.0F;

            PlayerEntity player = event.getPlayer();

            if (canDrinkFromFluid(event.getWorld(), player, event.getHand(), hydration, hydrationSaturation)) {
                player.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.5F, event.getWorld().rand.nextFloat() * 0.1F + 0.9F);
                if (!event.getWorld().isRemote) {
                    IHydration cap = Hydration.getFromPlayer(player);
                    cap.addStats(hydration, hydrationSaturation);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        if (ModConfig.COMMON.enableDirectFluidDrinking.get()) {
            int hydration = 2;
            float hydrationSaturation = 0.0F;

            PlayerEntity player = event.getPlayer();

            if (canDrinkFromFluid(event.getWorld(), player, event.getHand(), hydration, hydrationSaturation)) {
                player.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.5F, event.getWorld().rand.nextFloat() * 0.1F + 0.9F);
                PacketHandler.sendToServer(new DrinkFluidPacket(hydration, hydrationSaturation, new CompoundNBT()));
            }
        }
    }

    private static boolean canDrinkFromFluid(World world, PlayerEntity player, Hand hand, int hydrationValue, float saturationValue) {
        ItemStack heldItem = player.getHeldItem(hand);

        if (heldItem.isEmpty() && player.isSneaking() && hand == Hand.MAIN_HAND) {

            BlockRayTraceResult raytraceResult = rayTrace(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);

            if (raytraceResult.getType() == RayTraceResult.Type.BLOCK) {

                BlockPos blockPos = raytraceResult.getPos();
                BlockState blockState = world.getBlockState(blockPos);
                Fluid fluid = blockState.getFluidState().getFluid();

                if (fluid == Fluids.WATER) {
                    IHydration cap = Hydration.getFromPlayer(player);

                    int newHydration = Math.min(Math.max(cap.getHydration() + hydrationValue, 0), Hydration.MAX_HYDRATION);
                    float newHydrationSaturation = Math.max(Math.min(cap.getHydrationSaturation() + saturationValue, (float) cap.getHydration()), 0.0F);

                    return cap.getHydration() != newHydration || cap.getHydrationSaturation() != newHydrationSaturation;
                }
            }
        }
        return false;
    }

    private static BlockRayTraceResult rayTrace(World worldIn, PlayerEntity player, RayTraceContext.FluidMode fluidMode) {
        float f = player.rotationPitch;
        float f1 = player.rotationYaw;
        Vector3d vector3d = player.getEyePosition(1.0F);
        float f2 = MathHelper.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * ((float)Math.PI / 180F));
        float f5 = MathHelper.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = player.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue();;
        Vector3d vector3d1 = vector3d.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return worldIn.rayTraceBlocks(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.OUTLINE, fluidMode, player));
    }
}
