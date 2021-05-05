package net.tieso2001.quenched;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tieso2001.quenched.capability.entity.Hydration;
import net.tieso2001.quenched.capability.entity.HydrationStorage;
import net.tieso2001.quenched.capability.entity.IHydration;
import net.tieso2001.quenched.hydration.HydrationInfoManager;
import net.tieso2001.quenched.config.Config;
import net.tieso2001.quenched.init.ModEffects;
import net.tieso2001.quenched.network.PacketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Quenched.MOD_ID)
public final class Quenched {

    public static final String MOD_ID = "quenched";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final HydrationInfoManager HYDRATION_INFO_MANAGER = new HydrationInfoManager();

    public Quenched() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::setup);
        ModEffects.EFFECTS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
    }

    private void setup(final FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(IHydration.class, new HydrationStorage(), Hydration::new);
        PacketHandler.register();
    }

    @SubscribeEvent
    public void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(HYDRATION_INFO_MANAGER);
    }
}
