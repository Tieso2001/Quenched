package net.tieso2001.quenched.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.tieso2001.quenched.capability.entity.Hydration;
import net.tieso2001.quenched.capability.entity.IHydration;

import java.util.function.Supplier;

public class DrinkFluidPacket {

    private static final String HYDRATION_VALUE_TAG = "hydrationValue";
    private static final String SATURATION_VALUE_TAG = "saturationValue";

    private final CompoundNBT nbt;

    public DrinkFluidPacket(int hydrationValue, float saturationValue, CompoundNBT nbt) {
        nbt.putInt(HYDRATION_VALUE_TAG, hydrationValue);
        nbt.putFloat(SATURATION_VALUE_TAG, saturationValue);
        this.nbt = nbt;
    }

    public DrinkFluidPacket(CompoundNBT nbt) {
        this.nbt = nbt;
    }

    public static void encode(DrinkFluidPacket packet, PacketBuffer buf) {
        buf.writeCompoundTag(packet.nbt);
    }

    public static DrinkFluidPacket decode(PacketBuffer buf) {
        return new DrinkFluidPacket(buf.readCompoundTag());
    }

    public static void handle(DrinkFluidPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player != null) {
                IHydration cap = Hydration.getFromPlayer(player);
                cap.addStats(packet.nbt.getInt(HYDRATION_VALUE_TAG), packet.nbt.getFloat(SATURATION_VALUE_TAG));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
