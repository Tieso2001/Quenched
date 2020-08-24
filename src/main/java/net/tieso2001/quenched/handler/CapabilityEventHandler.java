package net.tieso2001.quenched.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tieso2001.quenched.Quenched;
import net.tieso2001.quenched.capability.entity.HydrationProvider;
import net.tieso2001.quenched.capability.item.ItemHydrationProvider;

@Mod.EventBusSubscriber
public class CapabilityEventHandler {

    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(Quenched.MOD_ID, "player_hydration"), new HydrationProvider());
        }
    }

    @SubscribeEvent
    public static void attachItemCapability(AttachCapabilitiesEvent<ItemStack> event){
        Item item = event.getObject().getItem();
        if (item.isFood() || item instanceof PotionItem || item == Items.MILK_BUCKET || item == Items.WATER_BUCKET) {
            event.addCapability(new ResourceLocation(Quenched.MOD_ID, "item_thirst"), new ItemHydrationProvider());
        }
    }
}
