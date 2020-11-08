package net.tieso2001.quenched.handler.hydration;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tieso2001.quenched.Quenched;
import net.tieso2001.quenched.capability.entity.Hydration;
import net.tieso2001.quenched.capability.entity.IHydration;
import net.tieso2001.quenched.init.ModEffects;

import java.awt.*;
import java.util.Random;

@Mod.EventBusSubscriber
public class HydrationOverlayHandler {

    private static final Minecraft mc = Minecraft.getInstance();
    private static final Random rand = new Random();
    private static final ResourceLocation MINECRAFT_ICONS = new ResourceLocation("minecraft", "textures/gui/icons.png");

    private static final ResourceLocation HYDRATION_ICONS = new ResourceLocation(Quenched.MOD_ID, "textures/gui/hydration_icons.png");
    private static final Rectangle DROPLET_EMPTY = new Rectangle(0, 0, 9, 9);
    private static final Rectangle DROPLET_FULL = new Rectangle(9, 0, 9, 9);
    private static final Rectangle DROPLET_HALF = new Rectangle(18, 0, 9, 9);

    private static final MatrixStack matrixStack = new MatrixStack();

    private static int ticks = 0;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !mc.isGamePaused()) {
            ticks++;
        }
    }

    @SubscribeEvent
    public static void renderAirBubbles(RenderGameOverlayEvent.Pre event) {

        if (event.getType() == RenderGameOverlayEvent.ElementType.AIR) {

            // cancel air bubbles
            if (!event.isCanceled()) {
                event.setCanceled(true);
            }

            PlayerEntity player = (PlayerEntity) mc.getRenderViewEntity();

            // render the air bubbles above hydration bar
            if (player != null) {
                if (player.isAlive()) {

                    mc.getTextureManager().bindTexture(MINECRAFT_ICONS);

                    int left = mc.getMainWindow().getScaledWidth() / 2 + 91;
                    int top = mc.getMainWindow().getScaledHeight() - 59;

                    if (player.areEyesInFluid(FluidTags.WATER) || player.getAir() < 300){

                        int full = MathHelper.ceil((double)(player.getAir() - 2) * 10.0D / 300.0D);
                        int partial = MathHelper.ceil((double)player.getAir() * 10.0D / 300.0D) - full;

                        for (int i = 0; i < full + partial; ++i) {
                            mc.ingameGUI.blit(matrixStack, left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderHydrationBar(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD && !event.isCanceled()) {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = (PlayerEntity) mc.getRenderViewEntity();

            if (player != null) {
                if (player.isAlive()) {

                    if (player.isPotionActive(ModEffects.DEHYDRATION.get())) {
                        DROPLET_FULL.x = 9 + 18;
                        DROPLET_HALF.x = 18 + 18;
                    } else {
                        DROPLET_FULL.x = 9;
                        DROPLET_HALF.x = 18;
                    }

                    mc.getTextureManager().bindTexture(HYDRATION_ICONS);

                    IHydration cap = Hydration.getFromPlayer(player);
                    int hydration = cap.getHydration();

                    // hydration bar position
                    int hydrationPosX = mc.getMainWindow().getScaledWidth() / 2 + 10;
                    int hydrationPosY = mc.getMainWindow().getScaledHeight() - 49;

                    // filled droplets
                    int droplets;
                    boolean half = false;

                    if (hydration % 2 == 1) {
                        droplets = (hydration - 1) / 2;
                        half = true;
                    } else {
                        droplets = hydration / 2;
                    }

                    rand.setSeed(ticks * 312871);

                    // render droplets
                    for (int i = 0; i < 10; i++) {

                        // hydration bar shaking when saturation is gone
                        if (cap.getHydrationSaturation() <= 0.0F && ticks % (cap.getHydration() * 3 + 1) == 0) {
                            hydrationPosY = (mc.getMainWindow().getScaledHeight() - 49) + (rand.nextInt(3) - 1);
                        }

                        // render empty droplet
                        mc.ingameGUI.blit(matrixStack, hydrationPosX, hydrationPosY, DROPLET_EMPTY.x, DROPLET_EMPTY.y, DROPLET_EMPTY.width, DROPLET_EMPTY.height);

                        // render filled droplets
                        if (half && droplets == (9 - i)) {
                            // render half droplet
                            mc.ingameGUI.blit(matrixStack, hydrationPosX, hydrationPosY, DROPLET_HALF.x, DROPLET_HALF.y, DROPLET_HALF.width, DROPLET_HALF.height);
                        } else if (droplets >= (10 - i)) {
                            // render full droplet
                            mc.ingameGUI.blit(matrixStack, hydrationPosX, hydrationPosY, DROPLET_FULL.x, DROPLET_FULL.y, DROPLET_FULL.width, DROPLET_FULL.height);
                        }
                        hydrationPosX += (DROPLET_EMPTY.width - 1);
                    }
                }
            }
        }
    }
}
