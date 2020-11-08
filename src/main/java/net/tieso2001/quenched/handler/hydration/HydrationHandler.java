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
import net.tieso2001.quenched.hydration.ItemHydrationStat;

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
}
