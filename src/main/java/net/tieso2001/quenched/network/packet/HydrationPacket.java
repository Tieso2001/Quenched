package net.tieso2001.quenched.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.tieso2001.quenched.capability.entity.Hydration;
import net.tieso2001.quenched.capability.entity.HydrationProvider;
import net.tieso2001.quenched.capability.entity.IHydration;

import java.util.function.Supplier;

public class HydrationPacket {

    private static final String ENTITY_ID_TAG = "entityId";

    private final CompoundNBT nbt;

    public HydrationPacket(int entityID, CompoundNBT nbt) {
        nbt.putInt(ENTITY_ID_TAG, entityID);
        this.nbt = nbt;
    }

    public HydrationPacket(CompoundNBT nbt) {
        this.nbt = nbt;
    }

    public static void encode(HydrationPacket packet, PacketBuffer buf) {
        buf.writeCompoundTag(packet.nbt);
    }

    public static HydrationPacket decode(PacketBuffer buf) {
        return new HydrationPacket(buf.readCompoundTag());
    }

    public static void handle(HydrationPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = (PlayerEntity) Minecraft.getInstance().world.getEntityByID(packet.nbt.getInt(ENTITY_ID_TAG));
            if (player != null) {
                IHydration cap = Hydration.getFromPlayer(player);
                HydrationProvider.PLAYER_HYDRATION.readNBT(cap, null, packet.nbt);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
