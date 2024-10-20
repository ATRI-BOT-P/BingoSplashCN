package cn.bingosplash.events;

import cn.bingosplash.BingoSplashCN;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || BingoSplashCN.messageManager.getLastMessage() == null) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fontRenderer = mc.fontRendererObj;

        String message = BingoSplashCN.messageManager.getLastMessage();
        int x = (event.resolution.getScaledWidth() - fontRenderer.getStringWidth(message)) / 2;
        int y = 66;
        int color = 0xFFFFFF;

        fontRenderer.drawString(message, x, y, color);
        new Thread(() -> {
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BingoSplashCN.messageManager.setLastMessage(null);
        }).start();
    }
}