package net.tieso2001.quenched.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.tieso2001.quenched.Quenched;
import net.tieso2001.quenched.network.packet.DrinkFluidPacket;
import net.tieso2001.quenched.network.packet.HydrationPacket;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Quenched.MOD_ID, "quenched"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++, HydrationPacket.class, HydrationPacket::encode, HydrationPacket::decode, HydrationPacket::handle);
        INSTANCE.registerMessage(id++, DrinkFluidPacket.class, DrinkFluidPacket::encode, DrinkFluidPacket::decode, DrinkFluidPacket::handle);
    }

    public static void sendToClient(ServerPlayerEntity player, Object packet) {
        INSTANCE.sendTo(packet, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }
}
