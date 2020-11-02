package net.tieso2001.quenched.handler.hydration;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tieso2001.quenched.capability.entity.Hydration;
import net.tieso2001.quenched.capability.entity.IHydration;
import net.tieso2001.quenched.capability.item.IItemHydration;
import net.tieso2001.quenched.capability.item.ItemHydration;
import net.tieso2001.quenched.hydration.HydrationItem;
import net.tieso2001.quenched.hydration.HydrationManager;

import java.util.Objects;

@Mod.EventBusSubscriber
public class HydrationHandler {

    @SubscribeEvent
    public static void onItemUse(LivingEntityUseItemEvent.Finish event) {
        World world = event.getEntity().world;
        if (!world.isRemote) {
            if (event.getEntity() instanceof PlayerEntity) {

                PlayerEntity player = (PlayerEntity) event.getEntity();
                IHydration playerCap = Hydration.getFromPlayer(player);

                ItemStack stack = event.getItem();
                IItemHydration itemCap = ItemHydration.getFromItem(stack);

                for (HydrationItem hydrationItem : HydrationManager.hydrationItems) {
                    if (Objects.equals(stack.getItem().getRegistryName(), new ResourceLocation(hydrationItem.getItem()))) {
                        itemCap.setHydration(hydrationItem.getHydration());
                        itemCap.setHydrationSaturation(hydrationItem.getHydrationSaturation());
                    }
                }

                if (stack.getItem() == Items.POTION) {
                    if (stack.hasTag()) {
                        if (stack.getTag().getString("Potion").equals("minecraft:water")) {
                            itemCap.setHydration(-8);
                            itemCap.setHydrationSaturation(-20);
                        }
                    }
                }

                playerCap.addStats(itemCap.getHydration(), itemCap.getHydrationSaturation());
                Hydration.updateClient((ServerPlayerEntity) player, playerCap);
            }
        }
    }
}
