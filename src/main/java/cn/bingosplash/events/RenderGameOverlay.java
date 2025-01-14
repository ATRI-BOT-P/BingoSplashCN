package cn.bingosplash.events;

import cn.bingosplash.BingoSplashCN;
import cn.bingosplash.loggers.BSLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.CompletableFuture;

public final class RenderGameOverlay {
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || BingoSplashCN.titleManager.getLastMessage() == null || mc.thePlayer == null) {
            return;
        }
        FontRenderer fontRenderer = mc.fontRendererObj;
        String message = BingoSplashCN.titleManager.getLastMessage();
        mc.fontRendererObj.drawString(message, (event.resolution.getScaledWidth() - fontRenderer.getStringWidth(message)) / 2, 66, 0xFFFFFF);
        CompletableFuture.runAsync(() -> {
            try {
                // sleep 5s 后消失标题
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                BSLogger.severe("Sleep thread catch: " + e.getMessage());
            } finally {
                BingoSplashCN.titleManager.setLastMessage(null);
            }
        }).exceptionally(e -> {
            BSLogger.severe("Error occurred: " + e.getMessage());
            return null;
        });
    }
}