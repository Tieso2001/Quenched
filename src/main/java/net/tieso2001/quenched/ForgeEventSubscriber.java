package net.tieso2001.quenched;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;

@Mod.EventBusSubscriber(modid = Quenched.MOD_ID)
public class ForgeEventSubscriber {

    @SubscribeEvent
    public static void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        //event.getServer().get().addReloadListener(Quenched.getHydrationStatsManager()); TODO
    }
}
