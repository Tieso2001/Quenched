package net.tieso2001.quenched.capability.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.Difficulty;
import net.tieso2001.quenched.network.PacketHandler;
import net.tieso2001.quenched.network.packet.HydrationPacket;

public class Hydration implements IHydration {

    public static final int MAX_HYDRATION = 20;

    private int hydration;
    private float decay;

    public Hydration() {
        this.hydration = MAX_HYDRATION;
        this.decay = 0;
    }

    @Override
    public void setHydration(int value) {
        if (value < 0) {
            this.hydration = 0;
        } else {
            this.hydration = Math.min(value, MAX_HYDRATION);
        }
    }

    @Override
    public int getHydration() {
        return this.hydration;
    }

    @Override
    public void setDecay(float value) {
        this.decay = value;
    }

    @Override
    public float getDecay() {
        return decay;
    }

    public static IHydration getFromPlayer(PlayerEntity player) {
        return player.getCapability(HydrationProvider.PLAYER_HYDRATION, null).orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));
    }

    public static void updateClient(ServerPlayerEntity player, IHydration cap) {
        PacketHandler.sendTo(player, new HydrationPacket(player.getEntityId(), (CompoundNBT) HydrationProvider.PLAYER_HYDRATION.writeNBT(cap, null)));
    }

    public static void tick(PlayerEntity player) {
        Difficulty difficulty = player.world.getDifficulty();
        IHydration cap = Hydration.getFromPlayer(player);
        if (cap.getDecay() >= 32.0F) {
            cap.setDecay(cap.getDecay() - 32.0F);
            if (difficulty != Difficulty.PEACEFUL) {
                cap.setHydration(cap.getHydration() - 1);
            }
        }
    }
}
