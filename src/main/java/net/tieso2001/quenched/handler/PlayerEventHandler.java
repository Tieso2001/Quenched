package net.tieso2001.quenched.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tieso2001.quenched.Quenched;
import net.tieso2001.quenched.capability.IThirst;
import net.tieso2001.quenched.capability.Thirst;
import net.tieso2001.quenched.capability.ThirstProvider;

@Mod.EventBusSubscriber
public class PlayerEventHandler {

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(Quenched.MOD_ID, "thirst"), new ThirstProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){

        World world = event.player.world;
        if (!world.isRemote) {
            PlayerEntity player = event.player;
            IThirst cap = Thirst.getFromPlayer(player);
            if (player.isSprinting()) {
                cap.setDecay(cap.getDecay() + 0.05F);
            }
            if (player.isSwimming()) {
                cap.setDecay(cap.getDecay() + 0.005F);
            }
            Thirst.tick(player);
            Thirst.updateClient((ServerPlayerEntity) event.player, Thirst.getFromPlayer(event.player));
        }
    }

    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event){
        World world = event.getEntityLiving().world;
        if (!world.isRemote) {
            if (event.getEntityLiving() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) event.getEntityLiving();
                IThirst cap = Thirst.getFromPlayer(player);
                if (player.isSprinting()) {
                    cap.setDecay(cap.getDecay() + 0.075F);
                } else {
                    cap.setDecay(cap.getDecay() + 0.05F);
                }
            }
        }
    }
}
