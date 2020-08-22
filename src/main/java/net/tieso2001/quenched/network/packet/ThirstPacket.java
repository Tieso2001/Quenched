package net.tieso2001.quenched.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.tieso2001.quenched.capability.IThirst;
import net.tieso2001.quenched.capability.Thirst;
import net.tieso2001.quenched.capability.ThirstProvider;

import java.util.function.Supplier;

public class ThirstPacket {

    private static final String ENTITY_ID_TAG = "entityId";

    private final CompoundNBT nbt;

    public ThirstPacket(int entityID, CompoundNBT nbt) {
        nbt.putInt(ENTITY_ID_TAG, entityID);
        this.nbt = nbt;
    }

    public ThirstPacket(CompoundNBT nbt) {
        this.nbt = nbt;
    }

    public static void encode(ThirstPacket packet, PacketBuffer buf) {
        buf.writeCompoundTag(packet.nbt);
    }

    public static ThirstPacket decode(PacketBuffer buf) {
        return new ThirstPacket(buf.readCompoundTag());
    }

    public static void handle(ThirstPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = (PlayerEntity) Minecraft.getInstance().world.getEntityByID(packet.nbt.getInt(ENTITY_ID_TAG));
            if (player != null) {
                IThirst cap = Thirst.getFromPlayer(player);
                ThirstProvider.THIRST.readNBT(cap, null, packet.nbt);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
