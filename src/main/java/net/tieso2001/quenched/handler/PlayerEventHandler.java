package net.tieso2001.quenched.handler;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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
        if (!event.player.world.isRemote) {
            Thirst.updateClient((ServerPlayerEntity) event.player, Thirst.getFromPlayer(event.player));
        }
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event){
        World world = event.getWorld();

        if (!world.isRemote){

            Block testblock = world.getBlockState(event.getPos()).getBlock();

            PlayerEntity player = event.getPlayer();
            IThirst thirstCap = Thirst.getFromPlayer(player);

            // for testing purposes
            if (event.getHand() == Hand.MAIN_HAND) {
                if (testblock == Blocks.DIRT) {
                    thirstCap.setThirst(thirstCap.getThirst() - 1);
                    world.playSound(null, event.getPlayer().getPosX(), event.getPlayer().getPosY(), event.getPlayer().getPosZ(), SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.NEUTRAL, 1.0F, 1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);
                } else if (testblock == Blocks.GRASS_BLOCK) {
                    thirstCap.setThirst(thirstCap.getThirst() + 1);
                    world.playSound(null, event.getPlayer().getPosX(), event.getPlayer().getPosY(), event.getPlayer().getPosZ(), SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.NEUTRAL, 1.0F, 1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);
                }
            }
        }
    }
}
