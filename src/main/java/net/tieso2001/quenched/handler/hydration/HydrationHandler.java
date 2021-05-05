package net.tieso2001.quenched.handler.hydration;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tieso2001.quenched.Quenched;
import net.tieso2001.quenched.capability.entity.Hydration;
import net.tieso2001.quenched.capability.entity.IHydration;
import net.tieso2001.quenched.hydration.HydrationItemInfo;

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

                if (Quenched.HYDRATION_INFO_MANAGER.hasHydration(stack)) {
                    HydrationItemInfo hydrationItemInfo = Quenched.HYDRATION_INFO_MANAGER.getHydration(stack);
                    cap.addStats(hydrationItemInfo.getHydration(), hydrationItemInfo.getHydrationSaturation());
                    Hydration.updateClient((ServerPlayerEntity) player, cap);
                }
            }
        }
    }
}
