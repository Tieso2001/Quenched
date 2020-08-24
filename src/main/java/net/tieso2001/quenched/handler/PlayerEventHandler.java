package net.tieso2001.quenched.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tieso2001.quenched.capability.entity.Hydration;
import net.tieso2001.quenched.capability.entity.IHydration;
import net.tieso2001.quenched.capability.item.IItemHydration;
import net.tieso2001.quenched.capability.item.ItemHydration;

@Mod.EventBusSubscriber
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        World world = event.player.world;
        if (!world.isRemote) {
            PlayerEntity player = event.player;
            if (!player.isCreative() && !player.isSpectator()) {
                IHydration cap = Hydration.getFromPlayer(player);
                if (player.isSprinting()) {
                    cap.setDecay(cap.getDecay() + 0.05F);
                }
                if (player.isSwimming()) {
                    cap.setDecay(cap.getDecay() + 0.005F);
                }
                Hydration.tick(player);
                Hydration.updateClient((ServerPlayerEntity) event.player, Hydration.getFromPlayer(event.player));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event){
        World world = event.getEntity().world;
        if (!world.isRemote) {
            if (event.getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) event.getEntity();
                if (!player.isCreative() && !player.isSpectator()) {
                    IHydration cap = Hydration.getFromPlayer(player);
                    if (player.isSprinting()) {
                        cap.setDecay(cap.getDecay() + 0.075F);
                    } else {
                        cap.setDecay(cap.getDecay() + 0.05F);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUse(LivingEntityUseItemEvent.Finish event) {
        World world = event.getEntity().world;
        if (!world.isRemote) {
            if (event.getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) event.getEntity();
                IHydration playerCap = Hydration.getFromPlayer(player);
                ItemStack stack = event.getItem();
                IItemHydration itemCap = ItemHydration.getFromItem(stack);
                if (stack.getItem() == Items.APPLE) {
                    itemCap.setHydration(1);
                } else if (stack.getItem() == Items.POTION) {
                    if (stack.hasTag()) {
                        if (stack.getTag().getString("Potion").equals("minecraft:water")) {
                            itemCap.setHydration(8);
                        }
                    }
                }
                playerCap.setHydration(playerCap.getHydration() + itemCap.getHydration());
                Hydration.updateClient((ServerPlayerEntity) player, playerCap);
            }
        }
    }
}
