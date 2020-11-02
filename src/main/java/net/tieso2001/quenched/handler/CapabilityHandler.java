package net.tieso2001.quenched.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tieso2001.quenched.Quenched;
import net.tieso2001.quenched.capability.entity.HydrationProvider;
import net.tieso2001.quenched.capability.item.ItemHydrationProvider;
import net.tieso2001.quenched.entity.player.CustomFoodStats;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber
public class CapabilityHandler {

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
            event.addCapability(new ResourceLocation(Quenched.MOD_ID, "item_hydration"), new ItemHydrationProvider());
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote) {
            if (event.getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) event.getEntity();
                Class c = player.getClass().getSuperclass();
                try {
                    Field foodStats = c.getDeclaredField("foodStats");
                    if (!foodStats.isAccessible()) {
                        foodStats.setAccessible(true);
                    }
                    foodStats.set(player, new CustomFoodStats());
                } catch (NoSuchFieldException | IllegalAccessException x) {
                    x.printStackTrace();
                }
            }
        }
    }
}
