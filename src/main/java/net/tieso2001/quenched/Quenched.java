package net.tieso2001.quenched;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tieso2001.quenched.capability.IThirst;
import net.tieso2001.quenched.capability.Thirst;
import net.tieso2001.quenched.capability.ThirstStorage;
import net.tieso2001.quenched.network.PacketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Quenched.MOD_ID)
public final class Quenched {

    public static final String MOD_ID = "quenched";
    public static final Logger LOGGER = LogManager.getLogger();


    public Quenched() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(IThirst.class, new ThirstStorage(), Thirst::new);
        PacketHandler.register();
    }

}