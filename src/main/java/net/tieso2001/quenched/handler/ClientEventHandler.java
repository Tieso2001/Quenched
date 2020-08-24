package net.tieso2001.quenched.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tieso2001.quenched.Quenched;
import net.tieso2001.quenched.capability.entity.IThirst;
import net.tieso2001.quenched.capability.entity.Thirst;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber
public class ClientEventHandler {

    private static final ResourceLocation MINECRAFT_ICONS = new ResourceLocation("minecraft", "textures/gui/icons.png");

    private static final ResourceLocation THIRST_GUI_ELEMENTS = new ResourceLocation(Quenched.MOD_ID, "textures/gui/thirst.png");
    private static final Rectangle THIRST_EMPTY = new Rectangle(0, 9, 9, 9);
    private static final Rectangle THIRST_FULL = new Rectangle(9, 9, 9, 9);
    private static final Rectangle THIRST_HALF = new Rectangle(18, 9, 9, 9);

    @SubscribeEvent
    public static void onRenderPre(RenderGameOverlayEvent.Pre event) {

        if (event.getType() == RenderGameOverlayEvent.ElementType.AIR) {

            // cancel air bubbles
            if (!event.isCanceled()) {
                event.setCanceled(true);
            }

            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = (PlayerEntity) mc.getRenderViewEntity();

            // render the air bubbles above thirst bar
            if (player.isAlive()) {

                mc.getTextureManager().bindTexture(MINECRAFT_ICONS);

                int left = mc.getMainWindow().getScaledWidth() / 2 + 91;
                int top = mc.getMainWindow().getScaledHeight() - 59;

                if (player.areEyesInFluid(FluidTags.WATER) || player.getAir() < 300){

                    int full = MathHelper.ceil((double)(player.getAir() - 2) * 10.0D / 300.0D);
                    int partial = MathHelper.ceil((double)player.getAir() * 10.0D / 300.0D) - full;

                    for (int i = 0; i < full + partial; ++i) {
                        mc.ingameGUI.blit(left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderPost(RenderGameOverlayEvent.Post event) {

        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD && !event.isCanceled()) {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = (PlayerEntity) mc.getRenderViewEntity();

            if (player.isAlive()){

                mc.getTextureManager().bindTexture(THIRST_GUI_ELEMENTS);

                IThirst thirstCap = Thirst.getFromPlayer(player);
                int thirst = thirstCap.getThirst();

                // thirst bar position
                int thirstPosX = mc.getMainWindow().getScaledWidth() / 2 + 10;
                int thirstPosY = mc.getMainWindow().getScaledHeight() - 49;

                // render empty droplets
                for (int i = 0; i < 10; i++) {
                    mc.ingameGUI.blit(thirstPosX, thirstPosY, THIRST_EMPTY.x, THIRST_EMPTY.y, THIRST_EMPTY.width, THIRST_EMPTY.height);
                    thirstPosX += (THIRST_EMPTY.width - 1);
                }

                /* render filled droplets */

                // x position of last droplet
                thirstPosX = mc.getMainWindow().getScaledWidth() / 2 + 10;
                thirstPosX += 9 * (THIRST_EMPTY.width - 1);

                int droplets;
                boolean half = false;

                if (thirst % 2 == 1) {
                    droplets = (thirst - 1) / 2;
                    half = true;
                } else {
                    droplets = thirst / 2;
                }

                for (int i = 0; i < droplets; i++) {
                    mc.ingameGUI.blit(thirstPosX, thirstPosY, THIRST_FULL.x, THIRST_FULL.y, THIRST_FULL.width, THIRST_FULL.height);
                    thirstPosX -= (THIRST_FULL.width - 1);
                }

                if (half) {
                    mc.ingameGUI.blit(thirstPosX, thirstPosY, THIRST_HALF.x, THIRST_HALF.y, THIRST_HALF.width, THIRST_HALF.height);
                }
            }
        }
    }
}
