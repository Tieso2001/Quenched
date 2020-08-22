package net.tieso2001.quenched.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.tieso2001.quenched.network.PacketHandler;
import net.tieso2001.quenched.network.packet.ThirstPacket;

public class Thirst implements IThirst {

    public static final int MAX_THIRST = 20;

    private int thirst;

    public Thirst() {
        this.thirst = MAX_THIRST;
    }

    @Override
    public void setThirst(int value) {
        if (value < 0) {
            this.thirst = 0;
        } else {
            this.thirst = Math.min(value, MAX_THIRST);
        }
    }

    @Override
    public int getThirst() {
        return this.thirst;
    }

    public static IThirst getFromPlayer(PlayerEntity player) {
        return player.getCapability(ThirstProvider.THIRST, null).orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));
    }

    public static void updateClient(ServerPlayerEntity player, IThirst cap) {
        PacketHandler.sendTo(player, new ThirstPacket(player.getEntityId(), (CompoundNBT) ThirstProvider.THIRST.writeNBT(cap, null)));
    }

    public static void tick(PlayerEntity player) {

    }
}
